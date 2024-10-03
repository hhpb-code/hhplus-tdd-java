package io.hhplus.tdd.point.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.hhplus.tdd.error.BusinessException;
import io.hhplus.tdd.point.exception.PointErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserPointTest {

  @Test
  @DisplayName("UserPoint 생성 실패 - id가 1보다 작을 때")
  void shouldFailToCreateUserPointWhenIdIsLessThanOne() {
    // given
    final Long id = 0L;
    final Long point = 100L;
    final Long updateMillis = System.currentTimeMillis();

    // when
    final var result = assertThrows(BusinessException.class,
        () -> UserPoint.from(id, point, updateMillis));

    // then
    assertThat(result.getErrorCode()).isEqualTo(PointErrorCode.INVALID_USER_ID);
  }


  @Test
  @DisplayName("UserPoint 생성 실패 - point가 0보다 작을 때")
  void shouldFailToCreateUserPointWhenPointIsLessThanZero() {
    // given
    final Long id = 1L;
    final Long point = -1L;
    final Long updateMillis = System.currentTimeMillis();

    // when
    final var result = assertThrows(BusinessException.class,
        () -> UserPoint.from(id, point, updateMillis));

    // then
    assertThat(result.getErrorCode()).isEqualTo(PointErrorCode.INVALID_AMOUNT);
  }

  @Test
  @DisplayName("UserPoint 생성 실패 - updateMillis가 0보다 작을 때")
  void shouldFailToCreateUserPointWhenUpdateMillisIsLessThanZero() {
    // given
    final Long id = 1L;
    final Long point = 100L;
    final Long updateMillis = -1L;

    // when
    final var result = assertThrows(BusinessException.class,
        () -> UserPoint.from(id, point, updateMillis));

    // then
    assertThat(result.getErrorCode()).isEqualTo(PointErrorCode.INVALID_UPDATE_MILLIS);
  }

  @Test
  @DisplayName("UserPoint 생성 성공")
  void shouldSuccessfullyCreateUserPoint() {
    // given
    final Long id = 1L;
    final Long point = 100L;
    final Long updateMillis = System.currentTimeMillis();

    // when
    final var userPoint = UserPoint.from(id, point, updateMillis);

    // then
    assertThat(userPoint.id()).isEqualTo(id);
    assertThat(userPoint.point()).isEqualTo(point);
    assertThat(userPoint.updateMillis()).isEqualTo(updateMillis);
  }

  @Test
  @DisplayName("UserPoint empty 생성 성공")
  void shouldSuccessfullyCreateEmptyUserPoint() {
    // given
    final Long id = 1L;

    // when
    final var userPoint = UserPoint.empty(id);

    // then
    assertThat(userPoint.id()).isEqualTo(id);
    assertThat(userPoint.point()).isZero();
    assertThat(userPoint.updateMillis()).isLessThanOrEqualTo(System.currentTimeMillis());
  }

  @Test
  @DisplayName("UserPoint 포인트 추가 성공")
  void shouldSuccessfullyAddPoint() {
    // given
    final Long id = 1L;
    final Long point = 100L;
    final Long updateMillis = System.currentTimeMillis();
    final var userPoint = UserPoint.from(id, point, updateMillis);
    final Long amount = 100L;

    // when
    final var result = userPoint.addPoint(amount);

    // then
    assertThat(result.id()).isEqualTo(id);
    assertThat(result.point()).isEqualTo(point + amount);
    assertThat(result.updateMillis()).isLessThanOrEqualTo(System.currentTimeMillis());
  }

  @Test
  @DisplayName("UserPoint 포인트 사용 실패 - 포인트가 부족한 경우")
  void shouldFailToUsePointWhenPointIsNotEnough() {
    // given
    final Long id = 1L;
    final Long point = 100L;
    final Long updateMillis = System.currentTimeMillis();
    final var userPoint = UserPoint.from(id, point, updateMillis);
    final Long amount = 101L;

    // when
    final var result = assertThrows(BusinessException.class, () -> userPoint.usePoint(amount));

    // then
    assertThat(result.getErrorCode()).isEqualTo(PointErrorCode.POINT_NOT_ENOUGH);
  }

  @Test
  @DisplayName("UserPoint 포인트 사용 성공")
  void shouldSuccessfullyUsePoint() {
    // given
    final Long id = 1L;
    final Long point = 100L;
    final Long updateMillis = System.currentTimeMillis();
    final var userPoint = UserPoint.from(id, point, updateMillis);
    final Long amount = 100L;

    // when
    final var result = userPoint.usePoint(amount);

    // then
    assertThat(result.id()).isEqualTo(id);
    assertThat(result.point()).isEqualTo(point - amount);
    assertThat(result.updateMillis()).isLessThanOrEqualTo(System.currentTimeMillis());
  }

}