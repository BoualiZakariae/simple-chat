package com.simplechat.util.api;

/**
 * @author Mohsen Jahanshahi
 * status of requested result
 */
public enum ResultStatus {

    SUCCESS(1),
    NOT_FOUND(2),
    VALIDATION_ERROR(3),
    ACCESS_DENIED(4),
    UNKNOWN_ERROR(5),
    ;

    private int code;

    ResultStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
