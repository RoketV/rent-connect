package ru.practicum.booking.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StartAndEndValidator.class)
@ReportAsSingleViolation
public @interface StartAndEndValidation {

    String message() default "Start must be before end";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

