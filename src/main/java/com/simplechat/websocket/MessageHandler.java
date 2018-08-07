package com.simplechat.websocket;

import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 * @author Mohsen Jahanshahi
 */
public interface MessageHandler {

    /**
     * add session to websockets pool
     * @param userId
     * @param session
     */
    public void addSessionToPool(String userId, WebSocketSession session);

    /**
     * send message to user
     * @param userId
     * @param message
     * @throws IOException
     */
    public void sendMessageToUser(String userId, String message) throws IOException;

    /**
     * remove given session from session pool
     * @param userId
     * @param session
     */
    void removeFromSessionToPool(String userId, WebSocketSession session);
}
