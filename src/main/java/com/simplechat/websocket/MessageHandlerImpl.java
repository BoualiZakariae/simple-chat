package com.simplechat.websocket;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Mohsen Jahanshahi
 */
@Service
public class MessageHandlerImpl implements MessageHandler {

    @Override
    public void addSessionToPool(String userId, WebSocketSession session) {

        // check if there is session for userId
        Set<WebSocketSession> userSessions = WebSocketPool.websockets.get(userId);

        if (userSessions != null) {
            userSessions.add(session);
            WebSocketPool.websockets.put(userId, userSessions);
        } else {
            Set<WebSocketSession> newUserSessions = new HashSet<>();
            newUserSessions.add(session);
            WebSocketPool.websockets.put(userId, newUserSessions);
        }

    }

    @Override
    public void sendMessageToUser(String userId, String message) throws IOException {

        // get user sessions
        Set<WebSocketSession> userSessions = WebSocketPool.websockets.get(userId);

        if (userSessions == null) {
            return;
        }

        TextMessage textMessage = new TextMessage(message);
        for (WebSocketSession session : userSessions) {
            session.sendMessage(textMessage);
        }

    }

    @Override
    public void removeFromSessionToPool(String userId, WebSocketSession session) {
        // check if there is session for userId
        Set<WebSocketSession> userSessions = WebSocketPool.websockets.get(userId);

        if (userSessions != null) {
            for (WebSocketSession sessionItem : userSessions) {
                if (sessionItem.equals(session)) {
                    userSessions.remove(session);
                }
            }
        }
        WebSocketPool.websockets.put(userId, userSessions);
    }
}
