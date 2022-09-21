package com.yucatio.penguinmeetingroomprototype01.repository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.yucatio.penguinmeetingroomprototype01.entity.Meeting;

@Repository
public class MeetingRepositoryStubImpl implements MeetingRepository {
  private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

  private static final Map<Integer, Meeting> meetings = new ConcurrentHashMap<>();

  private static int counter = 0;

  static {
    SimpleDateFormat format = new SimpleDateFormat(DATETIME_FORMAT);
    try {
      meetings.put(1,
          new Meeting(1, "event_1", 1, 3, format.parse("2022-02-14 10:00:00"), format.parse("2022-02-14 10:30:00")));
      meetings.put(2,
          new Meeting(2, "event_2", 1, 5, format.parse("2022-02-16 11:00:00"), format.parse("2022-02-16 11:30:00")));
      meetings.put(3,
          new Meeting(3, "event_3", 1, 8, format.parse("2022-02-16 12:00:00"), format.parse("2022-02-16 12:30:00")));
      counter = meetings.size() + 1;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public Optional<Meeting> selectOneById(Integer id) {
    return Optional.ofNullable(meetings.get(id));
  }

  @Override
  public Boolean hasOverlap(Integer meetingRoom, Date start, Date end, Integer id) {
    return meetings.values().stream()
        .filter(db -> db.getMeetingRoom().equals(meetingRoom))
        .filter(db -> !db.getId().equals(id))
        .anyMatch(db -> db.getEnd().compareTo(start) > 0 && db.getStart().compareTo(end) < 0);
  }

  @Override
  public Integer insert(Meeting meeting) {
    int id = counter++;

    meeting.setId(id);
    meetings.put(id, meeting);

    return id;
  }

  @Override
  public void update(Meeting meeting) {
    meetings.put(meeting.getId(), meeting);

  }
}
