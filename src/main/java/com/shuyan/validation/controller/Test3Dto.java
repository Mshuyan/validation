package com.shuyan.validation.controller;

import lombok.Data;

import javax.validation.constraints.Min;

/**
 * @author will
 */
@Data
public class Test3Dto {
    public interface Group1{}
    public interface Group2{}

    @Min(value = 18,groups = {Test3Dto.Group1.class})
    @Min(value = 16,groups = {Test3Dto.Group2.class})
    private Long age;
}
