package com.shuyan.validation.common.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.io.UnsupportedEncodingException;

/**
 * @author will
 */
@RestControllerAdvice
public class MyControllerAdvice {

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity handleBindException(BindException e) {
        BindingResult result = e.getBindingResult();
        return handleException(result);
    }

    private ResponseEntity handleException(BindingResult result){
        FieldError fieldError = result.getFieldError();
        String field = fieldError.getField();
        String error = fieldError.getDefaultMessage();
        int code = 1000;
        int minLen = 2;
        try {
            error = new String(error.getBytes("ISO-8859-1"),"utf-8");
            String[] arr = error.split("::");
            if(arr.length< minLen ){
                code =1006;
                error="错误信息或错误码不存在";
            }else{
                code = Integer.valueOf(arr[0]);
                error = arr[1];
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        MessageResponse messageResponse = new MessageResponse(code, field + error);
        return new ResponseEntity<Object>(messageResponse, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ParamsInvalidException.class)
    public ResponseEntity handleCommonException(ParamsInvalidException e) {
        MessageResponse messageResponse = new MessageResponse(e.getCode(), e.getMessage());
        return new ResponseEntity<Object>(messageResponse, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity constraintViolationException(ConstraintViolationException e) {
        MessageResponse messageResponse = new MessageResponse(1008, e.getMessage());
        return new ResponseEntity<Object>(messageResponse, HttpStatus.BAD_REQUEST);
    }

}
