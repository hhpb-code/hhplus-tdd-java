package io.hhplus.tdd.point.dto;

import io.hhplus.tdd.error.BusinessException;
import io.hhplus.tdd.point.exception.PointErrorCode;

public class UserPointCommand {

  public record Charge(Long userId, Long amount) {

    public Charge {
      if (userId == null) {
        throw new BusinessException(PointErrorCode.INVALID_USER_ID);
      }

      if (amount == null || amount <= 0) {
        throw new BusinessException(PointErrorCode.INVALID_AMOUNT);
      }
    }

    public static Charge from(Long userId, Long amount) {
      return new Charge(userId, amount);
    }
  }

  public record Use(Long userId, Long amount) {

    public Use {
      if (userId == null) {
        throw new BusinessException(PointErrorCode.INVALID_USER_ID);
      }

      if (amount == null || amount <= 0) {
        throw new BusinessException(PointErrorCode.INVALID_AMOUNT);
      }
    }

    public static Use from(Long userId, Long amount) {
      return new Use(userId, amount);
    }
  }

  public record FindById(Long userId) {

    public FindById {
      if (userId == null) {
        throw new BusinessException(PointErrorCode.INVALID_USER_ID);
      }
    }

    public static FindById from(Long userId) {
      return new FindById(userId);
    }
  }
}
