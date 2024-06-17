package com.angarium.constraint.validator;

import com.angarium.constraint.MaxDays;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class MaxDaysValidator implements ConstraintValidator<MaxDays, Integer>{

    @ConfigProperty(name = "angarium.config.max-days")
    int maxDays;

    @Override
    public boolean isValid(Integer days, ConstraintValidatorContext constraintValidatorContext) {
        if(days == null){
            return true;
        }
        if(days > 0 && days <= maxDays) {
            return true;
        }

        return false;
    }
}
