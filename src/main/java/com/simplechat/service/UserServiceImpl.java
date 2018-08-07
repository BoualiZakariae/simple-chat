package com.simplechat.service;

import com.simplechat.exception.NotFoundException;
import com.simplechat.model.Contact;
import com.simplechat.model.User;
import com.simplechat.repository.CacheRepository;
import com.simplechat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author Mohsen Jahanshahi
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CacheRepository cacheRepository;

    @Override
    public Set<User> usersGetUsers(String auh_key, String[] userIds) {

        int i = 0;
        StringBuilder commaSeperatedIds = new StringBuilder();
        for (String id : userIds) {
            if ((i == 0)) {
                commaSeperatedIds.append(id);
            } else {
                commaSeperatedIds.append(", ").append(id);
            }

            i++;
        }

        return userRepository.getUsersByIds(commaSeperatedIds.toString());

    }

    @Override
    public String getUserIdByAuthKey(String authKey) throws NotFoundException {
        /** first try to get it from cache if not exist get from persistence */
        String userId = cacheRepository.getUserIdByAuthKey(authKey);

        if (userId != null) {
            return userId;
        }

        // get userId from persistence
        userId = userRepository.getUserByAuthKey(authKey);

        if (userId != null) {
            // authkey not exist in cache so insert into to cache
            cacheRepository.addAuthKeyForUser(userId, authKey);
        } else {
            throw new NotFoundException("user with authkey=" + authKey + " not founded");
        }

        return userId;
    }

    @Override
    public void importContacts(String userId, Set<Contact> contacts, boolean replace) {

    }
}
