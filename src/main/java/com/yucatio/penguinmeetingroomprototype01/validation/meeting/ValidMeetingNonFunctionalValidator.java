package com.yucatio.penguinmeetingroomprototype01.validation.meeting;

import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.yucatio.penguinmeetingroomprototype01.entity.MeetingRoom;
import com.yucatio.penguinmeetingroomprototype01.entity.MeetingTransitionModel;
import com.yucatio.penguinmeetingroomprototype01.repository.MeetingRepository;
import com.yucatio.penguinmeetingroomprototype01.repository.MeetingRoomRepository;
import com.yucatio.penguinmeetingroomprototype01.validation.annotation.meeting.ValidMeetingNonFunctional;

public class ValidMeetingNonFunctionalValidator
    implements ConstraintValidator<ValidMeetingNonFunctional, MeetingTransitionModel> {

  MeetingRepository meetingRepository;

  MeetingRoomRepository meetingRoomRepository;

  @Autowired
  public ValidMeetingNonFunctionalValidator(MeetingRepository meetingRepository,
      MeetingRoomRepository meetingRoomRepository) {
    super();
    this.meetingRepository = meetingRepository;
    this.meetingRoomRepository = meetingRoomRepository;
  }

  @Override
  public boolean isValid(MeetingTransitionModel model, ConstraintValidatorContext context) {
    context.disableDefaultConstraintViolation();

    List<Boolean> result = List.of(
        validMeetingRoomAndAtendee(model, context),
        validateOverlap(model, context));

    return result.stream().allMatch(Boolean::valueOf);
  }

  protected boolean validMeetingRoomAndAtendee(MeetingTransitionModel model, ConstraintValidatorContext context) {
    if (model.getDbModel() != null
        && model.getDbModel().getMeetingRoom().equals(model.getMergedModel().getMeetingRoom())
        && model.getDbModel().getNumAtendee().equals(model.getMergedModel().getNumAtendee())) {
      // meetingRoomとnumAtendeeに変更なければスキップ
      return true;
    }

    Optional<MeetingRoom> meetingRoom = meetingRoomRepository.selectOneById(model.getMergedModel().getMeetingRoom());

    if (meetingRoom.isEmpty()) {
      // no meeting room
      context
          .buildConstraintViolationWithTemplate(
              "{com.yucatio.penguinmeetingroomprototype01.validation.ValidMeetingNonFunctional.meetingRoomNotExist.message}")
          .addPropertyNode("meetingRoom").addConstraintViolation();

      return false;
    }

    return validateNumAtendee(model, meetingRoom.get(), context);
  }

  protected boolean validateNumAtendee(MeetingTransitionModel model, MeetingRoom meetingRoom,
      ConstraintValidatorContext context) {
    // new or meetingRoom or numAtendee changed
    if (meetingRoom.getCapacity() < model.getMergedModel().getNumAtendee()) {
      context
          .buildConstraintViolationWithTemplate(
              "{com.yucatio.penguinmeetingroomprototype01.validation.ValidMeetingNonFunctional.numAtendeeExceedsCapacity.message}")
          .addPropertyNode("numAtendee").addConstraintViolation();

      return false;
    }

    return true;
  }

  protected boolean validateOverlap(MeetingTransitionModel model, ConstraintValidatorContext context) {
    // 新規登録もしく会議室または開始時間、終了時間が変更されたとき
    if (model.getDbModel() == null ||
        !model.getDbModel().getMeetingRoom().equals(model.getMergedModel().getMeetingRoom()) ||
        model.getDbModel().getStart().compareTo(model.getMergedModel().getStart()) != 0 ||
        model.getDbModel().getEnd().compareTo(model.getMergedModel().getEnd()) != 0) {

      if (meetingRepository.hasOverlap(model.getMergedModel().getMeetingRoom(), model.getMergedModel().getStart(),
          model.getMergedModel().getEnd(), model.getMergedModel().getId())) {
        // 会議が重複している
        context
            .buildConstraintViolationWithTemplate(
                "{com.yucatio.penguinmeetingroomprototype01.validation.ValidMeetingNonFunctional.meetingOverlap.message}")
            .addPropertyNode("start").addConstraintViolation();

        return false;
      }
    }

    return true;
  }

}
