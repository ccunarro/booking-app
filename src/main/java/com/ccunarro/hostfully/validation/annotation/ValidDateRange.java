package com.ccunarro.hostfully.validation.annotation;

import com.ccunarro.hostfully.validation.ValidDateRangeConstraint;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ValidDateRangeConstraint.class})
public @interface ValidDateRange {

    String message() default "Start date should be before end date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

