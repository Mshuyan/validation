package com.shuyan.validation.common.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author will
 * 自定义参数校验注解
 * 校验 List 集合中是否有null 元素
 */

@Target({ANNOTATION_TYPE, METHOD, FIELD,PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = ListNotHasNullImpl.class)////此处指定了注解的实现类为ListNotHasNullValidatorImpl

public @interface ListNotHasNull {

    /**
     * 添加value属性，可以作为校验时的条件,若不需要，可去掉此处定义
     */
    int value() default 0;

    String message() default "List集合中不能含有null元素";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 定义List，为了让Bean的一个属性上可以添加多套规则
     */
    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        ListNotHasNull[] value();
    }
}
