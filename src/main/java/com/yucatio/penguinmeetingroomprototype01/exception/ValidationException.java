package com.yucatio.penguinmeetingroomprototype01.exception;

import java.util.Set;

import javax.validation.ConstraintViolation;

public class ValidationException extends RuntimeException {

  private final Set<? extends ConstraintViolation<? extends Object>> errors;

  public ValidationException(Set<? extends ConstraintViolation<? extends Object>> errors) {
    super();
    this.errors = errors;
  }

  public ValidationException(String message, Throwable cause,
      Set<? extends ConstraintViolation<? extends Object>> errors) {
    super(message, cause);
    this.errors = errors;
  }

  public ValidationException(String message, Set<? extends ConstraintViolation<? extends Object>> errors) {
    super(message);
    this.errors = errors;
  }

  public ValidationException(Throwable cause, Set<? extends ConstraintViolation<? extends Object>> errors) {
    super(cause);
    this.errors = errors;
  }

  public Set<? extends ConstraintViolation<? extends Object>> getErrors() {
    return errors;
  }

}
