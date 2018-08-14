package com.simplechat.service;

import com.datastax.driver.core.utils.UUIDs;
import com.simplechat.exception.NotFoundException;
import com.simplechat.model.Contact;
import com.simplechat.model.User;
import com.simplechat.model.UserByMobile;
import com.simplechat.repository.CacheRepository;
import com.simplechat.repository.UserByMobileRepository;
import com.simplechat.repository.UserRepository;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Mohsen Jahanshahi
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserByMobileRepository userByMobileRepository;

    @Autowired
    CacheRepository cacheRepository;

    @Override
    public Set<User> usersGetUsers(String auh_key, String[] userIds) {
//
//        int i = 0;
//        StringBuilder commaSeperatedIds = new StringBuilder();
//        for (String id : userIds) {
//            if ((i == 0)) {
//                commaSeperatedIds.append(id);
//            } else {
//                commaSeperatedIds.append(", ").append(id);
//            }
//
//            i++;
//        }

        return userRepository.getUsersByIds(userIds);

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

        // prepare contacts to save in cassandra
        Map<String, String> contatsMap = new HashMap<>();

        Set<String> mobiles = new HashSet<>();
        for (Contact contact : contacts) {
            mobiles.add(contact.getMobile());
        }

        Set<UserByMobile> usersIdByMobiles = userByMobileRepository.getUsersIdByMobile(mobiles);

        Map<String, UUID> userMobilesMap = new HashMap<>();
        for (UserByMobile userByMobile : usersIdByMobiles) {
            userMobilesMap.put(userByMobile.getMobile(), userByMobile.getId());
        }

        for (Contact contact : contacts) {

            // if user id exist for this contact add it
            if (userMobilesMap.containsKey(contact.getMobile())) {
                contatsMap.put(contact.getMobile(), new JSONArray().put(contact.getFname()).put(contact.getLname()).put(userMobilesMap.get(contact.getMobile())).toString());
            } else {
                contatsMap.put(contact.getMobile(), new JSONArray().put(contact.getFname()).put(contact.getLname()).toString());
            }
        }

        // @todo affect replace parameter

        userRepository.setContactForUserByUserId(UUID.fromString(userId), contatsMap);
    }

    @Override
    public Set<Contact> getContacts(UUID userId) {

        Map<String, Map> result = userRepository.getUserContactsByUserId(userId);

        Map<String, String> contactsMap = result.get("contacts");
        // convert map to Contact object
        Set<Contact> contacts = new HashSet<>();
        for (Map.Entry<String, String> entry : contactsMap.entrySet()) {

            JSONArray contactData = new JSONArray(entry.getValue());

            // if has user id to
            if (contactData.length() == 3) {
                contacts.add(new Contact(entry.getKey(), contactData.getString(0), contactData.getString(1), UUID.fromString(contactData.getString(2))));

            } else {
                contacts.add(new Contact(entry.getKey(), contactData.getString(0), contactData.getString(1)));
            }
        }

        return contacts;
    }
}
