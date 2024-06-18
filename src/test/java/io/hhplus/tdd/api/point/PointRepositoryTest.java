package io.hhplus.tdd.api.point;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import io.hhplus.tdd.point.PointRepository;
import io.hhplus.tdd.point.PointRepositoryImpl;
import io.hhplus.tdd.point.UserPoint;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

//@Transactional
class PointRepositoryTest {

    private final PointRepository PointRepository;

    public PointRepositoryTest(PointRepositoryImpl pointRepositoryImpl) {
        this.PointRepository = pointRepositoryImpl;
    }

    @DisplayName("특정 유저의 포인트를 충전한다.")
    @Test
    void insertOrUpdate() {
        long id = 1L;
        long amount = 100L;

        // when
        UserPoint userPoint = PointRepository.insertOrUpdate(id, amount);

        // then
        assertThat(userPoint).extracting("id", "point")
                .containsExactlyInAnyOrder(
                        tuple(1L, 100L)
                );
    }
}
