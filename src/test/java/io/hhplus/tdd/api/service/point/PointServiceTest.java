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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PointServiceTest {

    // 포인트 서비스 테스트 항목들에 대한 코드 리뷰를 받고 싶습니다.

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
}