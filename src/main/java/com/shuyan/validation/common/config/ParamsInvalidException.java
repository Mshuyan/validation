package com.shuyan.validation.common.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自定义无效参数异常
 * @author will
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class ParamsInvalidException extends RuntimeException{
    private int code;
    private String message;
}
