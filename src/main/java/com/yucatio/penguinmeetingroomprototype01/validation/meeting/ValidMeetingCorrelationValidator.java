package com.yucatio.penguinmeetingroomprototype01.validation.meeting;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.yucatio.penguinmeetingroomprototype01.constant.BusinessSpec;
import com.yucatio.penguinmeetingroomprototype01.entity.Meeting;
import com.yucatio.penguinmeetingroomprototype01.validation.annotation.meeting.ValidMeetingCorrelation;

public class ValidMeetingCorrelationValidator implements ConstraintValidator<ValidMeetingCorrelation, Meeting> {

  @Override
  public boolean isValid(Meeting meeting, ConstraintValidatorContext context) {
    context.disableDefaultConstraintViolation();

    boolean isValid = true;

    if (meeting.getStart().compareTo(meeting.getEnd()) >= 0) {
      context
          .buildConstraintViolationWithTemplate(
              "{com.yucatio.penguinmeetingroomprototype01.validation.ValidMeetingCorrelation.endBeforeStart.message}")
          .addPropertyNode("start").addConstraintViolation();
      isValid = false;
    } else if (meeting.getEnd().getTime() - meeting.getStart().getTime() < BusinessSpec.Meeting.MIN_MILLISEC) {
      context
          .buildConstraintViolationWithTemplate(
              "{com.yucatio.penguinmeetingroomprototype01.validation.ValidMeetingCorrelation.minMeetingTime.message}")
          .addPropertyNode("start").addConstraintViolation();

      isValid = false;
    } else if (meeting.getEnd().getTime() - meeting.getStart().getTime() > BusinessSpec.Meeting.MAX_MILLISEC) {
      context
          .buildConstraintViolationWithTemplate(
              "{com.yucatio.penguinmeetingroomprototype01.validation.ValidMeetingCorrelation.maxMeetingTime.message}")
          .addPropertyNode("start").addConstraintViolation();

      isValid = false;
    }

    return isValid;
  }

}
