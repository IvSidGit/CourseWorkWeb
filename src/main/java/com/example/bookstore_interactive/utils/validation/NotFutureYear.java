package com.example.bookstore_interactive.utils.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NotFutureYearValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotFutureYear {
    String message() default "Год не может быть в будущем";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}