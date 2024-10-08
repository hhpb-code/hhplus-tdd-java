package io.hhplus.tdd.point.controller;

import io.hhplus.tdd.point.dto.UserPointCommand;
import io.hhplus.tdd.point.entity.PointHistory;
import io.hhplus.tdd.point.entity.UserPoint;
import io.hhplus.tdd.point.service.PointService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/point")
@RequiredArgsConstructor
public class PointController {

  private static final Logger log = LoggerFactory.getLogger(PointController.class);

  private final PointService pointService;

  /**
   * TODO - 특정 유저의 포인트를 조회하는 기능을 작성해주세요.
   */
  @GetMapping("{id}")
  public UserPoint point(
      @PathVariable long id
  ) {
    return pointService.getUserPoint(UserPointCommand.GetUserPoint.from(id));
  }

  /**
   * TODO - 특정 유저의 포인트 충전/이용 내역을 조회하는 기능을 작성해주세요.
   */
  @GetMapping("{id}/histories")
  public List<PointHistory> history(
      @PathVariable long id
  ) {
    return pointService.getUserPointHistories(UserPointCommand.GetUserPointHistories.from(id));
  }

  /**
   * TODO - 특정 유저의 포인트를 충전하는 기능을 작성해주세요.
   */
  @PatchMapping("{id}/charge")
  public UserPoint charge(
      @PathVariable Long id,
      @RequestBody Long amount
  ) {
    return pointService.charge(UserPointCommand.Charge.from(id, amount));
  }

  /**
   * TODO - 특정 유저의 포인트를 사용하는 기능을 작성해주세요.
   */
  @PatchMapping("{id}/use")
  public UserPoint use(
      @PathVariable long id,
      @RequestBody long amount
  ) {
    return pointService.use(UserPointCommand.Use.from(id, amount));
  }
}
