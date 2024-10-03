package io.hhplus.tdd.point.entity;

import io.hhplus.tdd.error.BusinessException;
import io.hhplus.tdd.point.exception.PointErrorCode;
import io.hhplus.tdd.point.type.TransactionType;

public record PointHistory(
    Long id,
    long userId,
    long amount,
    TransactionType type,
    long updateMillis
) {

  public PointHistory {
    if (id != null && id < 1) {
      throw new BusinessException(PointErrorCode.POINT_HISTORY_ID_INVALID);
    }

    if (userId < 1) {
      throw new BusinessException(PointErrorCode.INVALID_USER_ID);
    }

    if (amount < 1) {
      throw new BusinessException(PointErrorCode.INVALID_AMOUNT);
    }

    if (type == null) {
      throw new BusinessException(PointErrorCode.INVALID_TRANSACTION_TYPE);
    }

    if (updateMillis < 1) {
      throw new BusinessException(PointErrorCode.INVALID_UPDATE_MILLIS);
    }
  }

  public static PointHistory from(long userId, long amount, TransactionType type,
      long updateMillis) {
    return new PointHistory(null, userId, amount, type, updateMillis);
  }

}
