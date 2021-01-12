package com.vdavid;

class InvalidDataException extends RuntimeException {
  public InvalidDataException(String errorMessage) {
    super(errorMessage);
  }
}
