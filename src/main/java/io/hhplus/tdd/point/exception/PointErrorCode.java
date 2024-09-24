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
  USER_POINT_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자 포인트를 찾을 수 없습니다."),
  POINT_HISTORY_ID_INVALID(HttpStatus.BAD_REQUEST, "유효하지 않은 포인트 이력 ID입니다."),
  INVALID_TRANSACTION_TYPE(HttpStatus.BAD_REQUEST, "유효하지 않은 거래 유형입니다."),
  POINT_NOT_ENOUGH(HttpStatus.BAD_REQUEST, "포인트가 부족합니다."),
  EXCEED_MAX_POINT(HttpStatus.BAD_REQUEST, "포인트가 최대치를 초과했습니다."),
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
