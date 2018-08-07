package com.simplechat.service;

import com.simplechat.exception.NotFoundException;
import com.simplechat.repository.CacheRepository;
import com.simplechat.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * @author Mohsen Jahanshahi
 */
@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @Mock
    CacheRepository cacheRepository;

    @InjectMocks
    private UserService userService = new UserServiceImpl();

    private final String userId = "11";
    private String authKey = "22222-2";

    @Test
    public void usersGetUsers_verifyCallRepository_with_proper_params() {

        String[] s = {"11", "12", "15888"};
        String s_comma = "11, 12, 15888";

        userService.usersGetUsers(authKey, s);

        verify(userRepository).getUsersByIds(s_comma);
    }

    @Test
    public void getUserIdByAuthKey_if_userId_in_cache() {

        when(cacheRepository.getUserIdByAuthKey(authKey)).thenReturn(userId);
        String resultUserId = null;


        try {
            resultUserId = userService.getUserIdByAuthKey(authKey);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        assertEquals(userId, resultUserId);

        verify(cacheRepository).getUserIdByAuthKey(authKey);

        // no other repository call
        verifyZeroInteractions(userRepository);
    }

    @Test
    public void getUserIdByAuthKey_if_userId_not_in_cache_in_persistence() {

        when(cacheRepository.getUserIdByAuthKey(authKey)).thenReturn(null);
        when(userRepository.getUserByAuthKey(authKey)).thenReturn(userId);

        String resultUserId = null;


        try {
            resultUserId = userService.getUserIdByAuthKey(authKey);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        assertEquals(userId, resultUserId);

        verify(cacheRepository).getUserIdByAuthKey(authKey);
        verify(userRepository).getUserByAuthKey(authKey);

        // check add user to cache
        cacheRepository.addAuthKeyForUser(userId, authKey);
    }

    @Test(expected=NotFoundException.class)
    public void getUserIdByAuthKey_if_userId_not_exist() throws NotFoundException{

        // userId not in cache nor in persistence
        when(cacheRepository.getUserIdByAuthKey(authKey)).thenReturn(null);
        when(userRepository.getUserByAuthKey(authKey)).thenReturn(null);

        userService.getUserIdByAuthKey(authKey);


    }
}