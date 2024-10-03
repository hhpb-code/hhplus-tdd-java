package io.hhplus.tdd.point.repository;

import io.hhplus.tdd.point.entity.UserPoint;
import java.util.Optional;

public interface PointRepository {

  Optional<UserPoint> findById(long id);

  UserPoint update(UserPoint userPoint);
}
