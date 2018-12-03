package com.shuyan.validation.common.config;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 用于返回错误信息的对象
 * @author will
 */
@Data
@AllArgsConstructor
public class MessageResponse {
    private int code;
    private String message;
}
