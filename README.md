# validation
## 依赖

+ 非web项目

  对于非web项目，引入如下依赖即可使用参数校验功能

  ```xml
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-validation</artifactId>
  </dependency>
  ```

+ web项目

  对于web项目，因为`spring-boot-starter-web`依赖中已经引入了相关依赖，所以不需要单独引入

## 用法

### 基础

+ 参数校验

  对Controller方法的参数直接进行校验时，需要进行如下两步：

  + 在`Controller`上加上`@Validated`注解
  + 在参数前加入需要使用的参数校验注解

  ```java
  @RestController
  @Validated
  public class TestController {
      @GetMapping("/test1")
      public String test1(@NotBlank String str){
          return str;
      }
  }
  ```

+ 实体类属性校验

  当使用实体类接收多个参数时，需要进行如下两步：

  + 在`Controller`方法参数列表的实体类对象前加上`@Validated`注解
  + 在实体类属性中使用校验注解

  ```java
  @GetMapping("/test2")
  public String test2(@Validated TestDto dto){
      return dto.getName();
  }
  ```

  ```java
  @Data
  public class TestDto {
      @NotBlank
      private String name;
  }
  ```


### 分组校验

+ 概念

  ​	同样1个实体类，被多个接口用于接受参数，但是这些接口的参数校验规则可能不同，为了解决这个问题，提出了分组校验

  ​	在实体类中定义校验规则时，先定义几个分组，然后针对每个分组分别制定一套校验规则，在`Controller`中对参数进行校验时，通过制定分组来决定使用哪个分组的校验规则

+ 注意

  进行分组校验时，`Controller`方法参数上必须使用`@Validated`，不能使用`@Valid`

+ demo

  ```java
  @Data
  public class Test3Dto {
      public interface Group1{}
      public interface Group2{}
  
      @Min(value = 18,groups = {Test3Dto.Group1.class})
      @Min(value = 16,groups = {Test3Dto.Group2.class})
      private Long age;
  }
  ```

  ```java
  @GetMapping("/test3")
  public Long test3(@Validated(value = Test3Dto.Group2.class) Test3Dto dto){
      return dto.getAge();
  }
  ```


### 嵌套校验

+ 概念

  ​	当1个接受参数的实体类内的属性是另外1个实体类对象时，想要对内部的实体类对象内的属性进行校验，就需要进行嵌套校验

+ 方法

  在`实体类属性参数校验`基础上，进行如下2步，即可进行嵌套参数校验：

  + 在属性上加上`@Valid`注解

    > 这里不可以使用`@Validated`注解

  + 在该属性的类内的属性上加上校验注解

+ demo

  ```java
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
  
  ```

  ```java
  @GetMapping("/test4")
  public String test4(@Validated Test4Dto dto){
      return dto.getTest().getName();
  }
  ```


### 全局捕获异常

> 使用`@RestControllerAdvice`注解全局捕获参数校验产生的异常并返回给前段，参见:
>
> + [MyControllerAdvice](./src/main/java/com/shuyan/validation/common/config/MyControllerAdvice.java) 
> + [springMVC学习笔记](https://github.com/Mshuyan/springMVC/blob/master/demo01-annotation/springMVC%E6%B3%A8%E8%A7%A3%E7%AF%87.md)  

## 注解

### 校验注解

```
@Null   被注释的元素必须为 null    
@NotNull    被注释的元素必须不为 null    
@AssertTrue     被注释的元素必须为 true    
@AssertFalse    被注释的元素必须为 false    
@Min(value)     被注释的元素必须是一个数字，其值必须大于等于指定的最小值    
@Max(value)     被注释的元素必须是一个数字，其值必须小于等于指定的最大值    
@DecimalMin(value)  被注释的元素必须是一个数字，其值必须大于等于指定的最小值    
@DecimalMax(value)  被注释的元素必须是一个数字，其值必须小于等于指定的最大值    
@Size(max=, min=)   被注释的元素的大小必须在指定的范围内    
@Digits (integer, fraction)     被注释的元素必须是一个数字，其值必须在可接受的范围内    
@Past   被注释的元素必须是一个过去的日期    
@Future     被注释的元素必须是一个将来的日期    
@Pattern(regex=,flag=)  被注释的元素必须符合指定的正则表达式      
@NotBlank(message =)   验证字符串非null，且长度必须大于0    
@Email  被注释的元素必须是电子邮箱地址    
@Length(min=,max=)  被注释的字符串的大小必须在指定的范围内    
@NotEmpty   被注释的字符串的必须非空    
@Range(min=,max=,message=)  被注释的元素必须在合适的范围内
```

### `@Valid`与`@Validated`

`@Valid`与`@Validated`功能基本一样，只有2点区别：

+ `Validated`不能用在属性上，所以不支持嵌套校验，而`@Valid`支持
+ `Valid`不可以用于分组校验，而`@Validated`可以

## 自定义校验注解

+ demo

  ```java
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
  ```

  ```java
  public class ListNotHasNullImpl implements ConstraintValidator<ListNotHasNull, List> {
      private int value;
  
      @Override
      public void initialize(ListNotHasNull constraintAnnotation) {
          //传入value 值，可以在校验中使用
          this.value = constraintAnnotation.value();
      }
  
      @Override
      public boolean isValid(List list, ConstraintValidatorContext constraintValidatorContext) {
          for (Object object : list) {
              if (object == null) {
                  //如果List集合中含有Null元素，校验失败
                  return false;
              }
          }
          return true;
      }
  }
  ```

  > 此时，该注解就可以像其他校验注解进行校验了

## 手动校验

> 在非web项目中，无法在`Controller`中使用`@Validated`注解执行校验动作，所以需要手动执行校验动作。
>
> 参见[ValidationApplicationTests](./src/test/java/com/shuyan/validation/ValidationApplicationTests.java) 

```java
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
```

## 校验信息配置文件

+ 概述

  ​	校验过程中产生的各种错误信息可以统一在`resources/ValidationMessage.properties`文件中进行配置，然后在指定校验注解的`message`属性时，可以通过`{}`来引用配置文件中的错误信息

+ demo

  + ValidationMessage.properties

    ```properties
    auth.RegisterFormDto.username=2001::格式错误
    ```

  + 实体类

    ```java
    @Pattern(regexp = ValidationUtil.usernameRegExp,message = "{auth.RegisterFormDto.username}")
    private String username;
    ```






