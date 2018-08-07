package com.simplechat.service;

import com.simplechat.repository.CacheRepository;
import com.simplechat.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

/**
 * @author Mohsen Jahanshahi
 */
@RunWith(MockitoJUnitRunner.class)
public class AuthServiceImplTest {

    @Mock
    UserRepository userRepository;

    @Mock
    CacheRepository cacheRepository;

    @InjectMocks
    private AuthService authService= new AuthServiceImpl();

    private final String userId = "11";
    private String authKey = "22222-2";

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void addAuthKeyForUser_verifyCallRepositories() {

        authService.addAuthKeyForUser(userId, authKey);

        verify(cacheRepository).addAuthKeyForUser(userId, authKey);
       // verify(userRepository).addAuthKeyForUser(userId, authKey, new Date());
    }
}