package io.hhplus.tdd.point.repository;

import static org.assertj.core.api.Assertions.assertThat;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.point.entity.PointHistory;
import io.hhplus.tdd.point.type.TransactionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
class PointHistoryRepositoryTest {

  @Autowired
  private PointHistoryRepository target;

  @Autowired
  private PointHistoryTable pointHistoryTable;

  @Test
  @DisplayName("포인트 히스토리 생성 성공")
  void shouldSuccessfullyCreatePointHistory() {
    // given
    final Long userId = 1L;
    final Long amount = 100L;
    final TransactionType transactionType = TransactionType.CHARGE;
    final Long updateMillis = System.currentTimeMillis();
    final PointHistory pointHistory = PointHistory.from(userId, amount, transactionType,
        updateMillis);

    // when
    final PointHistory result = target.insert(pointHistory);

    // then
    assertThat(result).isNotNull();
    assertThat(result.id()).isPositive();
    assertThat(result.userId()).isEqualTo(userId);
    assertThat(result.amount()).isEqualTo(amount);
    assertThat(result.type()).isEqualTo(transactionType);
    assertThat(result.updateMillis()).isEqualTo(updateMillis);
  }

  // NOTE: Test 간의 의존성을 없애기 위해 DirtiesContext를 사용합니다.
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
  @Test
  @DisplayName("userId로 포인트 히스토리 조회 성공")
  void shouldSuccessfullyFindAllByUserId() {
    // given
    final Long userId = 1L;
    final Long amount = 100L;
    final TransactionType transactionType = TransactionType.CHARGE;
    final Long updateMillis = System.currentTimeMillis();
    final PointHistory pointHistory1 = PointHistory.from(userId, amount, transactionType,
        updateMillis);
    final PointHistory pointHistory2 = PointHistory.from(userId, amount, transactionType,
        updateMillis);
    pointHistoryTable.insert(pointHistory1.userId(), pointHistory1.amount(), pointHistory1.type(),
        pointHistory1.updateMillis());
    pointHistoryTable.insert(pointHistory2.userId(), pointHistory2.amount(), pointHistory2.type(),
        pointHistory2.updateMillis());

    // when
    final var result = target.findAllByUserId(userId);

    // then
    assertThat(result).isNotNull();
    assertThat(result).hasSize(2);
    assertThat(result.get(0).id()).isPositive();
    assertThat(result.get(0).userId()).isEqualTo(userId);
    assertThat(result.get(0).amount()).isEqualTo(amount);
    assertThat(result.get(0).type()).isEqualTo(transactionType);
    assertThat(result.get(0).updateMillis()).isEqualTo(updateMillis);
    assertThat(result.get(1).id()).isPositive();
    assertThat(result.get(1).userId()).isEqualTo(userId);
    assertThat(result.get(1).amount()).isEqualTo(amount);
    assertThat(result.get(1).type()).isEqualTo(transactionType);
    assertThat(result.get(1).updateMillis()).isEqualTo(updateMillis);
  }
}