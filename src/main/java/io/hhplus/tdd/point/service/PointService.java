package io.hhplus.tdd.point.service;

import io.hhplus.tdd.point.dto.UserPointCommand;
import io.hhplus.tdd.point.entity.UserPoint;

public interface PointService {

  UserPoint charge(UserPointCommand.Charge command);
}
