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

  @Test
  @DisplayName("UserPointCommand.Charge 생성 성공")
  void shouldCreateUserPointCommandCharge() {
    // given
    final Long userId = 1L;
    final Long amount = 100L;

    // when
    final UserPointCommand.Charge result = UserPointCommand.Charge.from(userId, amount);

    // then
    assertThat(result.userId()).isEqualTo(userId);
    assertThat(result.amount()).isEqualTo(amount);
  }

  @Test
  @DisplayName("UserPointCommand.Use 생성 실패 - userId가 null")
  void shouldFailToCreateUserPointCommandUseWhenUserIdIsNull() {
    // given
    final Long userId = null;
    final Long amount = 100L;

    // when
    final BusinessException result = assertThrows(BusinessException.class,
        () -> UserPointCommand.Use.from(userId, amount));

    // then
    assertThat(result.getMessage()).isEqualTo(PointErrorCode.INVALID_USER_ID.getMessage());
  }

  @Test
  @DisplayName("UserPointCommand.Use 생성 실패 - amount 가 null")
  void shouldFailToCreateUserPointCommandUseWhenAmountIsNull() {
    // given
    final Long userId = 1L;
    final Long amount = null;

    // when
    final BusinessException result = assertThrows(BusinessException.class,
        () -> UserPointCommand.Use.from(userId, amount));

    // then
    assertThat(result.getMessage()).isEqualTo(PointErrorCode.INVALID_AMOUNT.getMessage());
  }

  @Test
  @DisplayName("UserPointCommand.Use 생성 실패 - amount 가 0이하")
  void shouldFailToCreateUserPointCommandUseWhenAmountIsLessThanZero() {
    // given
    final Long userId = 1L;
    final Long amount = 0L;

    // when
    final BusinessException result = assertThrows(BusinessException.class,
        () -> UserPointCommand.Use.from(userId, amount));

    // then
    assertThat(result.getMessage()).isEqualTo(PointErrorCode.INVALID_AMOUNT.getMessage());
  }

  @Test
  @DisplayName("UserPointCommand.Use 생성 성공")
  void shouldCreateUserPointCommandUse() {
    // given
    final Long userId = 1L;
    final Long amount = 100L;

    // when
    final UserPointCommand.Use result = UserPointCommand.Use.from(userId, amount);

    // then
    assertThat(result.userId()).isEqualTo(userId);
    assertThat(result.amount()).isEqualTo(amount);
  }

  @Test
  @DisplayName("UserPointCommand.GetUserPoint 생성 실패 - userId가 null")
  void shouldFailToCreateUserPointCommandGetUserPointWhenUserIdIsNull() {
    // given
    final Long userId = null;

    // when
    final BusinessException result = assertThrows(BusinessException.class,
        () -> UserPointCommand.GetUserPoint.from(userId));

    // then
    assertThat(result.getMessage()).isEqualTo(PointErrorCode.INVALID_USER_ID.getMessage());
  }

  @Test
  @DisplayName("UserPointCommand.GetUserPoint 생성 성공")
  void shouldCreateUserPointCommandGetUserPoint() {
    // given
    final Long userId = 1L;

    // when
    final UserPointCommand.GetUserPoint result = UserPointCommand.GetUserPoint.from(userId);

    // then
    assertThat(result.userId()).isEqualTo(userId);
  }

}