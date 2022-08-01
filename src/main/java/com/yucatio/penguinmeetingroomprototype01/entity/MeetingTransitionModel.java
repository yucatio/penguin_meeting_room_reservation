package com.yucatio.penguinmeetingroomprototype01.entity;

import javax.validation.Valid;

import com.yucatio.penguinmeetingroomprototype01.validation.annotation.meeting.ValidMeetingWithDbModel;
import com.yucatio.penguinmeetingroomprototype01.validation.annotation.meeting.ValidMeetingNonFunctional;
import com.yucatio.penguinmeetingroomprototype01.validation.group.WithDBModel;
import com.yucatio.penguinmeetingroomprototype01.validation.group.NonFunctional;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@ValidMeetingWithDbModel(groups = WithDBModel.class)
@ValidMeetingNonFunctional(groups = NonFunctional.class)
public class MeetingTransitionModel {
  @Valid
  private Meeting mergedModel;
  private Meeting dbModel;
  private Meeting inputModel;
}
