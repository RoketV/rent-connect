package ru.practicum.booking.annotation;

import ru.practicum.booking.dto.BookingRequestDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class StartAndEndValidator implements ConstraintValidator<StartAndEndValidation, BookingRequestDto> {

    @Override
    public boolean isValid(BookingRequestDto value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        LocalDateTime start = value.getStart();
        LocalDateTime end = value.getEnd();

        return start == null || end == null || start.isBefore(end);
    }
}
