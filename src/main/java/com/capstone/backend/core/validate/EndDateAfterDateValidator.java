package com.capstone.backend.core.validate;


import com.capstone.backend.core.customAnnotation.ValidDateRange;

import com.capstone.backend.member.dto.request.ChangeScheduleRequest;
import com.capstone.backend.member.dto.request.CreateScheduleRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class EndDateAfterDateValidator implements ConstraintValidator<ValidDateRange, Object> {
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value instanceof CreateScheduleRequest request) {
            return isValidDate(request.startDate(), request.endDate(), context);
        } else if (value instanceof ChangeScheduleRequest request) {
            return isValidDate(request.startDate(), request.endDate(), context);
        }
        return true;
    }

    private boolean isValidDate(LocalDate start, LocalDate end, ConstraintValidatorContext context) {
        if (start == null || end == null) return true;
        if (end.isBefore(start)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("capstone.schedule.date.not.valid")
                    .addPropertyNode("endDate")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
