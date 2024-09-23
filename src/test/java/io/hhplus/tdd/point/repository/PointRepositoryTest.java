package io.hhplus.tdd.point.repository;

import static org.assertj.core.api.Assertions.assertThat;

import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.entity.UserPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PointRepositoryTest {

  private static final long USER_ID = 1L;

  @Autowired
  private PointRepository target;

  @Autowired
  private UserPointTable userPointTable;

  @BeforeEach
  void setUp() {
    userPointTable.insertOrUpdate(USER_ID, 0);
  }

  @Test
  @DisplayName("userId로 조회 성공")
  void shouldSuccessfullyFindById() {
    // given
    final Long id = 1L;

    // when
    final var result = target.findById(id).get();

    // then
    assertThat(result).isNotNull();
    assertThat(result.id()).isEqualTo(id);
    assertThat(result.point()).isZero();
    assertThat(result.updateMillis()).isLessThanOrEqualTo(System.currentTimeMillis());
  }

  @Test
  @DisplayName("UserPoint 업데이트 성공")
  void shouldSuccessfullyUpdateUserPoint() {
    // given
    final UserPoint userPoint = userPointTable.selectById(USER_ID);
    final Long point = 100L;
    final var updatedUserPoint = new UserPoint(userPoint.id(), point, System.currentTimeMillis());

    // when
    final var result = target.update(updatedUserPoint);

    // then
    assertThat(result).isNotNull();
    assertThat(result.id()).isEqualTo(USER_ID);
    assertThat(result.point()).isEqualTo(point);
    assertThat(result.updateMillis()).isLessThanOrEqualTo(System.currentTimeMillis());
  }

}