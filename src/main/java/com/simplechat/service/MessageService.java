package com.simplechat.service;

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
     * @param authKey current user AuthKey
     * @param peer Target user
     * @param offset Number of list elements to be skipped
     */
    void messagesGetHistory(String authKey, String peer, int offset);
}
