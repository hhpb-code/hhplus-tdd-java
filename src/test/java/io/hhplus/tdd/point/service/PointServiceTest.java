package io.hhplus.tdd.point.service;

import static org.assertj.core.api.Assertions.assertThat;

import io.hhplus.tdd.point.dto.UserPointCommand;
import io.hhplus.tdd.point.entity.UserPoint;
import io.hhplus.tdd.point.repository.PointRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PointServiceTest {

  @Autowired
  private PointService target;

  @Autowired
  private PointRepository pointRepository;

  @Test
  @DisplayName("포인트 충전 성공")
  void shouldSuccessfullyChargePoint() {
    // given
    final Long userId = 1L;
    final Long point = 50L;
    final Long amount = 100L;
    final UserPoint userPoint = UserPoint.from(userId, point, System.currentTimeMillis());
    pointRepository.update(userPoint);
    final UserPointCommand.Charge command = UserPointCommand.Charge.from(userId, amount);

    // when
    final var result = target.charge(command);

    // then
    assertThat(result).isNotNull();
    assertThat(result.id()).isEqualTo(userId);
    assertThat(result.point()).isEqualTo(point + amount);
    assertThat(result.updateMillis()).isLessThanOrEqualTo(System.currentTimeMillis());
  }
}
