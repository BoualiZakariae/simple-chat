package com.simplechat.util;

import java.util.Random;
import java.util.regex.Pattern;

/**
 *Miscellaneous {@link String} utility methods.
 *@author Mohsen Jahanshahi
 */
public abstract class StringHelper {

    /**
     * generate random number with given length
     *
     * @param length
     * @return
     */
    public static int generateRandomNumber(int length) {

        int max = Integer.parseInt("999999999".substring(0, length));
        int min = Integer.parseInt("100000000".substring(0, length));

        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    public static String slugify(String input) {

        if (input.isEmpty()) {
            return "";
        }
        String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
        return nowhitespace.replace("/", "-");

    }

    public static String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

    public static String randomString(int length) {
        java.util.Random rng = new java.util.Random();
        String characters = "abcdefghigklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = characters.charAt(rng.nextInt(characters.length()));
        }
        return new String(text);
    }
}





