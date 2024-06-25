package com.angarium.constraint.validator;

import com.angarium.constraint.MaxDays;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * Validator-Klasse zur Überprüfung, ob eine Anzahl von Tagen innerhalb des erlaubten Bereichs liegt.
 * Diese Klasse implementiert die Logik zur Validierung der `@MaxDays` Annotation.
 */
@ApplicationScoped
public class MaxDaysValidator implements ConstraintValidator<MaxDays, Integer>{

    /**
     * Die maximale Anzahl von Tagen, die aus der Konfiguration geladen wird.
     */
    @ConfigProperty(name = "angarium.config.max-days")
    int maxDays;

    /**
     * Überprüft, ob die gegebene Anzahl von Tagen gültig ist.
     *
     * @param days der zu überprüfende Wert
     * @param constraintValidatorContext Kontextinformationen für die Validierung
     * @return true, wenn die Anzahl der Tage gültig ist, andernfalls false
     */
    @Override
    public boolean isValid(Integer days, ConstraintValidatorContext constraintValidatorContext) {
        if(days == null){
            return true;
        }
        return days > 0 && days <= maxDays;
    }
}
