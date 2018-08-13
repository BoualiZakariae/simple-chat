package com.simplechat.controller;

import com.datastax.driver.core.utils.UUIDs;
import com.simplechat.model.User;
import com.simplechat.repository.CacheRepository;
import com.simplechat.repository.UserRepository;
import com.simplechat.security.AuthKeyHelper;
import com.simplechat.service.AuthService;
import com.simplechat.util.StringHelper;
import com.simplechat.util.api.ResponseEntityGenerator;
import com.simplechat.util.api.ResultStatus;
import com.simplechat.util.api.ValidationErrorStatus;
import com.simplechat.util.api.ValidationErrorsJSON;
import com.simplechat.util.validation.Validation;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

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


        String mobile;
        if(!jsonObjectRequest.has("mobile")) {
            return ResponseEntityGenerator.createErrorResponseEntity(ResultStatus.VALIDATION_ERROR, new ValidationErrorsJSON().addError(ValidationErrorStatus.NOT_EMPTY, "mobile").getResult());
        }
        mobile = jsonObjectRequest.getString("mobile");

        if(!Validation.isMobileValid(mobile)) {
            return ResponseEntityGenerator.createErrorResponseEntity(ResultStatus.VALIDATION_ERROR, new ValidationErrorsJSON().addError(ValidationErrorStatus.NOT_MOBILE, "mobile").getResult());
        }

        // generate random key
        int code = StringHelper.generateRandomNumber(6);

        // store user_id and code in redis in redis
        cacheRepository.storeActivationCodeForMobile(mobile, String.valueOf(code));

        // @todo send sms with broker

        return ResponseEntityGenerator.createSuccesResponseEntity();
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

        // get and validate mobile
        String mobile;
        if(!jsonObjectRequest.has("mobile")) {
            return ResponseEntityGenerator.createErrorResponseEntity(ResultStatus.VALIDATION_ERROR, new ValidationErrorsJSON().addError(ValidationErrorStatus.NOT_EMPTY, "mobile").getResult());
        }
        mobile = jsonObjectRequest.getString("mobile");

        if(!Validation.isMobileValid(mobile)) {
            return ResponseEntityGenerator.createErrorResponseEntity(ResultStatus.VALIDATION_ERROR, new ValidationErrorsJSON().addError(ValidationErrorStatus.NOT_MOBILE, "mobile").getResult());
        }

        // get and validate code
        String code;
        if(!jsonObjectRequest.has("code")) {
            return ResponseEntityGenerator.createErrorResponseEntity(ResultStatus.VALIDATION_ERROR, new ValidationErrorsJSON().addError(ValidationErrorStatus.NOT_EMPTY, "code").getResult());
        }
        code = jsonObjectRequest.getString("code");

        String result = null;
        // get info by activation code and mobile
        result = cacheRepository.getInfoByActivationCodeAndMobile(mobile, code);

        if (result != null) {

            // generate an auth_key
            String authKey = AuthKeyHelper.generateAuthKey();

            // get user id by mobile number
            User userResult = userRepository.getIdByMobile(mobile);
            UUID userId = null;

            if(userResult == null) {

                // if not exist create it in persistence database
                userId = UUIDs.random();
                User user = new User();
                user.setId(userId);
                user.setMobile(mobile);
                userRepository.save(user);
            }
            else {
                userId = userResult.getId();
            }

            authService.addAuthKeyForUser(userId.toString(), authKey);

            return ResponseEntityGenerator.createSuccesResponseEntity(new JSONObject().put("auth_key", authKey));

        }
        else {
            return ResponseEntityGenerator.createErrorResponseEntity(ResultStatus.NOT_FOUND);
        }
    }

}