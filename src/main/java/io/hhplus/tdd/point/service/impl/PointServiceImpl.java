package io.hhplus.tdd.point.service.impl;

import io.hhplus.tdd.error.BusinessException;
import io.hhplus.tdd.point.dto.UserPointCommand;
import io.hhplus.tdd.point.entity.UserPoint;
import io.hhplus.tdd.point.exception.PointErrorCode;
import io.hhplus.tdd.point.repository.PointRepository;
import io.hhplus.tdd.point.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {

  private final PointRepository pointRepository;

  @Override
  public UserPoint charge(UserPointCommand.Charge command) {
    final var userPoint = pointRepository.findById(command.userId())
        .orElseThrow(() -> new BusinessException(PointErrorCode.USER_POINT_NOT_FOUND));

    final var updatedUserPoint = userPoint.addPoint(command.amount());

    return pointRepository.update(updatedUserPoint);
  }
}