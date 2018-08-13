package com.simplechat.controller;

import com.simplechat.exception.NotFoundException;
import com.simplechat.util.api.ResponseEntityGenerator;
import com.simplechat.util.api.ResultStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author Mohsen Jahanshahi
 */
@ControllerAdvice
@RestController
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<String> handleUserNotFoundException(NotFoundException ex, WebRequest request) {

        return ResponseEntityGenerator.createErrorResponseEntity(ResultStatus.NOT_FOUND);

    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<String> generalException(Exception ex, WebRequest request) {

        return ResponseEntityGenerator.createErrorResponseEntity(ResultStatus.UNKNOWN_ERROR);

    }
}
