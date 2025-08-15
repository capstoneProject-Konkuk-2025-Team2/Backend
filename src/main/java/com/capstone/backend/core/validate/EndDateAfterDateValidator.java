package com.capstone.backend.core.validate;


import com.capstone.backend.core.customAnnotation.ValidDateRange;

import com.capstone.backend.member.dto.request.ChangeScheduleRequest;
import com.capstone.backend.member.dto.request.CreateScheduleRequest;
import com.capstone.backend.member.dto.request.ExtracurricularField;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EndDateAfterDateValidator implements ConstraintValidator<ValidDateRange, Object> {
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value instanceof CreateScheduleRequest request) {
            return isValidDate(request.startDateTime(), request.endDateTime(), request.extracurricularId(), context, "endDate");
        } else if (value instanceof ChangeScheduleRequest request) {
            return isValidDate(request.startDateTime(), request.endDateTime(), request.extracurricularId(), context, "endDate");
        } else if (value instanceof ExtracurricularField request) {
            return isValidDate(request.applicationStart(), request.applicationEnd(), context, "applicationEnd")
                    &&
                    isValidDate(request.activityStart(), request.activityEnd(), context, "activityEnd");
        }
        return true;
    }

    private <T extends Comparable<T>> boolean isValidDate(T start, T end, Long extracurricularId, ConstraintValidatorContext context, String fieldName) {
        if ((start == null || end == null) && extracurricularId == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("capstone.schedule.normal.date.blank")
                    .addPropertyNode(fieldName)
                    .addConstraintViolation();
            return false;
        }
        if (start == null || end == null) return true;
        if (end.compareTo(start) < 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("capstone.schedule.date.not.valid")
                    .addPropertyNode(fieldName)
                    .addConstraintViolation();
            return false;
        }
        return true;
    }

    private <T extends Comparable<T>> boolean isValidDate(T start, T end, ConstraintValidatorContext context, String fieldName) {
        return isValidDate(start, end, null, context, fieldName);
    }
}
