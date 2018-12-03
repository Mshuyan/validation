package com.shuyan.validation.controller;

import lombok.Data;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author will
 */
@Data
public class Test4Dto {

    @Valid
    @NotNull
    private Test41Dto test;
}

@Data
class Test41Dto {
    @NotBlank
    private String name;
}
