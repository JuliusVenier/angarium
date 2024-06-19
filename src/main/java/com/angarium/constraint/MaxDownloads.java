package com.angarium.constraint;

import com.angarium.constraint.validator.MaxDownloadsValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation zur Validierung der maximal erlaubten Downloads für ein Feld.
 * Diese Annotation kann auf Felder angewendet werden, um sicherzustellen,
 * dass der Wert innerhalb eines bestimmten Bereichs liegt.
 */
@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = MaxDownloadsValidator.class)
public @interface MaxDownloads {

    String message() default "Max downloads is too low or too high";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}