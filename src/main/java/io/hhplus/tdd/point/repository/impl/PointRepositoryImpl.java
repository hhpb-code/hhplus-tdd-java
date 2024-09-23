package io.hhplus.tdd.point.repository.impl;

import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.entity.UserPoint;
import io.hhplus.tdd.point.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PointRepositoryImpl implements PointRepository {

  private final UserPointTable userPointTable;

  @Override
  public UserPoint findById(long id) {
    return userPointTable.selectById(id);
  }

  @Override
  public UserPoint update(UserPoint userPoint) {
    return userPointTable.insertOrUpdate(userPoint.id(), userPoint.point());
  }
}
