package io.hhplus.tdd.point.service;

import io.hhplus.tdd.point.dto.UserPointCommand;
import io.hhplus.tdd.point.entity.PointHistory;
import io.hhplus.tdd.point.entity.UserPoint;
import java.util.List;

public interface PointService {

  UserPoint charge(UserPointCommand.Charge command);

  UserPoint use(UserPointCommand.Use command);

  UserPoint getUserPoint(UserPointCommand.GetUserPoint command);

  List<PointHistory> getUserPointHistories(UserPointCommand.GetUserPointHistories command);
}
