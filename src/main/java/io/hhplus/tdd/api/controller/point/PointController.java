package io.hhplus.tdd.api.controller.point;

import io.hhplus.tdd.api.ApiResponse;
import io.hhplus.tdd.api.controller.point.request.PointChargeRequest;
import io.hhplus.tdd.api.service.point.PointHistoryService;
import io.hhplus.tdd.api.service.point.PointService;
import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.UserPoint;
import java.time.LocalDateTime;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/point")
public class PointController {

    private static final Logger log = LoggerFactory.getLogger(PointController.class);

    private final PointService pointService;
    private final PointHistoryService pointHistoryService;

    /**
     * TODO - 특정 유저의 포인트를 조회하는 기능을 작성해주세요.
     */
    @GetMapping("{id}")
    public ApiResponse<UserPoint> point(
            @PathVariable long id
    ) {
        return ApiResponse.ok(pointService.getUserPoint(id));
    }

    /**
     * TODO - 특정 유저의 포인트 충전/이용 내역을 조회하는 기능을 작성해주세요.
     */
    @GetMapping("{id}/histories")
    public ApiResponse<List<PointHistory>> history(
            @PathVariable long id
    ) {
        return ApiResponse.ok(pointHistoryService.getUserPointHistory(id));
    }

    /**
     * TODO - 특정 유저의 포인트를 충전하는 기능을 작성해주세요.
     */
    @PatchMapping("{id}/charge")
    public ApiResponse<UserPoint> charge(
        @PathVariable long id,
        @Valid @RequestBody PointChargeRequest reqVo
    ) {
        return ApiResponse.ok(pointService.chargeUserPoint(id, reqVo.getAmount()));
    }

    /**
     * TODO - 특정 유저의 포인트를 사용하는 기능을 작성해주세요.
     */
    @PatchMapping("{id}/use")
    public ApiResponse<UserPoint> use(
            @PathVariable long id,
            @RequestBody long amount
    ) {
        return ApiResponse.ok(pointService.userPointUse(id, amount));
    }
}
