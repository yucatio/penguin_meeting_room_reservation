package com.yucatio.penguinmeetingroomprototype01.entity.reqres;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> {
  private String resultCode;

  private List<ErrorMessage> errors;
  
  private Integer newId;

  private T result;
}
