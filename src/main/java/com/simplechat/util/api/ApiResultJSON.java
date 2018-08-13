package com.simplechat.util.api;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;

/**
 * @author Mohsen Jahanshahi
 */
public class ApiResultJSON implements ApiResult {

    // general data to return for a api call
    private JSONObject data;

    // error data to return for api call
    private JSONObject errors;

    private ResultStatus resultStatus;

    @Override
    public String getResult() {
        // if result is success put data and return
        if(resultStatus == ResultStatus.SUCCESS) {
            JSONObject result = new JSONObject();
            result.put("code" , resultStatus.getCode());

            if(data != null) {
                result.put("data", data);
            }
            return result.toString();
        }
        // if result not succes put error code and return
        else {
            JSONObject result = new JSONObject();
            result.put("code" , resultStatus.getCode());

            if(errors != null) {
                result.put("errors", errors);
            }
            return result.toString();
        }
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public JSONObject getErrors() {
        return errors;
    }

    public void setErrors(JSONObject errors) {
        this.errors = errors;
    }

    public ResultStatus getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(ResultStatus resultStatus) {
        this.resultStatus = resultStatus;
    }
}
