package io.hhplus.tdd.point.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.hhplus.tdd.error.BusinessException;
import io.hhplus.tdd.point.dto.UserPointCommand;
import io.hhplus.tdd.point.entity.PointHistory;
import io.hhplus.tdd.point.entity.UserPoint;
import io.hhplus.tdd.point.repository.PointHistoryRepository;
import io.hhplus.tdd.point.repository.PointRepository;
import io.hhplus.tdd.point.type.TransactionType;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;
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

  // NOTE: Test 간의 의존성을 없애기 위해 DirtiesContext를 사용합니다.
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
  @Test
  @DisplayName("포인트 충전/사용 내역 조회 성공 - 내역 없음")
  void shouldSuccessfullyGetUserPointHistoriesWhenNoHistories() {
    // given
    final Long userId = 1L;
    final UserPointCommand.GetUserPointHistories command = UserPointCommand.GetUserPointHistories.from(
        userId);

    // when
    final var result = target.getUserPointHistories(command);

    // then
    assertThat(result).isNotNull();
    assertThat(result).isEmpty();
  }

  // NOTE: Test 간의 의존성을 없애기 위해 DirtiesContext를 사용합니다.
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
  @Test
  @DisplayName("포인트 충전/사용 내역 조회 성공")
  void shouldSuccessfullyGetUserPointHistories() {
    // given
    final Long userId = 1L;
    final PointHistory pointHistory1 = PointHistory.from(userId, 100L, TransactionType.CHARGE,
        System.currentTimeMillis());
    final PointHistory pointHistory2 = PointHistory.from(userId, 50L, TransactionType.USE,
        System.currentTimeMillis());

    pointHistoryRepository.insert(pointHistory1);
    pointHistoryRepository.insert(pointHistory2);
    final UserPointCommand.GetUserPointHistories command = UserPointCommand.GetUserPointHistories.from(
        userId);

    // when
    final var result = target.getUserPointHistories(command);

    // then
    assertThat(result).isNotNull();
    assertThat(result).hasSize(2);
    assertThat(result.get(0).id()).isEqualTo(1);
    assertThat(result.get(0).userId()).isEqualTo(userId);
    assertThat(result.get(0).amount()).isEqualTo(100);
    assertThat(result.get(0).type()).isEqualTo(TransactionType.CHARGE);
    assertThat(result.get(0).updateMillis()).isLessThanOrEqualTo(System.currentTimeMillis());
    assertThat(result.get(1).id()).isEqualTo(2);
    assertThat(result.get(1).userId()).isEqualTo(userId);
    assertThat(result.get(1).amount()).isEqualTo(50);
    assertThat(result.get(1).type()).isEqualTo(TransactionType.USE);
    assertThat(result.get(1).updateMillis()).isLessThanOrEqualTo(System.currentTimeMillis());
  }

  // NOTE: Test 간의 의존성을 없애기 위해 DirtiesContext를 사용합니다.
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
  @Test
  @DisplayName("포인트 충전 동시성 검증")
  void shouldSuccessfullyChargePointConcurrently() {
    // given
    final Long userId = 1L;
    final Long point = 50L;
    final UserPoint userPoint = UserPoint.from(userId, point, System.currentTimeMillis());
    // NOTE: insert가 없어 update로 초기값 설정
    pointRepository.update(userPoint);
    final int numOperations = 10;
    final List<Long> amounts = IntStream.range(0, numOperations)
        .mapToObj(i -> 100L * (i + 1))
        .toList();

    List<CompletableFuture<Void>> futures = amounts.stream()
        .map(i -> CompletableFuture.runAsync(
            () -> target.charge(UserPointCommand.Charge.from(userId, i)))
        )
        .toList();

    // when
    CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

    // then
    final var result = pointRepository.findById(userId).orElseThrow();
    assertThat(result.id()).isEqualTo(userId);
    assertThat(result.point()).isEqualTo(point + amounts.stream().reduce(0L, Long::sum));
    assertThat(result.updateMillis()).isLessThanOrEqualTo(System.currentTimeMillis());
    final var pointHistories = pointHistoryRepository.findAllByUserId(userId);
    assertThat(pointHistories).hasSize(numOperations);
  }

  // NOTE: Test 간의 의존성을 없애기 위해 DirtiesContext를 사용합니다.
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
  @Test
  @DisplayName("포인트 사용 동시성 검증")
  void shouldSuccessfullyUsePointConcurrently() {
    // given
    final Long userId = 1L;
    final Long point = 100000L;
    final UserPoint userPoint = UserPoint.from(userId, point, System.currentTimeMillis());
    // NOTE: insert가 없어 update로 초기값 설정
    pointRepository.update(userPoint);
    final int numOperations = 10;
    final List<Long> amounts = IntStream.range(0, numOperations)
        .mapToObj(i -> 50L * (i + 1))
        .toList();

    List<CompletableFuture<Void>> futures = amounts.stream()
        .map(i -> CompletableFuture.runAsync(() -> target.use(UserPointCommand.Use.from(userId, i)))
        )
        .toList();

    // when
    CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

    // then
    final var result = pointRepository.findById(userId).orElseThrow();
    assertThat(result.id()).isEqualTo(userId);
    assertThat(result.point()).isEqualTo(point - amounts.stream().reduce(0L, Long::sum));
    assertThat(result.updateMillis()).isLessThanOrEqualTo(System.currentTimeMillis());
    final var pointHistories = pointHistoryRepository.findAllByUserId(userId);
    assertThat(pointHistories).hasSize(numOperations);
  }

  // NOTE: Test 간의 의존성을 없애기 위해 DirtiesContext를 사용합니다.
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
  @Test
  @DisplayName("포인트 충전/사용 동시성 검증")
  void shouldSuccessfullyChargeAndUsePointConcurrently() {
    // given
    final Long userId = 1L;
    final Long point = 100000L;
    final UserPoint userPoint = UserPoint.from(userId, point, System.currentTimeMillis());
    // NOTE: insert가 없어 update로 초기값 설정
    pointRepository.update(userPoint);
    final int numOperations = 10;
    final List<Long> amounts = IntStream.range(0, numOperations)
        .mapToObj(i -> 50L * (i + 1))
        .toList();

    List<CompletableFuture<Void>> futures = amounts.stream()
        .map(i -> CompletableFuture.runAsync(() -> {
          if (i % 2 == 0) {
            target.charge(UserPointCommand.Charge.from(userId, i));
          } else {
            target.use(UserPointCommand.Use.from(userId, i));
          }
        }))
        .toList();

    // when
    CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

    // then
    final var result = pointRepository.findById(userId).orElseThrow();
    assertThat(result.id()).isEqualTo(userId);
    assertThat(result.point()).isEqualTo(
        point + amounts.stream().filter(i -> i % 2 == 0).reduce(0L, Long::sum)
            - amounts.stream().filter(i -> i % 2 != 0).reduce(0L, Long::sum));
    assertThat(result.updateMillis()).isLessThanOrEqualTo(System.currentTimeMillis());
    final var pointHistories = pointHistoryRepository.findAllByUserId(userId);
    assertThat(pointHistories).hasSize(numOperations);
  }

}
