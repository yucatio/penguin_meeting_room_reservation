package com.yucatio.penguinmeetingroomprototype01.repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.yucatio.penguinmeetingroomprototype01.entity.MeetingRoom;

@Repository
public class MeetingRoomStubImpl implements MeetingRoomRepository {
  private static final Map<Integer, MeetingRoom> meetingRooms = new ConcurrentHashMap<>();

  static {
    meetingRooms.put(1, new MeetingRoom(1, "001", 5));
    meetingRooms.put(2, new MeetingRoom(2, "002", 10));
    meetingRooms.put(3, new MeetingRoom(3, "003", 50));
  }

  @Override
  public Optional<MeetingRoom> selectOneById(Integer id) {
    return Optional.ofNullable(meetingRooms.get(id));
  }

}
