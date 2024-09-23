package io.hhplus.tdd.point.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doReturn;

import io.hhplus.tdd.error.BusinessException;
import io.hhplus.tdd.point.dto.UserPointCommand;
import io.hhplus.tdd.point.entity.UserPoint;
import io.hhplus.tdd.point.exception.PointErrorCode;
import io.hhplus.tdd.point.repository.PointHistoryRepository;
import io.hhplus.tdd.point.repository.PointRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PointServiceImplTest {

  @InjectMocks
  private PointServiceImpl target;

  @Mock
  private PointRepository pointRepository;

  @Mock
  private PointHistoryRepository pointHistoryRepository;

  @Test
  @DisplayName("포인트 충전 실패 - userPoint가 null인 경우")
  void shouldFailToChargePointWhenUserPointIsNull() {
    // given
    final Long userId = 1L;
    final Long amount = 100L;
    final UserPointCommand.Charge command = UserPointCommand.Charge.from(userId, amount);
    doReturn(Optional.empty()).when(pointRepository).findById(userId);

    // when
    final var result = assertThrows(BusinessException.class, () -> target.charge(command));

    // then
    assertThat(result.getMessage()).isEqualTo(PointErrorCode.USER_POINT_NOT_FOUND.getMessage());
  }
  
  @Test
  @DisplayName("포인트 충전 성공")
  void shouldSuccessfullyChargePoint() {
    // given
    final Long userId = 1L;
    final Long point = 50L;
    final Long amount = 100L;
    final UserPoint userPoint = UserPoint.from(userId, point, System.currentTimeMillis());
    final UserPoint updatedUserPoint = UserPoint.from(userId, point + amount,
        System.currentTimeMillis());
    final UserPointCommand.Charge command = UserPointCommand.Charge.from(userId, amount);
    doReturn(Optional.of(userPoint)).when(pointRepository).findById(userId);
    doReturn(updatedUserPoint).when(pointRepository)
        .update(argThat(up -> up.id() == userId && up.point() == (point + amount)
        ));

    // when
    final var result = target.charge(command);

    // then
    assertThat(result).isNotNull();
    assertThat(result.id()).isEqualTo(userId);
    assertThat(result.point()).isEqualTo(point + amount);
    assertThat(result.updateMillis()).isLessThanOrEqualTo(System.currentTimeMillis());
  }

}