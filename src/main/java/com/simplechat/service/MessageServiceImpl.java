package com.simplechat.service;

import com.datastax.driver.core.utils.UUIDs;
import com.simplechat.messagebroker.Sender;
import com.simplechat.exception.NotFoundException;
import com.simplechat.model.Message;
import com.simplechat.repository.CacheRepository;
import com.simplechat.repository.MessageRepository;
import com.simplechat.repository.UserRepository;
import com.simplechat.websocket.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

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
    public List<Message> messagesGetHistory(UUID userId, UUID toUserId, UUID offsetId) {

        if(offsetId == null) {
            return messageRepository.messagesGetHistory(userId, toUserId);

        }
        else {
            return messageRepository.messagesGetHistoryWithOffset(userId, toUserId, offsetId);
        }
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
        message1.setAuthorId(UUID.fromString(senderId));
        message1.setMsg(msg);
        messageRepository.save(message1);

        Message message2 = new Message();
        message2.setId(messageId);
        message2.setSender_id(toId);
        message2.setTo_id(senderId);
        message1.setAuthorId(UUID.fromString(senderId));
        message2.setMsg(msg);
        messageRepository.save(message2);
    }
}
