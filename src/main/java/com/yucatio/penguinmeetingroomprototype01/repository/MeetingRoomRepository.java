package com.yucatio.penguinmeetingroomprototype01.repository;

import java.util.Optional;

import com.yucatio.penguinmeetingroomprototype01.entity.MeetingRoom;

public interface MeetingRoomRepository {
  Optional<MeetingRoom> selectOneById(Integer id);
}
