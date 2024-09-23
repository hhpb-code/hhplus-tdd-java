package io.hhplus.tdd.point.repository;

import io.hhplus.tdd.point.entity.UserPoint;

public interface PointRepository {

  UserPoint findById(long id);

  UserPoint update(UserPoint userPoint);
}
