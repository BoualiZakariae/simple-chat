package com.simplechat.websocket;

import com.simplechat.exception.NotFoundException;
import com.simplechat.messagebroker.Sender;
import com.simplechat.messagebroker.TopicType;
import com.simplechat.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private static final Logger logger = LogManager.getLogger(WebSocketHandler.class);

    @Autowired
    UserService userService;

    @Autowired
    MessageHandler messageHandler;

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        String parameters[] = session.getUri().getQuery().split("=");

        if(parameters.length == 2 && parameters[0].equals("auth_key")) {
            String authKey = parameters[1];

            // check if auth exist
            // get user id
            String userId;
            try {
                userId = userService.getUserIdByAuthKey(authKey);
            } catch (NotFoundException e) {
                // if user id not fount that mean auth_key is invalid so close connection
                return;
            }
            // delete socket session from web socket pool
            messageHandler.removeFromSessionToPool(userId, session);
        }
        else {
        }

    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        // get auth_key
        String parameters[] = session.getUri().getQuery().split("=");

        if(parameters.length == 2 && parameters[0].equals("auth_key")) {
            String authKey = parameters[1];

            // check if auth exist
            // get user id
            String userId;
            try {
                userId = userService.getUserIdByAuthKey(authKey);
            } catch (NotFoundException e) {
                // if user id not fount that mean auth_key is invalid so close connection
                session.close();
                return;
            }

            messageHandler.addSessionToPool(userId, session);
        }
        else {
            session.close();
        }

    }

    @Autowired
    private Sender sender;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {

        // A message has been received
        logger.info("new message recieved from socket: " + textMessage.getPayload());

        JSONObject jsonObject = new JSONObject(textMessage.getPayload());
        String method = jsonObject.getString("method");

        if(method == null) {
            return;
        }

        // if method name is not exist
        if(TopicType.valueOf(method) == null) {
            return;
        }

        // send message to broker
        sender.send(TopicType.valueOf(method).toString(), textMessage.getPayload());
    }
}