package io.hhplus.tdd.error;

import org.springframework.http.HttpStatus;

public interface ErrorCode {

  String getCode();

  HttpStatus getStatus();

  String getMessage();
}