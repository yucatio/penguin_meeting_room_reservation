package com.yucatio.penguinmeetingroomprototype01.constant;

public class BusinessSpec {
  private BusinessSpec() {
  }

  public static class Meeting {
    public static final long MIN_MILLISEC = 15 * 60 * 1000;
    public static final long MAX_MILLISEC = 2 * 60 * 60 * 1000;
  }
}
