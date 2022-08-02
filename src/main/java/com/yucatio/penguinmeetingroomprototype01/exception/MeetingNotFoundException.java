package com.yucatio.penguinmeetingroomprototype01.exception;

public class MeetingNotFoundException extends RuntimeException {

  public MeetingNotFoundException() {
    super();
  }

  public MeetingNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public MeetingNotFoundException(String message) {
    super(message);
  }

  public MeetingNotFoundException(Throwable cause) {
    super(cause);
  }

}
