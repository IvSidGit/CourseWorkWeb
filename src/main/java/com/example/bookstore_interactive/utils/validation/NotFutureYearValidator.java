package com.example.bookstore_interactive.utils.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import java.time.Year;

@Component
public class NotFutureYearValidator implements ConstraintValidator<NotFutureYear, Integer> {

    @Override
    public boolean isValid(Integer year, ConstraintValidatorContext context) {
        if (year == null) {
            return true;
        }

        int currentYear = Year.now().getValue();

        return year <= currentYear;
    }
}