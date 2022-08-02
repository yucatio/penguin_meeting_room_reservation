package com.yucatio.penguinmeetingroomprototype01.repository;

import java.util.Date;
import java.util.Optional;

import com.yucatio.penguinmeetingroomprototype01.entity.Meeting;

public interface MeetingRepository {
  Optional<Meeting> selectOneById(Integer id);

  Boolean hasOverlap(Integer meetingRoom, Date start, Date end, Integer id);

  Integer insert(Meeting meeting);

  void update(Meeting meeting);
}
