package com.shuyan.validation.controller;

import com.shuyan.validation.common.validate.ListNotHasNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author will
 */
@RestController
@Validated
public class TestController {

    @GetMapping("/test1")
    public String test1(@NotBlank String str){
        return str;
    }

    @GetMapping("/test2")
    public String test2(@Validated Test2Dto dto){
        return dto.getName();
    }

    @GetMapping("/test3")
    public Long test3(@Validated(value = Test3Dto.Group2.class) Test3Dto dto){
        return dto.getAge();
    }

    @GetMapping("/test4")
    public String test4(@Validated Test4Dto dto){
        return dto.getTest().getName();
    }

    @GetMapping("/test5")
    public List test5(@ListNotHasNull List<String> list){
        return list;
    }
}
