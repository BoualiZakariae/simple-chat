package com.simplechat.messagebroker;

import com.simplechat.exception.NotFoundException;
import com.simplechat.model.User;
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
import java.util.Set;

@Service
public class UserReceiver {

    private static final Logger LOG = LoggerFactory.getLogger(UserReceiver.class);

    @Autowired
    UserService userService;

    @Autowired
    MessageHandler messageHandler;

    @KafkaListener(topics = "USERS_GET_USERS")
    public void usersGetUsers(@Payload String message, @Headers MessageHeaders headers) {

        // send to socket
        JSONObject jsonObject = new JSONObject(message);

        String authKey = jsonObject.getString("auth_key");
        JSONArray peers = jsonObject.getJSONArray("peers");

        // convert jsonarray of peers to string array
        String[] userIds = new String[peers.length()];
        for (int i = 0; i < userIds.length; i++) {
            userIds[i] = peers.optString(i);
        }

        Set<User> usersEntities = userService.usersGetUsers(authKey, userIds);

        JSONArray users = new JSONArray();

        for(User userEntity : usersEntities) {

            JSONObject jsonObjectUser = new JSONObject();
            jsonObjectUser.put("fname", userEntity.getFname());
            jsonObjectUser.put("lname", userEntity.getLname());
            jsonObjectUser.put("id", userEntity.getId());

            users.put(jsonObjectUser);
        }

        // get user id from auth_key to send data to
        String userId = null;
        try {
            userId = userService.getUserIdByAuthKey(authKey);
        } catch (NotFoundException e) {
            return;
        }

        try {
            messageHandler.sendMessageToUser(userId, users.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}