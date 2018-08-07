package com.simplechat.service;

import com.simplechat.exception.NotFoundException;
import com.simplechat.model.Contact;
import com.simplechat.model.User;

import java.util.Set;

/**
 * @author Mohsen Jahanshahi
 */
public interface UserService {

    /**
     * return basic users with their id
     * @param peers
     * @return
     */
    Set<User> usersGetUsers(String auh_key, String[] userIds);

    /**
     * get user id by given auth_key
     * @param authKey
     * @return
     */
    String getUserIdByAuthKey(String authKey) throws NotFoundException;

    /**
     * import cantacts for user with given userId
     * @param userId
     * @param contacts list of cantact to be added for user
     * @param replace if true replace all contacts with new ones else just appent to old contact list
     */
    void importContacts(String userId, Set<Contact> contacts, boolean replace);
}
