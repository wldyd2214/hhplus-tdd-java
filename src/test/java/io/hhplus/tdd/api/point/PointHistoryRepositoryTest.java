package io.hhplus.tdd.api.point;

import static io.hhplus.tdd.point.TransactionType.CHARGE;
import static io.hhplus.tdd.point.TransactionType.USE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.PointHistoryRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PointHistoryRepositoryTest {

    @Autowired
    private PointHistoryRepository pointHistoryRepository;

    @DisplayName("포인트 히스토리 정보를 등록한다.")
    @Test
    void insert() {
        // given
        long userId = 2;
        long amount = 1000;
        long now = System.currentTimeMillis() % 1000;

        // when
        PointHistory pointHistory = pointHistoryRepository.insert(userId, amount, CHARGE, now);

        // then
        assertThat(pointHistory).extracting("userId", "amount", "type", "updateMillis")
                                .contains(userId, amount, CHARGE, now);
    }

    @DisplayName("포인트 히스토리 정보를 조회한다.")
    @Test
    void selectAllByUserId() {
        // given
        long userId = 1;
        long now = System.currentTimeMillis() % 1000;
        pointHistoryRepository.insert(userId, 2000, CHARGE, now);
        pointHistoryRepository.insert(userId, 1000, USE, now);
//        pointHistoryRepository.insert(2, 3000, CHARGE, now);

        // when
        List<PointHistory> pointHistoryList = pointHistoryRepository.selectAllByUserId(userId);

        // then
        assertThat(pointHistoryList).hasSize(2)
                                    .extracting( "userId", "amount", "type")
                                    .containsExactlyInAnyOrder(
                                        tuple(1L, 2000L, CHARGE),
                                        tuple(1L, 1000L, USE)
                                     );
    }
}
