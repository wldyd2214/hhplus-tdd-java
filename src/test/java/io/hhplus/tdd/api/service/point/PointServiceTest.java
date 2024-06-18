package io.hhplus.tdd.api.service.point;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.hhplus.tdd.point.PointRepository;
import io.hhplus.tdd.point.UserPoint;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

@SpringBootTest
class PointServiceTest {

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private PointService pointService;

//    @AfterEach
//    void tearDown() {
//        pointRepository.;
//    }

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

    @DisplayName("특정 유저의 포인트를 사용한다.")
    @Test
    void userPointOverrideUse() {
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

    @DisplayName("가지고 있는 포인트 보다 많은 금액을 사용한 경우 예외가 발생한다.")
    @Test
    void usePointOverride() throws Exception {
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