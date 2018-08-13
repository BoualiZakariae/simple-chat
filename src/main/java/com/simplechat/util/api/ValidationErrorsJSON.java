package com.simplechat.util.api;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mohsen Jahanshahi
 */
public class ValidationErrorsJSON {

    private List<ValidationError> errors = new ArrayList();

    public void addError(ValidationError validationError) {
        errors.add(validationError);
    }

    public ValidationErrorsJSON addError(ValidationErrorStatus status, String fieldName) {
        errors.add(new ValidationError(status, fieldName));

        return this;
    }

    /**
     * get json format of validation errors
     * @return
     */
    public JSONObject getResult() {

        JSONArray errorArr = new JSONArray();

        for(ValidationError validationError : errors) {
            errorArr.put(validationError.getJSON());
        }

        return new JSONObject().put("validation", errorArr);
    }
}
