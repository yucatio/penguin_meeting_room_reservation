package com.yucatio.penguinmeetingroomprototype01.controller.advice;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.yucatio.penguinmeetingroomprototype01.entity.reqres.ErrorMessage;
import com.yucatio.penguinmeetingroomprototype01.entity.reqres.Response;
import com.yucatio.penguinmeetingroomprototype01.exception.MeetingNotFoundException;
import com.yucatio.penguinmeetingroomprototype01.exception.ValidationException;

@RestControllerAdvice
public class ControllerAdvice {

  @ExceptionHandler({ ValidationException.class })
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Response<Object> handleValidationException(ValidationException e) {
    Response<Object> response = new Response<>();
    response.setResultCode("C001");

    List<ErrorMessage> errors = e.getErrors().stream()
        .map(violation -> new ErrorMessage(violation.getPropertyPath().toString(), violation.getMessage()))
        .collect(Collectors.toList());

    response.setErrors(errors);

    return response;
  }

  @ExceptionHandler({ MeetingNotFoundException.class })
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public Response<Object> handleMeetingNotFoundException(MeetingNotFoundException e) {
    Response<Object> response = new Response<>();
    response.setResultCode("C004");

    List<ErrorMessage> errors = new ArrayList<>();
    errors.add(new ErrorMessage("id", "not found"));

    response.setErrors(errors);

    return response;
  }
}
