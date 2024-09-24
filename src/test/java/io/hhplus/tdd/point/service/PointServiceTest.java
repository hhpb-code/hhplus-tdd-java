package io.hhplus.tdd.point.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.hhplus.tdd.error.BusinessException;
import io.hhplus.tdd.point.dto.UserPointCommand;
import io.hhplus.tdd.point.entity.UserPoint;
import io.hhplus.tdd.point.repository.PointHistoryRepository;
import io.hhplus.tdd.point.repository.PointRepository;
import io.hhplus.tdd.point.type.TransactionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
class PointServiceTest {

  @Autowired
  private PointService target;

  @Autowired
  private PointRepository pointRepository;

  @Autowired
  private PointHistoryRepository pointHistoryRepository;

  // NOTE: Test 간의 의존성을 없애기 위해 DirtiesContext를 사용합니다.
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
  @Test
  @DisplayName("포인트 충전 성공")
  void shouldSuccessfullyChargePoint() {
    // given
    final Long userId = 1L;
    final Long point = 50L;
    final Long amount = 100L;
    final UserPoint userPoint = UserPoint.from(userId, point, System.currentTimeMillis());
    // NOTE: insert가 없어 update로 초기값 설정
    pointRepository.update(userPoint);
    final UserPointCommand.Charge command = UserPointCommand.Charge.from(userId, amount);

    // when
    final var result = target.charge(command);

    // then
    assertThat(result).isNotNull();
    assertThat(result.id()).isEqualTo(userId);
    assertThat(result.point()).isEqualTo(point + amount);
    assertThat(result.updateMillis()).isLessThanOrEqualTo(System.currentTimeMillis());

    final var pointHistory = pointHistoryRepository.findAllByUserId(userId).get(0);
    assertThat(pointHistory.userId()).isEqualTo(userId);
    assertThat(pointHistory.amount()).isEqualTo(amount);
    assertThat(pointHistory.type()).isEqualTo(TransactionType.CHARGE);
    assertThat(pointHistory.updateMillis()).isLessThanOrEqualTo(System.currentTimeMillis());
  }

  @Test
  @DisplayName("포인트 충전 실패 - 최대 포인트 초과")
  void shouldFailToChargePointWhenExceedMaxPoint() {
    // given
    final Long userId = 1L;
    final Long point = Long.MAX_VALUE;
    final Long amount = 1L;
    final UserPoint userPoint = UserPoint.from(userId, point, System.currentTimeMillis());
    // NOTE: insert가 없어 update로 초기값 설정
    pointRepository.update(userPoint);
    final UserPointCommand.Charge command = UserPointCommand.Charge.from(userId, amount);

    // when
    final var result = assertThrows(BusinessException.class, () -> target.charge(command));

    // then
    assertThat(result.getMessage()).isEqualTo("포인트가 최대치를 초과했습니다.");
  }

  @Test
  @DisplayName("포인트 사용 실패 - 포인트 부족")
  void shouldFailToUsePointWhenPointIsNotEnough() {
    // given
    final Long userId = 1L;
    final Long point = 50L;
    final Long amount = 100L;
    final UserPoint userPoint = UserPoint.from(userId, point, System.currentTimeMillis());
    // NOTE: insert가 없어 update로 초기값 설정
    pointRepository.update(userPoint);
    final UserPointCommand.Use command = UserPointCommand.Use.from(userId, amount);

    // when
    final var result = assertThrows(BusinessException.class, () -> target.use(command));

    // then
    assertThat(result.getMessage()).isEqualTo("포인트가 부족합니다.");
  }

  // NOTE: Test 간의 의존성을 없애기 위해 DirtiesContext를 사용합니다.
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
  @Test
  @DisplayName("포인트 사용 성공")
  void shouldSuccessfullyUsePoint() {
    // given
    final Long userId = 1L;
    final Long point = 100L;
    final Long amount = 50L;
    final UserPoint userPoint = UserPoint.from(userId, point, System.currentTimeMillis());
    // NOTE: insert가 없어 update로 초기값 설정
    pointRepository.update(userPoint);
    final UserPointCommand.Use command = UserPointCommand.Use.from(userId, amount);

    // when
    final var result = target.use(command);

    // then
    assertThat(result).isNotNull();
    assertThat(result.id()).isEqualTo(userId);
    assertThat(result.point()).isEqualTo(point - amount);
    assertThat(result.updateMillis()).isLessThanOrEqualTo(System.currentTimeMillis());

    final var pointHistory = pointHistoryRepository.findAllByUserId(userId).get(0);
    assertThat(pointHistory.userId()).isEqualTo(userId);
    assertThat(pointHistory.amount()).isEqualTo(amount);
    assertThat(pointHistory.type()).isEqualTo(TransactionType.USE);
    assertThat(pointHistory.updateMillis()).isLessThanOrEqualTo(System.currentTimeMillis());
  }

  @Test
  @DisplayName("포인트 조회 성공")
  void shouldSuccessfullyGetUserPoint() {
    // given
    final Long userId = 1L;
    final Long point = 100L;
    final UserPoint userPoint = UserPoint.from(userId, point, System.currentTimeMillis());
    // NOTE: insert가 없어 update로 초기값 설정
    pointRepository.update(userPoint);
    final UserPointCommand.GetUserPoint command = UserPointCommand.GetUserPoint.from(userId);

    // when
    final var result = target.getUserPoint(command);

    // then
    assertThat(result).isNotNull();
    assertThat(result.id()).isEqualTo(userId);
    assertThat(result.point()).isEqualTo(point);
    assertThat(result.updateMillis()).isLessThanOrEqualTo(System.currentTimeMillis());
  }
}
