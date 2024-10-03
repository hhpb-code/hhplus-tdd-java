package io.hhplus.tdd.point.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.hhplus.tdd.error.BusinessException;
import io.hhplus.tdd.point.exception.PointErrorCode;
import io.hhplus.tdd.point.type.TransactionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PointHistoryTest {

  @Test
  @DisplayName("PointHistory 생성 실패 - id가 1보다 작을 때")
  void shouldFailToCreatePointHistoryWhenIdIsLessThanOne() {
    // given
    final Long userId = 1L;
    final Long amount = 100L;
    final TransactionType type = TransactionType.CHARGE;
    final Long updateMillis = System.currentTimeMillis();

    // when
    final var result = assertThrows(BusinessException.class,
        () -> new PointHistory(0L, userId, amount, type, updateMillis));

    // then
    assertThat(result.getErrorCode()).isEqualTo(PointErrorCode.POINT_HISTORY_ID_INVALID);
  }

  @Test
  @DisplayName("PointHistory 생성 실패 - userId가 1보다 작을 때")
  void shouldFailToCreatePointHistoryWhenUserIdIsLessThanOne() {
    // given
    final Long userId = 0L;
    final Long amount = 100L;
    final TransactionType type = TransactionType.CHARGE;
    final Long updateMillis = System.currentTimeMillis();

    // when
    final var result = assertThrows(BusinessException.class,
        () -> PointHistory.from(userId, amount, type, updateMillis));

    // then
    assertThat(result.getErrorCode()).isEqualTo(PointErrorCode.INVALID_USER_ID);
  }

  @Test
  @DisplayName("PointHistory 생성 실패 - amount가 1보다 작을 때")
  void shouldFailToCreatePointHistoryWhenAmountIsLessThanOne() {
    // given
    final Long userId = 1L;
    final Long amount = 0L;
    final TransactionType type = TransactionType.CHARGE;
    final Long updateMillis = System.currentTimeMillis();

    // when
    final var result = assertThrows(BusinessException.class,
        () -> PointHistory.from(userId, amount, type, updateMillis));

    // then
    assertThat(result.getErrorCode()).isEqualTo(PointErrorCode.INVALID_AMOUNT);
  }

  @Test
  @DisplayName("PointHistory 생성 실패 - type이 null일 때")
  void shouldFailToCreatePointHistoryWhenTypeIsNull() {
    // given
    final Long userId = 1L;
    final Long amount = 100L;
    final TransactionType type = null;
    final Long updateMillis = System.currentTimeMillis();

    // when
    final var result = assertThrows(BusinessException.class,
        () -> PointHistory.from(userId, amount, type, updateMillis));

    // then
    assertThat(result.getErrorCode()).isEqualTo(PointErrorCode.INVALID_TRANSACTION_TYPE);
  }

  @Test
  @DisplayName("PointHistory 생성 실패 - updateMillis가 1보다 작을 때")
  void shouldFailToCreatePointHistoryWhenUpdateMillisIsLessThanOne() {
    // given
    final Long userId = 1L;
    final Long amount = 100L;
    final TransactionType type = TransactionType.CHARGE;
    final Long updateMillis = 0L;

    // when
    final var result = assertThrows(BusinessException.class,
        () -> PointHistory.from(userId, amount, type, updateMillis));

    // then
    assertThat(result.getErrorCode()).isEqualTo(PointErrorCode.INVALID_UPDATE_MILLIS);
  }

  @Test
  @DisplayName("PointHistory 생성 성공 - id가 null일 때")
  void shouldSuccessfullyCreatePointHistoryWhenIdIsNull() {
    // given
    final Long userId = 1L;
    final Long amount = 100L;
    final TransactionType type = TransactionType.CHARGE;
    final Long updateMillis = System.currentTimeMillis();

    // when
    final var pointHistory = PointHistory.from(userId, amount, type, updateMillis);

    // then
    assertThat(pointHistory.id()).isNull();
    assertThat(pointHistory.userId()).isEqualTo(userId);
    assertThat(pointHistory.amount()).isEqualTo(amount);
    assertThat(pointHistory.type()).isEqualTo(type);
    assertThat(pointHistory.updateMillis()).isEqualTo(updateMillis);
  }
}