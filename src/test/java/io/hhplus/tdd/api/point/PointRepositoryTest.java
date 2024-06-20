package io.hhplus.tdd.api.point;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import io.hhplus.tdd.point.PointRepository;
import io.hhplus.tdd.point.PointRepositoryImpl;
import io.hhplus.tdd.point.UserPoint;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PointRepositoryTest {

    // 포인트 레포 테스트 항목들에 대한 코드 리뷰를 받고 싶습니다.

    @Autowired
    private PointRepository PointRepository;

    @DisplayName("특정 유저의 포인트를 저장한다.")
    @Test
    void insertOrUpdate() {
        long id = 1L;
        long amount = 1000L;

        // when
        UserPoint userPoint = PointRepository.insertOrUpdate(id, amount);

        // then
        assertThat(userPoint).extracting("id", "point")
                             .contains(id, amount);
    }
}
