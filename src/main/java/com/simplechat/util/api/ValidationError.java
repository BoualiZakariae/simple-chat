package com.simplechat.util.api;

import org.json.JSONObject;

/**
 * @author Mohsen Jahanshahi
 */
public class ValidationError {

    private ValidationErrorStatus validationErrorStatus;
    private String fieldName;

    public ValidationError(ValidationErrorStatus status, String fieldName) {
        validationErrorStatus = status;
        this.fieldName = fieldName;
    }

    public JSONObject getJSON() {
        return new JSONObject().put("code", validationErrorStatus.getCode()).put("field_name", fieldName);
    }
}
