package io.hhplus.tdd.api.service.point;

import static org.assertj.core.api.Assertions.assertThat;

import io.hhplus.tdd.point.UserPoint;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PointServiceTest {

    @Autowired
    private PointService pointService;

    @DisplayName("특정 유저의 포인트를 충전한다.")
    @Test
    void charge() {
        // given
        long id = 1L;
        long amount = 1000L;

        // when
        UserPoint userPoint = pointService.charge(id, amount);

        // then
        assertThat(userPoint.point()).isNotNull();
        assertThat(userPoint).extracting("id", "point")
                             .contains(id, amount);
    }
}