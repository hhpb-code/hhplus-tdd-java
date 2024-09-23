package io.hhplus.tdd.point.exception;

import io.hhplus.tdd.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PointErrorCode implements ErrorCode {

  INVALID_USER_ID(HttpStatus.BAD_REQUEST, "유효하지 않은 사용자 ID입니다."),
  INVALID_AMOUNT(HttpStatus.BAD_REQUEST, "유효하지 않은 금액입니다."),
  INVALID_UPDATE_MILLIS(HttpStatus.BAD_REQUEST, "유효하지 않은 업데이트 시간입니다."),
  ;

  private final HttpStatus status;
  private final String message;

  @Override
  public String getCode() {
    return name();
  }

  @Override
  public HttpStatus getStatus() {
    return status;
  }

  @Override
  public String getMessage() {
    return message;
  }
}
