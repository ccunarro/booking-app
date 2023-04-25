package com.ccunarro.hostfully.validation;

import com.ccunarro.hostfully.data.common.DateRange;
import com.ccunarro.hostfully.validation.annotation.ValidDateRange;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class ValidDateRangeConstraint implements ConstraintValidator<ValidDateRange, DateRange> {

    @Override
    public void initialize(ValidDateRange constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(DateRange dateRange, ConstraintValidatorContext constraintValidatorContext) {

        if (dateRange == null || dateRange.getStart() == null || dateRange.getEnd() == null) {
            return false;
        }
        boolean startDateOk = LocalDate.now().isBefore(dateRange.getStart());
        boolean endDateOk = dateRange.getStart().isBefore(dateRange.getEnd());

        return startDateOk && endDateOk;
    }
}
