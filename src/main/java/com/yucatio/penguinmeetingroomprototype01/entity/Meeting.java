package com.yucatio.penguinmeetingroomprototype01.entity;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;

import com.yucatio.penguinmeetingroomprototype01.validation.annotation.meeting.ValidMeetingCorrelation;
import com.yucatio.penguinmeetingroomprototype01.validation.group.FieldCorrelation;
import com.yucatio.penguinmeetingroomprototype01.validation.group.InsertOnly;
import com.yucatio.penguinmeetingroomprototype01.validation.group.Single;
import com.yucatio.penguinmeetingroomprototype01.validation.group.UpdateOnly;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ValidMeetingCorrelation(groups = FieldCorrelation.class)
public class Meeting implements Serializable {
  @Null(groups = InsertOnly.class)
  @NotNull(groups = UpdateOnly.class)
  private Integer id;

  @NotNull(groups = InsertOnly.class)
  @Size(min = 3, max = 256, groups = Single.class)
  private String subject;

  @NotNull(groups = InsertOnly.class)
  @Min(value = 1, groups = Single.class)
  private Integer meetingRoom;

  @NotNull(groups = InsertOnly.class)
  @Range(min = 1, max = 50, groups = Single.class)
  private Integer numAtendee;

  @NotNull(groups = InsertOnly.class)
  @Future(groups = InsertOnly.class)
  private Date start;

  @NotNull(groups = InsertOnly.class)
  @Future(groups = InsertOnly.class)
  private Date end;
}
