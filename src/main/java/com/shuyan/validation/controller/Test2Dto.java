package com.shuyan.validation.controller;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author will
 */
@Data
public class Test2Dto {
    @NotBlank
    private String name;
}
