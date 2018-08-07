package com.simplechat.security;

import java.util.UUID;

public class AuthKeyHelper {

    public static String generateAuthKey() {
        return UUID.randomUUID().toString();
    }
}
