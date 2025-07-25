package com.capstone.backend.core.customAnnotation;

import com.capstone.backend.core.validate.EndDateAfterDateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = EndDateAfterDateValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDateRange {
    String message() default "capstone.schedule.date.not.valid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
