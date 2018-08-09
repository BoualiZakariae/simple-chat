package com.simplechat.service;

import com.simplechat.exception.NotFoundException;
import com.simplechat.model.Contact;
import com.simplechat.repository.CacheRepository;
import com.simplechat.repository.UserRepository;
import org.json.JSONArray;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

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

    private final String userId = "ff5eb38b-b9fe-4e6d-bad1-02ab3fe7146a";
    private String authKey = "22222-2";
    private Contact contacts1;

    @Before
    public void setup() {
        contacts1 = new Contact("09999", "myFirstName", "myLastName");
    }

    @Test
    public void usersGetUsers_verifyCallRepository_with_proper_params() {

        String[] s = {"11", "12", "15888"};
        //   String s_comma = "11, 12, 15888";

        userService.usersGetUsers(authKey, s);

        verify(userRepository).getUsersByIds(s);
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

    @Test(expected = NotFoundException.class)
    public void getUserIdByAuthKey_if_userId_not_exist() throws NotFoundException {

        // userId not in cache nor in persistence
        when(cacheRepository.getUserIdByAuthKey(authKey)).thenReturn(null);
        when(userRepository.getUserByAuthKey(authKey)).thenReturn(null);

        userService.getUserIdByAuthKey(authKey);


    }

    @Test
    public void importContacts_with_one_contact() {

        Set<Contact> contacts = new HashSet<>();
        contacts.add(contacts1);

        userService.importContacts(userId, contacts, true);


        Map<String, String> contatsMap = new HashMap<>();
        contatsMap.put(contacts1.getMobile(), new JSONArray().put(contacts1.getFname()).put(contacts1.getLname()).toString());

        verify(userRepository).setContactForUserByUserId(UUID.fromString(userId), contatsMap);
    }

    @Test
    public void getContacts_with_one_contact() {

        Map<String, Map> repoResult = new HashMap<>();
        Map<String, String> contatsMap = new HashMap<>();
        contatsMap.put(contacts1.getMobile(), new JSONArray().put(contacts1.getFname()).put(contacts1.getLname()).toString());
        repoResult.put("contacts", contatsMap);

        when(userRepository.getUserContactsByUserId(UUID.fromString(userId))).thenReturn(repoResult);

        Set<Contact> resultContacts = userService.getContacts(UUID.fromString(userId));

        assertEquals(1, resultContacts.size());

        Contact resultContact1 = resultContacts.iterator().next();

        assertEquals(contacts1.getMobile(), resultContact1.getMobile());
        assertEquals(contacts1.getFname(), resultContact1.getFname());
        assertEquals(contacts1.getLname(), resultContact1.getLname());
    }
}