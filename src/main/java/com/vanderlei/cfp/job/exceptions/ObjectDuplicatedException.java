package com.vanderlei.cfp.job.exceptions;

public class ObjectDuplicatedException extends RuntimeException {

  public ObjectDuplicatedException(String msg) {
    super(msg);
  }

  public ObjectDuplicatedException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
