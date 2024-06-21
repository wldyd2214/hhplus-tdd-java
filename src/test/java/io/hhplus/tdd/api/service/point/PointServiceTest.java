package io.hhplus.tdd.api.service.point;

import static io.hhplus.tdd.point.TransactionType.CHARGE;
import static io.hhplus.tdd.point.TransactionType.USE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.PointHistoryRepository;
import io.hhplus.tdd.point.PointRepository;
import io.hhplus.tdd.point.UserPoint;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PointServiceTest {

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private PointHistoryRepository pointHistoryRepository;

    @Autowired
    private PointService pointService;

    @DisplayName("특정 유저의 포인트를 충전한다.")
    @Test
    void charge() {
        // given
        long id = 1L;
        long amount = 1000L;

        // when
        UserPoint userPoint = pointService.chargeUserPoint(id, amount);

        // then
        assertThat(userPoint.point()).isNotNull();
        assertThat(userPoint).extracting("id", "point")
                             .contains(id, amount);
    }

    @DisplayName("포인트를 충전시 히스토리 정보가 잘 적재 되었는지 확인한다.")
    @Test
    void userPointChargeHistory() {
        // given
        long id = 1L;
        long amount = 1000L;

        // when
        pointService.chargeUserPoint(id, amount);
        List<PointHistory> pointHistoryList = pointHistoryRepository.selectAllByUserId(id);

        // then
        assertThat(pointHistoryList).hasSize(1);
        assertThat(pointHistoryList).extracting( "userId", "amount", "type")
                                    .containsExactlyInAnyOrder(
                                        tuple(id, amount, CHARGE)
                                    );
    }

    @DisplayName("특정 유저의 포인트 정보를 조회한다.")
    @Test
    void getUserPoint() {
        // given
        pointRepository.insertOrUpdate(1L, 1000L);
        pointRepository.insertOrUpdate(2L, 2000L);
        pointRepository.insertOrUpdate(3L, 3000L);

        // when
        UserPoint userPoint = pointService.getUserPoint(1L);

        // then
        assertThat(userPoint.point()).isNotNull();
        assertThat(userPoint).extracting("id", "point")
                             .contains(1L, 1000L);
    }

    @DisplayName("특정 유저의 포인트를 사용한다.")
    @Test
    void userPointUse() {
        // given
        pointRepository.insertOrUpdate(1L, 1000L);
        pointRepository.insertOrUpdate(2L, 2000L);
        pointRepository.insertOrUpdate(3L, 3000L);

        // when
        UserPoint userPoint = pointService.userPointUse(1L, 700L);

        // then
        assertThat(userPoint.point()).isNotNull();
        assertThat(userPoint).extracting("id", "point")
                             .contains(1L, 300L);
    }

    @DisplayName("포인트를 사용시 히스토리 정보가 잘 적재 되었는지 확인한다.")
    @Test
    void userPointUseHistory() {
        // given
        pointRepository.insertOrUpdate(1L, 1000L);
        pointRepository.insertOrUpdate(2L, 2000L);
        pointRepository.insertOrUpdate(3L, 3000L);

        // when
        pointService.userPointUse(1L, 700L);
        List<PointHistory> pointHistoryList = pointHistoryRepository.selectAllByUserId(1L);

        // then
        assertThat(pointHistoryList).hasSize(1);
        assertThat(pointHistoryList).extracting( "userId", "amount", "type")
                                     .containsExactlyInAnyOrder(
                                         tuple(1L, 700L, USE)
                                     );
    }

    @DisplayName("가지고 있는 포인트 보다 많은 금액을 사용한 경우 예외가 발생한다.")
    @Test
    void usePointOverride() {
        // given
        pointRepository.insertOrUpdate(1L, 1000L);
        pointRepository.insertOrUpdate(2L, 2000L);
        pointRepository.insertOrUpdate(3L, 3000L);

        // when // then
        assertThatThrownBy(() -> pointService.userPointUse(1L, 1001L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("보유 포인트가 부족합니다.");
    }

    @DisplayName("사용자 포인트 충전/사용 동시성 테스트")
    @Test
    void userPointAsync() {
        // given
        long id = 1;
        pointService.chargeUserPoint(id, 10000);

        // when
        CompletableFuture.allOf(
                CompletableFuture.runAsync(() -> {
                    pointService.userPointUse(id, 1000);
                }),
                CompletableFuture.runAsync(() -> {
                    pointService.chargeUserPoint(id, 300);

                }),
                CompletableFuture.runAsync(() -> {
                    pointService.userPointUse(id, 200);
                })
        ).join();

        UserPoint userPoint = pointService.getUserPoint(id);
        System.out.println("사용자 포인트: " + userPoint.point());
        assertThat(userPoint.point()).isEqualTo(10000 - 1000 + 300 - 200);
    }
}