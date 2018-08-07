package com.simplechat.service;

import com.datastax.driver.core.utils.UUIDs;
import com.simplechat.messagebroker.Sender;
import com.simplechat.exception.NotFoundException;
import com.simplechat.model.Message;
import com.simplechat.repository.CacheRepository;
import com.simplechat.repository.MessageRepository;
import com.simplechat.repository.UserRepository;
import com.simplechat.websocket.MessageHandler;
import com.simplechat.websocket.WebSocketPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;

/**
 * @author Mohsen Jahanshahi
 */
@Service
public class MessageServiceImpl implements MessageService{

    private static final Logger LOGGER = LoggerFactory.getLogger(Sender.class);

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    CacheRepository cacheRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    MessageHandler messageHandler;

    @Override
    public void sendMessageToUser(String authKey, String toId, String msg) {

        try {
            messageHandler.sendMessageToUser(toId, msg);
        } catch (IOException e) {
            return;
        }

        storeMessageToUser(authKey, toId, msg);

    }

    @Override
    public void messagesGetHistory(String authKey, String peer, int offset) {

        // get user id
        String senderId;
        try {
            senderId = userService.getUserIdByAuthKey(authKey);
        } catch (NotFoundException e) {
            return;
        }

        messageRepository.messagesGetHistory(senderId, peer, offset);
    }

    /**
     * store message into persistence
     * @param authKey
     * @param toId
     * @param msg
     */
    private void storeMessageToUser(String authKey, String toId, String msg) {

        // get user id
        String senderId;
        try {
            senderId = userService.getUserIdByAuthKey(authKey);
        } catch (NotFoundException e) {
            return;
        }
        // store in cassandra
        String messageId = UUIDs.timeBased().toString();
        Message message1 = new Message();
        message1.setId(messageId);
        message1.setSender_id(senderId);
        message1.setTo_id(toId);
        message1.setMsg(msg);
        messageRepository.save(message1);

        Message message2 = new Message();
        message2.setId(messageId);
        message2.setSender_id(toId);
        message2.setTo_id(senderId);
        message2.setMsg(msg);
        messageRepository.save(message2);
    }
}
