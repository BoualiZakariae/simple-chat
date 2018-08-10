package com.simplechat.service;

import com.simplechat.model.Message;

import java.util.List;
import java.util.UUID;

/**
 * @author Mohsen Jahanshahi
 */
public interface MessageService {

    /**
     * send message to user also save to persistence
     * @param authKey
     * @param to_id
     * @param msg
     */
    public void sendMessageToUser(String authKey, String to_id, String msg);

    /**
     * return message history for chat
     * @param userId current user id
     * @param toUserId Target user id
     * @param offsetId if not null message before this will return
     */
    List<Message> messagesGetHistory(UUID userId, UUID toUserId, UUID offsetId);
}
