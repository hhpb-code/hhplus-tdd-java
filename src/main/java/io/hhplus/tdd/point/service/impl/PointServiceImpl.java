package io.hhplus.tdd.point.service.impl;

import io.hhplus.tdd.error.BusinessException;
import io.hhplus.tdd.point.dto.UserPointCommand;
import io.hhplus.tdd.point.dto.UserPointCommand.GetUserPointHistories;
import io.hhplus.tdd.point.entity.PointHistory;
import io.hhplus.tdd.point.entity.UserPoint;
import io.hhplus.tdd.point.exception.PointErrorCode;
import io.hhplus.tdd.point.repository.PointHistoryRepository;
import io.hhplus.tdd.point.repository.PointRepository;
import io.hhplus.tdd.point.service.PointService;
import io.hhplus.tdd.point.type.TransactionType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {

  private final PointRepository pointRepository;

  private final PointHistoryRepository pointHistoryRepository;

  @Override
  public UserPoint charge(UserPointCommand.Charge command) {
    final var userPoint = pointRepository.findById(command.userId())
        .orElseThrow(() -> new BusinessException(PointErrorCode.USER_POINT_NOT_FOUND));

    final var updatedUserPoint = pointRepository.update(userPoint.addPoint(command.amount()));

    pointHistoryRepository.insert(
        PointHistory.from(userPoint.id(), command.amount(), TransactionType.CHARGE,
            System.currentTimeMillis()));

    return updatedUserPoint;
  }

  @Override
  public UserPoint use(UserPointCommand.Use command) {
    final var userPoint = pointRepository.findById(command.userId())
        .orElseThrow(() -> new BusinessException(PointErrorCode.USER_POINT_NOT_FOUND));

    final var updatedUserPoint = pointRepository.update(userPoint.usePoint(command.amount()));

    pointHistoryRepository.insert(
        PointHistory.from(userPoint.id(), command.amount(), TransactionType.USE,
            System.currentTimeMillis()));

    return updatedUserPoint;
  }

  @Override
  public UserPoint getUserPoint(UserPointCommand.GetUserPoint command) {
    return pointRepository.findById(command.userId()).orElse(null);
  }

  @Override
  public List<PointHistory> getUserPointHistories(GetUserPointHistories command) {
    return pointHistoryRepository.findAllByUserId(command.userId());
  }
}