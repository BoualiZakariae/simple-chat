package com.simplechat.service;

public interface AuthService {

    /**
     * add auth_key for user with given mobile number to db
     * @param mobile
     * @param auth_key
     */
    void addAuthKeyForUser(String userId, String auth_key);

    void loginUserWithActivationCode(String mobile, String code);
}
