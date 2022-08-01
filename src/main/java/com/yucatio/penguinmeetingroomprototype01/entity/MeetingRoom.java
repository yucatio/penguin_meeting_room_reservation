package com.yucatio.penguinmeetingroomprototype01.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MeetingRoom {
  private Integer id;

  private String name;

  private Integer capacity;
}
