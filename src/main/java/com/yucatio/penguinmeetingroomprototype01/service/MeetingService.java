package com.yucatio.penguinmeetingroomprototype01.service;

import com.yucatio.penguinmeetingroomprototype01.entity.Meeting;

public interface MeetingService {
  Meeting selectOneById(Integer id);
  
  Integer insert(Meeting meeting);
  
  void update(Integer id, Meeting meeting);
}
