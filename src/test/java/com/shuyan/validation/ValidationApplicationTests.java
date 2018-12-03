package com.shuyan.validation;

import com.shuyan.validation.controller.Test2Dto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ValidationApplicationTests {
    // 创建校验器
    private static Validator engineValidator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void test()
    {
        Test2Dto dto = new Test2Dto();
        // 执行校验动作
        List<String> validate = validate(dto);
        System.out.println(validate);
    }

    /**
     * 手动校验方法
     * @param dto 需要校验的对象
     * @param groups 分组
     * @return 校验结果
     */
    private static List<String> validate(Object dto, Class<?>... groups) {
        Set<ConstraintViolation<Object>> constraintViolations = engineValidator.validate(dto, groups);
        if (constraintViolations.size() > 0) {
            List<String> fieldErrorList = new ArrayList<>();
            for (ConstraintViolation<Object> constraintViolation : constraintViolations) {
                String filedError = constraintViolation.getPropertyPath() + ": " + constraintViolation.getMessage();
                fieldErrorList.add(filedError);
            }
            return fieldErrorList;
        }
        return null;
    }
}
