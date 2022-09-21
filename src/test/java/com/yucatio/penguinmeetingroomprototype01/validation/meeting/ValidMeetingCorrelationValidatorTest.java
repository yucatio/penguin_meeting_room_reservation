package com.yucatio.penguinmeetingroomprototype01.validation.meeting;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.OffsetDateTime;
import java.util.Date;

import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.yucatio.penguinmeetingroomprototype01.entity.Meeting;

class ValidMeetingCorrelationValidatorTest {
  ValidMeetingCorrelationValidator target;

  ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
  ConstraintViolationBuilder builder = mock(ConstraintViolationBuilder.class);
  NodeBuilderCustomizableContext builderContext = mock(NodeBuilderCustomizableContext.class);

  @BeforeEach
  public void setup() {
    target = new ValidMeetingCorrelationValidator();
    when(context.buildConstraintViolationWithTemplate(anyString()))
        .thenReturn(builder);
    when(builder.addPropertyNode(anyString())).thenReturn(builderContext);
  }

  @Test
  void test_isValid_valid() {
    OffsetDateTime start = OffsetDateTime.parse("2032-09-12T09:00:00+09:00");
    OffsetDateTime end = OffsetDateTime.parse("2032-09-12T10:00:00+09:00");
    Meeting meeting = new Meeting(1, "会議01", 1, 3, Date.from(start.toInstant()), Date.from(end.toInstant()));

    boolean actual = target.isValid(meeting, context);
    assertEquals(true, actual);
  }

  @Test
  void test_isValid_invalid() {
    OffsetDateTime start = OffsetDateTime.parse("2032-09-12T09:00:00+09:00");
    OffsetDateTime end = OffsetDateTime.parse("2032-09-12T12:00:00+09:00");
    Meeting meeting = new Meeting(1, "会議01", 1, 3, Date.from(start.toInstant()), Date.from(end.toInstant()));

    boolean actual = target.isValid(meeting, context);
    assertEquals(false, actual);
  }

  @ParameterizedTest
  @CsvSource({
      // start is after end
      "2032-09-12T09:00:00+09:00, 2032-09-12T08:59:59+09:00, false",
      // start equals to end
      "2032-09-12T09:00:00+09:00, 2032-09-12T09:00:00+09:00, false",
      // duration between start and end are less than 15 min
      "2032-09-12T09:00:00+09:00, 2032-09-12T09:14:59+09:00, false",
      // duration between start and end are equal to 15 min
      "2032-09-12T09:00:00+09:00, 2032-09-12T09:15:00+09:00, true",
      // duration between start and end are equal to 2 hour
      "2032-09-12T09:00:00+09:00, 2032-09-12T11:00:00+09:00, true",
      // duration between start and end are more than 2 hour
      "2032-09-12T09:00:00+09:00, 2032-09-12T11:00:01+09:00, false",
  })
  void test_validateStartAndEnd(OffsetDateTime start, OffsetDateTime end, boolean expected) {
    Meeting meeting = new Meeting(1, "会議01", 1, 3, Date.from(start.toInstant()), Date.from(end.toInstant()));
    boolean actual = target.validateStartAndEnd(meeting, context);
    assertEquals(expected, actual);
  }

}
