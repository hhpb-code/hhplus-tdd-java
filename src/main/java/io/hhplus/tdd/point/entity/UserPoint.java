package io.hhplus.tdd.point.entity;

import io.hhplus.tdd.error.BusinessException;
import io.hhplus.tdd.point.exception.PointErrorCode;

public record UserPoint(
    long id,
    long point,
    long updateMillis
) {

  public UserPoint {
    if (id < 1) {
      throw new BusinessException(PointErrorCode.INVALID_USER_ID);
    }

    if (point < 0) {
      throw new BusinessException(PointErrorCode.INVALID_AMOUNT);
    }

    if (updateMillis < 0) {
      throw new BusinessException(PointErrorCode.INVALID_UPDATE_MILLIS);
    }
  }

  public static UserPoint from(long id, long point, long updateMillis) {
    return new UserPoint(id, point, updateMillis);
  }

  public static UserPoint empty(long id) {
    return new UserPoint(id, 0, System.currentTimeMillis());
  }

  public UserPoint addPoint(Long amount) {
    return new UserPoint(id, point + amount, System.currentTimeMillis());
  }
}
