package com.simplechat.messagebroker;

/**
 * @author Mohsen Jahanshahi
 */
public enum TopicType {

    MESSAGES_SEND_TO_USER(1),
    MESSAGES_GET_HISTORY(1),
    USERS_GET_USERS(2),
    CONTACTS_IMPORT_CONTACTS(3),
    CONTACTS_GET_CONTACTS(3)
    ;

    private int code;

    TopicType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
