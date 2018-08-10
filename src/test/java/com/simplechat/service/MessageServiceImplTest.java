package com.simplechat.service;

import com.simplechat.exception.NotFoundException;
import com.simplechat.model.Message;
import com.simplechat.repository.CacheRepository;
import com.simplechat.repository.MessageRepository;
import com.simplechat.repository.UserRepository;
import com.simplechat.websocket.MessageHandler;
import com.simplechat.websocket.WebSocketPool;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Mohsen Jahanshahi
 */
@RunWith(MockitoJUnitRunner.class)
public class MessageServiceImplTest {

    @Mock
    MessageRepository messageRepository;

    @Mock
    CacheRepository cacheRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    UserService userService;

    @Mock
    MessageHandler messageHandler;

    @InjectMocks
    private MessageService messageService = new MessageServiceImpl();

    private final String userId = "11";
    private final String toId = "8888";
    private final String authKey = "22222-2";
    private final String msg = "salam chetori?";
    private final Date date1 = new Date();

    @Before
    public void setup() {

        WebSocketSession session = mock(WebSocketSession.class);
        Set<WebSocketSession> newUserSessions = new HashSet<>();
        newUserSessions.add(session);
        WebSocketPool.websockets.put(toId, newUserSessions);
    }

    @Test
    public void sendMessageToUser_all_fine_check_save_to_persistence() throws NotFoundException{

        when(userService.getUserIdByAuthKey(authKey)).thenReturn(userId);

        List<Message> messages = new ArrayList<>();
        when(messageRepository.save(any())).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                messages.add((Message)args[0]);
                return null;
            }
        });

        messageService.sendMessageToUser(authKey, toId, msg);

        verify(userService).getUserIdByAuthKey(authKey);

        // 1
        assertEquals(messages.get(0).getMsg() , msg);
        assertEquals(messages.get(0).getSender_id() , userId);
        assertEquals(messages.get(0).getTo_id() , toId);
        // 2
        assertEquals(messages.get(1).getMsg() , msg);
        assertEquals(messages.get(1).getSender_id() , toId);
        assertEquals(messages.get(1).getTo_id() , userId);

    }

//    @Test
//    public void messagesGetHistory_check_call_repository_on_success() throws NotFoundException{
//
//        int offset = 110;
//        when(userService.getUserIdByAuthKey(authKey)).thenReturn(userId);
//
//        messageService.messagesGetHistory(authKey, toId, offset);
//
//        verify(messageRepository).messagesGetHistory(userId, toId, offset);
//    }

}