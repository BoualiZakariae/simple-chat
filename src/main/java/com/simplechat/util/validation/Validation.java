package com.simplechat.util.validation;

/**
 * @author Mohsen Jahanshahi
 */
public class Validation {

    public static boolean isMobileValid(String mobile) {

        if(mobile.length() == 10) {
            return true;
        }
        return false;
    }
}
