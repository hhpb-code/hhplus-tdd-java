package io.hhplus.tdd.point.repository.impl;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.point.entity.PointHistory;
import io.hhplus.tdd.point.repository.PointHistoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PointHistoryRepositoryImpl implements PointHistoryRepository {

  private final PointHistoryTable pointHistoryTable;


  @Override
  public PointHistory insert(PointHistory pointHistory) {
    return pointHistoryTable.insert(pointHistory.userId(), pointHistory.amount(),
        pointHistory.type(), pointHistory.updateMillis());
  }

  @Override
  public List<PointHistory> findAllByUserId(Long userId) {
    return pointHistoryTable.selectAllByUserId(userId);
  }
}
