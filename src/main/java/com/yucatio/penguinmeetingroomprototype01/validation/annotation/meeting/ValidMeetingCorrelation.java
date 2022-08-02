package com.yucatio.penguinmeetingroomprototype01.validation.annotation.meeting;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.yucatio.penguinmeetingroomprototype01.validation.meeting.ValidMeetingCorrelationValidator;

@Documented
@Constraint(validatedBy = { ValidMeetingCorrelationValidator.class })
@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidMeetingCorrelation {
  String message() default "{com.yucatio.penguinmeetingroomprototype01.validation.ValidMeetingCorrelation.message}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  @Target({ElementType.TYPE})
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  public @interface List {
    ValidMeetingCorrelation[] value();
  }
}
