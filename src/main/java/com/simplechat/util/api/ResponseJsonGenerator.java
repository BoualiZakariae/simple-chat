package com.simplechat.util.api;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * @author Mohsen Jahanshahi
 */
public class ResponseJsonGenerator {

    private ResponseJsonGenerator(){}

    public static String createSuccesResponseEntity(JSONObject data) {
        ApiResultJSON apiResult = new ApiResultJSON();
        apiResult.setData(data);
        apiResult.setResultStatus(ResultStatus.SUCCESS);

        return apiResult.getResult();
    }

    public static String createSuccesResponseEntity() {
        ApiResultJSON apiResult = new ApiResultJSON();
        apiResult.setResultStatus(ResultStatus.SUCCESS);

        return apiResult.getResult();
    }

    public static String createErrorResponseEntity(ResultStatus resultStatus) {

        ApiResultJSON apiResult = new ApiResultJSON();
        apiResult.setResultStatus(resultStatus);
        return apiResult.getResult();
    }

    public static String createErrorResponseEntity(ResultStatus resultStatus, JSONObject errors) {

        ApiResultJSON apiResult = new ApiResultJSON();
        apiResult.setResultStatus(resultStatus);
        apiResult.setErrors(errors);
        return apiResult.getResult();
    }
}
