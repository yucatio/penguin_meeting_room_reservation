package com.yucatio.penguinmeetingroomprototype01.validation.meeting;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.yucatio.penguinmeetingroomprototype01.entity.Meeting;
import com.yucatio.penguinmeetingroomprototype01.entity.MeetingTransitionModel;

class ValidMeetingWithDbModelValidatorTest extends ValidMeetingWithDbModelValidator {
  ValidMeetingWithDbModelValidator target;

  ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
  ConstraintViolationBuilder builder = mock(ConstraintViolationBuilder.class);
  NodeBuilderCustomizableContext builderContext = mock(NodeBuilderCustomizableContext.class);

  OffsetDateTime virtualNow = OffsetDateTime.parse("2022-09-12T09:00:00+09:00");

  @BeforeEach
  void setUp() throws Exception {
    Clock clock = Clock.fixed(virtualNow.toInstant(), ZoneId.systemDefault());
    target = new ValidMeetingWithDbModelValidator(clock);
    when(context.buildConstraintViolationWithTemplate(anyString()))
        .thenReturn(builder);
    when(builder.addPropertyNode(anyString())).thenReturn(builderContext);
  }

  @Test
  void test_isValid_valid() {
    OffsetDateTime dbStart = OffsetDateTime.parse("2022-09-13T10:00:00+09:00");
    OffsetDateTime dbEnd = OffsetDateTime.parse("2022-09-13T12:00:00+09:00");
    OffsetDateTime inStart = OffsetDateTime.parse("2022-09-13T13:00:00+09:00");
    OffsetDateTime inEnd = OffsetDateTime.parse("2022-09-13T14:00:00+09:00");

    Meeting db = new Meeting(1, "会議01", 1, 3, Date.from(dbStart.toInstant()), Date.from(dbEnd.toInstant()));
    Meeting input = new Meeting(1, "会議01", 1, 3, Date.from(inStart.toInstant()), Date.from(inEnd.toInstant()));
    Meeting merged = new Meeting(1, "会議01", 1, 3, Date.from(inStart.toInstant()), Date.from(inEnd.toInstant()));

    MeetingTransitionModel tModel = new MeetingTransitionModel(merged, db, input);

    boolean actual = target.validateStartTime(tModel, context);

    assertEquals(true, actual);
  }

  @Test
  void test_isValid_invalid() {
    OffsetDateTime dbStart = OffsetDateTime.parse("2022-09-11T10:00:00+09:00");
    OffsetDateTime dbEnd = OffsetDateTime.parse("2022-09-11T12:00:00+09:00");
    OffsetDateTime inStart = OffsetDateTime.parse("2022-09-11T13:00:00+09:00");
    OffsetDateTime inEnd = OffsetDateTime.parse("2022-09-11T14:00:00+09:00");

    Meeting db = new Meeting(1, "会議01", 1, 3, Date.from(dbStart.toInstant()), Date.from(dbEnd.toInstant()));
    Meeting input = new Meeting(1, "会議01", 1, 3, Date.from(inStart.toInstant()), Date.from(inEnd.toInstant()));
    Meeting merged = new Meeting(1, "会議01", 1, 3, Date.from(inStart.toInstant()), Date.from(inEnd.toInstant()));

    MeetingTransitionModel tModel = new MeetingTransitionModel(merged, db, input);

    boolean actual = target.validateStartTime(tModel, context);

    assertEquals(false, actual);
  }

  @Test
  void test_validateStartTime_db_null() {
    OffsetDateTime start = OffsetDateTime.parse("2032-09-12T09:00:00+09:00");
    OffsetDateTime end = OffsetDateTime.parse("2032-09-12T10:00:00+09:00");

    Meeting meeting = new Meeting(1, "会議01", 1, 3, Date.from(start.toInstant()), Date.from(end.toInstant()));

    MeetingTransitionModel tModel = new MeetingTransitionModel(meeting, null, meeting);

    boolean actual = target.validateStartTime(tModel, context);

    assertEquals(true, actual);
  }

  @ParameterizedTest
  @CsvSource({
      "2022-09-11T09:00:00+09:00, 2022-09-11T10:00:00+09:00, 2022-09-11T09:00:00+09:00, 2022-09-11T10:00:00+09:00, true",
      "2022-09-12T09:00:00+09:00, 2022-09-12T10:00:00+09:00, 2022-09-12T08:59:00+09:00, 2022-09-12T11:00:00+09:00, false",
      "2022-09-12T08:59:00+09:00, 2022-09-12T10:00:00+09:00, 2022-09-12T09:01:00+09:00, 2022-09-12T11:00:00+09:00, false",
      "2022-09-12T09:00:00+09:00, 2022-09-12T10:00:00+09:00, 2022-09-12T10:00:00+09:00, 2022-09-12T11:00:00+09:00, true",
  })
  void test_validateStartTime(OffsetDateTime dbStart, OffsetDateTime dbEnd,
      OffsetDateTime inStart, OffsetDateTime inEnd, boolean expected) {
    Meeting db = new Meeting(1, "会議01", 1, 3, Date.from(dbStart.toInstant()), Date.from(dbEnd.toInstant()));
    Meeting input = new Meeting(1, "会議01", 1, 3, Date.from(inStart.toInstant()), Date.from(inEnd.toInstant()));
    Meeting merged = new Meeting(1, "会議01", 1, 3, Date.from(inStart.toInstant()), Date.from(inEnd.toInstant()));

    MeetingTransitionModel tModel = new MeetingTransitionModel(merged, db, input);

    boolean actual = target.validateStartTime(tModel, context);

    assertEquals(expected, actual);
  }

  @Test
  void test_validateEndTime_db_null() {
    OffsetDateTime start = OffsetDateTime.parse("2032-09-12T09:00:00+09:00");
    OffsetDateTime end = OffsetDateTime.parse("2032-09-12T10:00:00+09:00");

    Meeting meeting = new Meeting(1, "会議01", 1, 3, Date.from(start.toInstant()), Date.from(end.toInstant()));

    MeetingTransitionModel tModel = new MeetingTransitionModel(meeting, null, meeting);

    boolean actual = target.validateEndTime(tModel, context);

    assertEquals(true, actual);
  }

  @ParameterizedTest
  @CsvSource({
      "2022-09-11T08:00:00+09:00, 2022-09-11T08:30:00+09:00, 2022-09-11T08:00:00+09:00, 2022-09-11T08:30:00+09:00, true",
      "2022-09-12T08:00:00+09:00, 2022-09-12T10:00:00+09:00, 2022-09-12T08:00:00+09:00, 2022-09-12T08:59:00+09:00, false",
      "2022-09-12T08:00:00+09:00, 2022-09-12T08:59:00+09:00, 2022-09-12T08:00:00+09:00, 2022-09-12T09:30:00+09:00, false",
      "2022-09-12T08:00:00+09:00, 2022-09-12T09:00:00+09:00, 2022-09-12T08:00:00+09:00, 2022-09-12T09:30:00+09:00, true",
  })
  void test_validateEndTime(OffsetDateTime dbStart, OffsetDateTime dbEnd,
      OffsetDateTime inStart, OffsetDateTime inEnd, boolean expected) {
    Meeting db = new Meeting(1, "会議01", 1, 3, Date.from(dbStart.toInstant()), Date.from(dbEnd.toInstant()));
    Meeting input = new Meeting(1, "会議01", 1, 3, Date.from(inStart.toInstant()), Date.from(inEnd.toInstant()));
    Meeting merged = new Meeting(1, "会議01", 1, 3, Date.from(inStart.toInstant()), Date.from(inEnd.toInstant()));

    MeetingTransitionModel tModel = new MeetingTransitionModel(merged, db, input);

    boolean actual = target.validateEndTime(tModel, context);

    assertEquals(expected, actual);

  }

}
