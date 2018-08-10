package com.simplechat.messagebroker;

import com.simplechat.exception.NotFoundException;
import com.simplechat.model.Message;
import com.simplechat.service.MessageService;
import com.simplechat.service.UserService;
import com.simplechat.websocket.MessageHandler;
import com.simplechat.websocket.WebSocketPool;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class MessageReceiver {

    private static final Logger LOG = LoggerFactory.getLogger(MessageReceiver.class);

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Autowired
    MessageHandler messageHandler;

    @KafkaListener(topics = "MESSAGES_SEND_TO_USER")
    public void messagesSendToUser(@Payload String message, @Headers MessageHeaders headers) {

        // send to socket
        JSONObject jsonObject = new JSONObject(message);

        if (WebSocketPool.websockets.get(jsonObject.getString("send_to")) != null) {

            String sendTo = jsonObject.getString("send_to");
            String msg = jsonObject.getString("msg");
            String authKey = jsonObject.getString("auth_key");

            messageService.sendMessageToUser(authKey, sendTo, msg);

        }
    }

    /**
     * get message history of two user
     */
    @KafkaListener(topics = "MESSAGES_GET_HISTORY")
    public void messagesGetHistory(@Payload String message, @Headers MessageHeaders headers) {

        // send to socket
        JSONObject jsonObject = new JSONObject(message);

        UUID offsetId = null;

        if(jsonObject.has("offsetId")) {
            offsetId = UUID.fromString(jsonObject.getString("offsetId"));
        }

        String toUserId = jsonObject.getString("peer");
        String authKey = jsonObject.getString("auth_key");

        UUID userId;
        try {
            userId = UUID.fromString(userService.getUserIdByAuthKey(authKey));
        } catch (NotFoundException e) {
            e.printStackTrace();
            return; // @todo return error
        }

        // get messages from 
        List<Message> messages = messageService.messagesGetHistory(userId, UUID.fromString(toUserId), offsetId);

        // wrap the result in json
        JSONArray messagesArr = new JSONArray();
        for(Message messageItem : messages) {

            JSONObject messageObj = new JSONObject();
            messageObj.put("id", messageItem.getId());
            messageObj.put("user_id", messageItem.getSender_id());
            messageObj.put("second_user_id", messageItem.getTo_id());
            messageObj.put("auther_id", messageItem.getAuthorId());
            messageObj.put("msg", messageItem.getMsg());

            // put ito array
            messagesArr.put(messageObj);
        }

        // send the result to user
        try {
            messageHandler.sendMessageToUser(userId.toString(), messagesArr.toString());
        } catch (IOException e) {
            LOG.error("error sending message to user : ", e);
        }
    }
}