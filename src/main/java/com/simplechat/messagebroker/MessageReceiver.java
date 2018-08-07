package com.simplechat.messagebroker;

import com.simplechat.service.MessageService;
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

@Service
public class MessageReceiver {

    private static final Logger LOG = LoggerFactory.getLogger(MessageReceiver.class);

    @Autowired
    MessageService messageService;

    @KafkaListener(topics = "MESSAGES_SEND_TO_USER")
    public void messagesSendToUser(@Payload String message, @Headers MessageHeaders headers) {

        // send to socket
        JSONObject jsonObject = new JSONObject(message);

        if(WebSocketPool.websockets.get(jsonObject.getString("send_to")) != null) {

            String sendTo = jsonObject.getString("send_to");
            String msg = jsonObject.getString("msg");
            String authKey = jsonObject.getString("auth_key");

            messageService.sendMessageToUser(authKey, sendTo, msg);

        }
    }

}