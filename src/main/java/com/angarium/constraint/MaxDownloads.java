package com.angarium.constraint;

import com.angarium.constraint.validator.MaxDaysValidator;
import com.angarium.constraint.validator.MaxDownloadsValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = MaxDownloadsValidator.class)
public @interface MaxDownloads {

    String message() default "Max downloads is too low or too high";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}