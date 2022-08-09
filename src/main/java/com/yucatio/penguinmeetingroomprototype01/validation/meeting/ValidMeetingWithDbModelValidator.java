package com.yucatio.penguinmeetingroomprototype01.validation.meeting;

import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.yucatio.penguinmeetingroomprototype01.entity.MeetingTransitionModel;
import com.yucatio.penguinmeetingroomprototype01.validation.annotation.meeting.ValidMeetingWithDbModel;

public class ValidMeetingWithDbModelValidator
    implements ConstraintValidator<ValidMeetingWithDbModel, MeetingTransitionModel> {

  @Override
  public boolean isValid(MeetingTransitionModel model, ConstraintValidatorContext context) {
    context.disableDefaultConstraintViolation();

    boolean isValid = true;

    isValid &= validateStartTime(model, context);
    isValid &= validateEndTIme(model, context);

    return isValid;
  }

  protected boolean validateStartTime(MeetingTransitionModel model, ConstraintValidatorContext context) {
    if (model.getDbModel() == null) {
      // insertは対象外
      return true;
    }

    if (model.getDbModel().getStart().compareTo(model.getMergedModel().getStart()) == 0) {
      // startTimeに変更がないなら問題なし
      return true;
    }

    boolean isValid = true;

    if (model.getMergedModel().getStart().compareTo(new Date()) < 0) {
      // 過去
      context
          .buildConstraintViolationWithTemplate("{javax.validation.constraints.Future.message}")
          .addPropertyNode("start").addConstraintViolation();
      isValid = false;
    }

    if (model.getDbModel().getStart().compareTo(new Date()) < 0) {
      // 開始時間を過ぎていたら開始時間を変更できない
      context
          .buildConstraintViolationWithTemplate(
              "{com.yucatio.penguinmeetingroomprototype01.validation.ValidMeetingWithDbModel.notChangeStartAfterStart.message}")
          .addPropertyNode("start").addConstraintViolation();

      isValid = false;
    }

    return isValid;
  }

  protected boolean validateEndTIme(MeetingTransitionModel model, ConstraintValidatorContext context) {
    if (model.getDbModel() == null) {
      // insertは対象外
      return true;
    }

    if (model.getDbModel().getEnd().compareTo(model.getMergedModel().getEnd()) == 0) {
      // endTimeに変更がないなら問題なし
      return true;
    }

    boolean isValid = true;

    if (model.getMergedModel().getEnd().compareTo(new Date()) < 0) {
      // 過去
      context
          .buildConstraintViolationWithTemplate("{javax.validation.constraints.Future.message}")
          .addPropertyNode("end").addConstraintViolation();
      isValid = false;
    }

    if (model.getDbModel().getEnd().compareTo(new Date()) < 0) {
      // 終了時間を過ぎていたら終了時間を変更できない
      context
          .buildConstraintViolationWithTemplate(
              "{com.yucatio.penguinmeetingroomprototype01.validation.ValidMeetingWithDbModel.notChangeEndAfterEnd.message}")
          .addPropertyNode("start").addConstraintViolation();
      isValid = false;
    }

    return isValid;
  }
}
