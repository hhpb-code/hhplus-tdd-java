package io.hhplus.tdd.point.repository;

import io.hhplus.tdd.point.entity.PointHistory;
import java.util.List;

public interface PointHistoryRepository {

  PointHistory insert(PointHistory pointHistory);

  List<PointHistory> findAllByUserId(Long userId);

}
