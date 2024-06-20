package com.angarium.constraint.validator;

import com.angarium.constraint.MaxDownloads;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * Validator-Klasse zur Überprüfung, ob eine Anzahl von Downloads innerhalb des erlaubten Bereichs liegt.
 * Diese Klasse implementiert die Logik zur Validierung der `@MaxDownloads` Annotation.
 */
@ApplicationScoped
public class MaxDownloadsValidator implements ConstraintValidator<MaxDownloads, Integer> {

    /**
     * Die maximale Anzahl von Downloads, die aus der Konfiguration geladen wird.
     */
    @ConfigProperty(name = "angarium.config.max-downloads")
    int maxDownloads;

    /**
     * Überprüft, ob die gegebene Anzahl von Downloads gültig ist.
     *
     * @param downloads der zu überprüfende Wert
     * @param constraintValidatorContext Kontextinformationen für die Validierung
     * @return true, wenn die Anzahl der Downloads gültig ist, andernfalls false
     */
    @Override
    public boolean isValid(Integer downloads, ConstraintValidatorContext constraintValidatorContext) {
        if(downloads == null){
            return true;
        }
        return downloads > 0 && downloads <= maxDownloads;
    }
}
