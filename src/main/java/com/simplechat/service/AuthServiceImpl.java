package com.simplechat.service;

import com.simplechat.repository.CacheRepository;
import com.simplechat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    CacheRepository cacheRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public void addAuthKeyForUser(String userId, String authKey) {

        // 1- add to cache
        cacheRepository.addAuthKeyForUser(userId, authKey);

        // 2- add to persistence
        userRepository.addAuthKeyForUser(userId, authKey, new Date());
    }

    @Override
    public void loginUserWithActivationCode(String mobile, String code) {

    }
}
