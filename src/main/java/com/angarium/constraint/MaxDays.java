package com.angarium.constraint;


import com.angarium.constraint.validator.MaxDaysValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = MaxDaysValidator.class)
public @interface MaxDays {

    String message() default "Max days is too low or too high";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}