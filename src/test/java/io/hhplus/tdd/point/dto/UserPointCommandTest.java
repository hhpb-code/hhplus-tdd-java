package io.hhplus.tdd.point.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.hhplus.tdd.error.BusinessException;
import io.hhplus.tdd.point.exception.PointErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserPointCommandTest {

  @Test
  @DisplayName("UserPointCommand.Charge 생성 실패 - userId가 null")
  void shouldFailToCreateUserPointCommandChargeWhenUserIdIsNull() {
    // given
    final Long userId = null;
    final Long amount = 100L;

    // when
    final BusinessException result = assertThrows(BusinessException.class,
        () -> UserPointCommand.Charge.from(userId, amount));

    // then
    assertThat(result.getMessage()).isEqualTo(PointErrorCode.INVALID_USER_ID.getMessage());
  }

  @Test
  @DisplayName("UserPointCommand.Charge 생성 실패 - amount 가 null")
  void shouldFailToCreateUserPointCommandChargeWhenAmountIsNull() {
    // given
    final Long userId = 1L;
    final Long amount = null;

    // when
    final BusinessException result = assertThrows(BusinessException.class,
        () -> UserPointCommand.Charge.from(userId, amount));

    // then
    assertThat(result.getMessage()).isEqualTo(PointErrorCode.INVALID_AMOUNT.getMessage());
  }

  @Test
  @DisplayName("UserPointCommand.Charge 생성 실패 - amount 가 0이하")
  void shouldFailToCreateUserPointCommandChargeWhenAmountIsLessThanZero() {
    // given
    final Long userId = 1L;
    final Long amount = 0L;

    // when
    final BusinessException result = assertThrows(BusinessException.class,
        () -> UserPointCommand.Charge.from(userId, amount));

    // then
    assertThat(result.getMessage()).isEqualTo(PointErrorCode.INVALID_AMOUNT.getMessage());
  }

}