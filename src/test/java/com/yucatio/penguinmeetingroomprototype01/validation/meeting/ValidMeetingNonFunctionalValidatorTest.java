package com.yucatio.penguinmeetingroomprototype01.validation.meeting;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.Optional;

import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.yucatio.penguinmeetingroomprototype01.entity.Meeting;
import com.yucatio.penguinmeetingroomprototype01.entity.MeetingRoom;
import com.yucatio.penguinmeetingroomprototype01.entity.MeetingTransitionModel;
import com.yucatio.penguinmeetingroomprototype01.repository.MeetingRepository;
import com.yucatio.penguinmeetingroomprototype01.repository.MeetingRoomRepository;

class ValidMeetingNonFunctionalValidatorTest {
  ValidMeetingNonFunctionalValidator target;

  MeetingRepository meetingRepository = mock(MeetingRepository.class);
  MeetingRoomRepository meetingRoomRepository = mock(MeetingRoomRepository.class);

  ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
  ConstraintViolationBuilder builder = mock(ConstraintViolationBuilder.class);
  NodeBuilderCustomizableContext builderContext = mock(NodeBuilderCustomizableContext.class);

  @BeforeEach
  void setUp() throws Exception {
    target = new ValidMeetingNonFunctionalValidator(meetingRepository, meetingRoomRepository);
    when(context.buildConstraintViolationWithTemplate(anyString()))
        .thenReturn(builder);
    when(builder.addPropertyNode(anyString())).thenReturn(builderContext);
  }

  @Test
  void test_isValid_valid() {
    MeetingRoom meetingRoom = new MeetingRoom(1, "会議室101", 10);
    when(meetingRoomRepository.selectOneById(any())).thenReturn(Optional.of(meetingRoom));
    when(meetingRepository.hasOverlap(anyInt(), any(), any(), anyInt())).thenReturn(false);

    OffsetDateTime dbStart = OffsetDateTime.parse("2032-09-12T09:00:00+09:00");
    OffsetDateTime dbEnd = OffsetDateTime.parse("2032-09-12T10:00:00+09:00");
    OffsetDateTime inStart = OffsetDateTime.parse("2032-09-12T11:00:00+09:00");
    OffsetDateTime inEnd = OffsetDateTime.parse("2032-09-12T12:00:00+09:00");

    Meeting db = new Meeting(1, "会議01", 1, 3, Date.from(dbStart.toInstant()), Date.from(dbEnd.toInstant()));
    Meeting input = new Meeting(1, "会議01", 2, 5, Date.from(inStart.toInstant()), Date.from(inEnd.toInstant()));
    Meeting merged = new Meeting(1, "会議01", 2, 5, Date.from(inStart.toInstant()), Date.from(inEnd.toInstant()));

    MeetingTransitionModel tModel = new MeetingTransitionModel(merged, db, input);

    boolean actual = target.isValid(tModel, context);

    assertEquals(true, actual);
  }

  @Test
  void test_isValid_invalid() {
    MeetingRoom meetingRoom = new MeetingRoom(1, "会議室101", 10);
    when(meetingRoomRepository.selectOneById(any())).thenReturn(Optional.of(meetingRoom));
    when(meetingRepository.hasOverlap(anyInt(), any(), any(), anyInt())).thenReturn(true);

    OffsetDateTime dbStart = OffsetDateTime.parse("2032-09-12T09:00:00+09:00");
    OffsetDateTime dbEnd = OffsetDateTime.parse("2032-09-12T10:00:00+09:00");
    OffsetDateTime inStart = OffsetDateTime.parse("2032-09-12T11:00:00+09:00");
    OffsetDateTime inEnd = OffsetDateTime.parse("2032-09-12T12:00:00+09:00");

    Meeting db = new Meeting(1, "会議01", 1, 3, Date.from(dbStart.toInstant()), Date.from(dbEnd.toInstant()));
    Meeting input = new Meeting(1, "会議01", 2, 5, Date.from(inStart.toInstant()), Date.from(inEnd.toInstant()));
    Meeting merged = new Meeting(1, "会議01", 2, 5, Date.from(inStart.toInstant()), Date.from(inEnd.toInstant()));

    MeetingTransitionModel tModel = new MeetingTransitionModel(merged, db, input);

    boolean actual = target.isValid(tModel, context);

    assertEquals(false, actual);

  }

  @Test
  void test_validMeetingRoomAndAtendee_meeting_room_db_null_valid() {
    MeetingRoom meetingRoom = new MeetingRoom(1, "会議室101", 10);
    when(meetingRoomRepository.selectOneById(any())).thenReturn(Optional.of(meetingRoom));

    OffsetDateTime start = OffsetDateTime.parse("2032-09-12T09:00:00+09:00");
    OffsetDateTime end = OffsetDateTime.parse("2032-09-12T10:00:00+09:00");

    Meeting input = new Meeting(1, "会議01", 1, 3, Date.from(start.toInstant()), Date.from(end.toInstant()));

    MeetingTransitionModel tModel = new MeetingTransitionModel(input, null, input);

    boolean actual = target.validMeetingRoomAndAtendee(tModel, context);

    assertEquals(true, actual);
  }

  @Test
  void test_validMeetingRoomAndAtendee_meeting_room_db_null_invalid() {
    when(meetingRoomRepository.selectOneById(any())).thenReturn(Optional.empty());

    OffsetDateTime start = OffsetDateTime.parse("2032-09-12T09:00:00+09:00");
    OffsetDateTime end = OffsetDateTime.parse("2032-09-12T10:00:00+09:00");

    Meeting input = new Meeting(1, "会議01", 1, 3, Date.from(start.toInstant()), Date.from(end.toInstant()));

    MeetingTransitionModel tModel = new MeetingTransitionModel(input, null, input);

    boolean actual = target.validMeetingRoomAndAtendee(tModel, context);

    assertEquals(false, actual);
  }

  @ParameterizedTest
  @CsvSource({
      // not changed
      "10, 3, 10, 3, true",
      // meeting room changed.
      "10, 3, 11, 3, false",
      // numAtendee changed,
      "10, 3, 10, 5, false",
  })
  void test_validMeetingRoomAndAtendee_meeting_room_not_exists(int dbMeetingRoom, int dbNumAtendee,
      int inMeetingRoom, int inNumAtendee, boolean expected) {
    when(meetingRoomRepository.selectOneById(any())).thenReturn(Optional.empty());

    OffsetDateTime start = OffsetDateTime.parse("2032-09-12T09:00:00+09:00");
    OffsetDateTime end = OffsetDateTime.parse("2032-09-12T10:00:00+09:00");

    Meeting db = new Meeting(1, "会議01", dbMeetingRoom, dbNumAtendee, Date.from(start.toInstant()),
        Date.from(end.toInstant()));
    Meeting input = new Meeting(1, "会議01", inMeetingRoom, inNumAtendee, Date.from(start.toInstant()),
        Date.from(end.toInstant()));
    Meeting merged = new Meeting(1, "会議01", inMeetingRoom, inNumAtendee, Date.from(start.toInstant()),
        Date.from(end.toInstant()));

    MeetingTransitionModel tModel = new MeetingTransitionModel(merged, db, input);

    boolean actual = target.validMeetingRoomAndAtendee(tModel, context);

    assertEquals(expected, actual);
  }

  @ParameterizedTest
  @CsvSource({
      // not changed
      "10, 3, 10, 3, 5, true",
      // meeting room changed. Capacity OK
      "10, 3, 11, 3, 5, true",
      // meeting room changed, Capacity NG
      "10, 3, 11, 3, 2, false",
      // numAtendee changed, Capacity OK
      "10, 3, 10, 5, 5, true",
      // numAtendee changed. Capacity NG
      "10, 3, 10, 6, 5, false",
  })
  void test_validMeetingRoomAndAtendee_meeting_room_exists(int dbMeetingRoom, int dbNumAtendee,
      int inMeetingRoom, int inNumAtendee, int capacity, boolean expected) {
    MeetingRoom meetingRoom = new MeetingRoom(1, "会議室101", capacity);
    when(meetingRoomRepository.selectOneById(any())).thenReturn(Optional.of(meetingRoom));

    OffsetDateTime start = OffsetDateTime.parse("2032-09-12T09:00:00+09:00");
    OffsetDateTime end = OffsetDateTime.parse("2032-09-12T10:00:00+09:00");

    Meeting db = new Meeting(1, "会議01", dbMeetingRoom, dbNumAtendee, Date.from(start.toInstant()),
        Date.from(end.toInstant()));
    Meeting input = new Meeting(1, "会議01", inMeetingRoom, inNumAtendee, Date.from(start.toInstant()),
        Date.from(end.toInstant()));
    Meeting merged = new Meeting(1, "会議01", inMeetingRoom, inNumAtendee, Date.from(start.toInstant()),
        Date.from(end.toInstant()));

    MeetingTransitionModel tModel = new MeetingTransitionModel(merged, db, input);

    boolean actual = target.validMeetingRoomAndAtendee(tModel, context);

    assertEquals(expected, actual);
  }

  @ParameterizedTest
  @CsvSource({
      "3, 3, true",
      "3, 4, false",
  })
  void test_validateNumAtendee(int capacity, int numAtendee, boolean expected) {
    MeetingRoom meetingRoom = new MeetingRoom(1, "会議室101", capacity);

    OffsetDateTime start = OffsetDateTime.parse("2032-09-12T09:00:00+09:00");
    OffsetDateTime end = OffsetDateTime.parse("2032-09-12T10:00:00+09:00");

    Meeting db = new Meeting(1, "会議01", 1, numAtendee, Date.from(start.toInstant()), Date.from(end.toInstant()));
    Meeting input = new Meeting(1, "会議01", 1, numAtendee, Date.from(start.toInstant()), Date.from(end.toInstant()));
    Meeting merged = new Meeting(1, "会議01", 1, numAtendee, Date.from(start.toInstant()), Date.from(end.toInstant()));

    MeetingTransitionModel tModel = new MeetingTransitionModel(merged, db, input);

    boolean actual = target.validateNumAtendee(tModel, meetingRoom, context);

    assertEquals(expected, actual);

  }

  @Test
  void test_validateOverlap_same_db_input() {
    OffsetDateTime start = OffsetDateTime.parse("2032-09-12T09:00:00+09:00");
    OffsetDateTime end = OffsetDateTime.parse("2032-09-12T10:00:00+09:00");

    Meeting db = new Meeting(1, "会議01", 1, 3, Date.from(start.toInstant()), Date.from(end.toInstant()));
    Meeting input = new Meeting(1, "会議01", 1, 3, Date.from(start.toInstant()), Date.from(end.toInstant()));
    Meeting merged = new Meeting(1, "会議01", 1, 3, Date.from(start.toInstant()), Date.from(end.toInstant()));

    MeetingTransitionModel tModel = new MeetingTransitionModel(merged, db, input);

    boolean actual = target.validateOverlap(tModel, context);

    assertEquals(true, actual);
  }

  @ParameterizedTest
  @CsvSource({
      "true, false",
      "false, true",
  })
  void test_validateOverlap_db_null(boolean overlap, boolean expected) {
    when(meetingRepository.hasOverlap(anyInt(), any(), any(), anyInt())).thenReturn(overlap);

    OffsetDateTime start = OffsetDateTime.parse("2032-09-12T09:00:00+09:00");
    OffsetDateTime end = OffsetDateTime.parse("2032-09-12T10:00:00+09:00");

    Meeting input = new Meeting(1, "会議01", 1, 3, Date.from(start.toInstant()), Date.from(end.toInstant()));

    MeetingTransitionModel tModel = new MeetingTransitionModel(input, null, input);

    boolean actual = target.validateOverlap(tModel, context);

    assertEquals(expected, actual);
  }

  @ParameterizedTest
  @CsvSource({
      "1, 1, false, true",
      "1, 3, true, false",
      "1, 3, false, true",
  })
  void test_validateOverlap_meeting_room(int dbMeeetingRoom, int inputMeetingRoom, boolean overlap, boolean expected) {
    when(meetingRepository.hasOverlap(anyInt(), any(), any(), anyInt())).thenReturn(overlap);

    OffsetDateTime start = OffsetDateTime.parse("2032-09-12T09:00:00+09:00");
    OffsetDateTime end = OffsetDateTime.parse("2032-09-12T10:00:00+09:00");

    Meeting db = new Meeting(1, "会議01", dbMeeetingRoom, 3, Date.from(start.toInstant()), Date.from(end.toInstant()));
    Meeting input = new Meeting(1, "会議01", inputMeetingRoom, 3, Date.from(start.toInstant()),
        Date.from(end.toInstant()));
    Meeting merged = new Meeting(1, "会議01", inputMeetingRoom, 3, Date.from(start.toInstant()),
        Date.from(end.toInstant()));

    MeetingTransitionModel tModel = new MeetingTransitionModel(merged, db, input);

    boolean actual = target.validateOverlap(tModel, context);

    assertEquals(expected, actual);
  }

  @ParameterizedTest
  @CsvSource({
      // start end not changed
      "2022-09-12T09:00:00+09:00, 2022-09-12T10:00:00+09:00, 2022-09-12T09:00:00+09:00, 2022-09-12T10:00:00+09:00, false, true",
      // start changed and has overlap
      "2022-09-12T09:00:00+09:00, 2022-09-12T10:00:00+09:00, 2022-09-12T09:30:00+09:00, 2022-09-12T10:00:00+09:00, true, false",
      // start changed and not have overlap
      "2022-09-12T09:00:00+09:00, 2022-09-12T10:00:00+09:00, 2022-09-12T09:30:00+09:00, 2022-09-12T10:00:00+09:00, false, true",
      // end changed and has overlap
      "2022-09-12T09:00:00+09:00, 2022-09-12T10:00:00+09:00, 2022-09-12T09:00:00+09:00, 2022-09-12T10:30:00+09:00, true, false",
      // end changed and not have overlap
      "2022-09-12T09:00:00+09:00, 2022-09-12T10:00:00+09:00, 2022-09-12T09:00:00+09:00, 2022-09-12T10:30:00+09:00, false, true",
  })
  void test_validateOverlap_start_end(OffsetDateTime dbStart, OffsetDateTime dbEnd,
      OffsetDateTime inStart, OffsetDateTime inEnd, boolean overlap, boolean expected) {
    when(meetingRepository.hasOverlap(anyInt(), any(), any(), anyInt())).thenReturn(overlap);

    Meeting db = new Meeting(1, "会議01", 1, 3, Date.from(dbStart.toInstant()), Date.from(dbEnd.toInstant()));
    Meeting input = new Meeting(1, "会議01", 1, 3, Date.from(inStart.toInstant()),
        Date.from(inEnd.toInstant()));
    Meeting merged = new Meeting(1, "会議01", 1, 3, Date.from(inStart.toInstant()),
        Date.from(inEnd.toInstant()));

    MeetingTransitionModel tModel = new MeetingTransitionModel(merged, db, input);

    boolean actual = target.validateOverlap(tModel, context);

    assertEquals(expected, actual);
  }
}
