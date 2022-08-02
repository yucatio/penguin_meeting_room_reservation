package com.yucatio.penguinmeetingroomprototype01.entity.reqres;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorMessage {
  private String fieldId;
  private String message;
}
