package com.angarium.constraint.validator;

import com.angarium.constraint.MaxDays;
import com.angarium.constraint.MaxDownloads;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class MaxDownloadsValidator implements ConstraintValidator<MaxDownloads, Integer> {

    @ConfigProperty(name = "angarium.config.max-downloads")
    int maxDownloads;

    @Override
    public boolean isValid(Integer downloads, ConstraintValidatorContext constraintValidatorContext) {
        if(downloads == null){
            return true;
        }
        if(downloads > 0 && downloads <= maxDownloads) {
            return true;
        }

        return false;
    }
}
