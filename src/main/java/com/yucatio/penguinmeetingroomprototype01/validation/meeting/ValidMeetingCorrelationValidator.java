package com.yucatio.penguinmeetingroomprototype01.validation.meeting;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.yucatio.penguinmeetingroomprototype01.constant.BusinessSpec;
import com.yucatio.penguinmeetingroomprototype01.entity.Meeting;
import com.yucatio.penguinmeetingroomprototype01.validation.annotation.meeting.ValidMeetingCorrelation;

public class ValidMeetingCorrelationValidator implements ConstraintValidator<ValidMeetingCorrelation, Meeting> {

  @Override
  public boolean isValid(Meeting meeting, ConstraintValidatorContext context) {
    context.disableDefaultConstraintViolation();

    List<Boolean> result = List.of(
        validateStartAndEnd(meeting, context));

    return result.stream().allMatch(Boolean::valueOf);
  }

  protected boolean validateStartAndEnd(Meeting meeting, ConstraintValidatorContext context) {
    if (meeting.getStart().compareTo(meeting.getEnd()) >= 0) {
      context
          .buildConstraintViolationWithTemplate(
              "{com.yucatio.penguinmeetingroomprototype01.validation.ValidMeetingCorrelation.endBeforeStart.message}")
          .addPropertyNode("start").addConstraintViolation();
      return false;
    } else if (meeting.getEnd().getTime() - meeting.getStart().getTime() < BusinessSpec.Meeting.MIN_MILLISEC) {
      context
          .buildConstraintViolationWithTemplate(
              "{com.yucatio.penguinmeetingroomprototype01.validation.ValidMeetingCorrelation.minMeetingTime.message}")
          .addPropertyNode("start").addConstraintViolation();

      return false;
    } else if (meeting.getEnd().getTime() - meeting.getStart().getTime() > BusinessSpec.Meeting.MAX_MILLISEC) {
      context
          .buildConstraintViolationWithTemplate(
              "{com.yucatio.penguinmeetingroomprototype01.validation.ValidMeetingCorrelation.maxMeetingTime.message}")
          .addPropertyNode("start").addConstraintViolation();

      return false;
    }
    return true;
  }

}
