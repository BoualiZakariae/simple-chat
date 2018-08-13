package com.simplechat.util.api;

/**
 * @author Mohsen Jahanshahi
 */
public enum ValidationErrorStatus {

    NOT_EMPTY(1),
    NOT_ALPHA(2),
    NOT_ALPHA_NUM(3),
    NOT_EMAIL(4),
    NOT_MOBILE(5),
    ;

    private int code;

    ValidationErrorStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
