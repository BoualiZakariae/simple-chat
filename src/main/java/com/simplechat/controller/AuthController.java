package com.simplechat.controller;

import com.datastax.driver.core.utils.UUIDs;
import com.simplechat.model.User;
import com.simplechat.repository.CacheRepository;
import com.simplechat.repository.UserRepository;
import com.simplechat.security.AuthKeyHelper;
import com.simplechat.service.AuthService;
import com.simplechat.util.StringHelper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthService authService;

    @Autowired
    CacheRepository cacheRepository;

    /**
     * send activation code to the given mobile number
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/sendcode", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> sendCode(@RequestBody String request) {

        JSONObject jsonObjectRequest = new JSONObject(request);

        String mobile = jsonObjectRequest.getString("mobile");

        // @todo validate mobile number

        // generate random key
        int code = StringHelper.generateRandomNumber(6);

        // store user_id and code in redis in redis
        cacheRepository.storeActivationCodeForMobile(mobile, String.valueOf(code));

        // @todo send sms


        return new ResponseEntity<String>(new JSONObject().put("status", "ok").toString(), HttpStatus.OK);
    }

    /**
     * login user with activation code that was send to user
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/user/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> login(@RequestBody String request) {

        JSONObject jsonObjectRequest = new JSONObject(request);

        String mobile = jsonObjectRequest.getString("mobile");
        String code = jsonObjectRequest.getString("code");

        // @todo validate inputs

        String result = null;
        // get info by activation code and mobile
        result = cacheRepository.getInfoByActivationCodeAndMobile(mobile, code);

        if (result != null) {

            // generate an auth_key
            String auth_key = AuthKeyHelper.generateAuthKey();

            // get user id by mobile number
            User userResult = userRepository.getIdByMobile(mobile);
            String userId = null;

            if(userResult == null) {

                // if not exist create it in persistence database
                userId = UUIDs.random().toString();
                User user = new User();
                user.setId(userId);
                user.setMobile(mobile);
                userRepository.save(user);
            }
            else {
                userId = userResult.getId();
            }

            authService.addAuthKeyForUser(userId, auth_key);

            return new ResponseEntity<String>(new JSONObject().put("status", "ok").put("auth_key", auth_key).toString(), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<String>(new JSONObject().put("status", "error").put("auth_key", "").toString(), HttpStatus.OK);

        }
    }

}