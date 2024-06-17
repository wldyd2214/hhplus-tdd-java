package io.hhplus.tdd.api.service.point;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import io.hhplus.tdd.point.UserPoint;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
class PointServiceTest {

    @Autowired
    private PointService pointService;

    @DisplayName("특정 유저의 포인트를 충전한다.")
    @Test
    void charge() {
        // given
        long id = 1L;
        long amount = 100L;
        LocalDateTime registeredDateTime = LocalDateTime.now();

        // when
        UserPoint userPoint = pointService.charge(id, amount, registeredDateTime);

        // then
        assertThat(userPoint.getId()).isNotNull();
    }

}