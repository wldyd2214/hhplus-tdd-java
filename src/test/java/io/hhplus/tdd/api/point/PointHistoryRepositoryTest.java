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

    // 포인트 히스토리 레포 테스트 항목들에 대한 코드 리뷰를 받고 싶습니다.

    @Autowired
    private PointHistoryRepository pointHistoryRepository;

    @DisplayName("포인트 히스토리 정보를 등록한다.")
    @Test
    void insert() {
        // given
        long now = System.currentTimeMillis() % 1000;

        // when
        PointHistory pointHistory = pointHistoryRepository.insert(1L, 1000L, CHARGE, now);

        // then
        assertThat(pointHistory).extracting("userId", "amount", "type", "updateMillis")
                                .contains(1L, 1000L, CHARGE, now);
    }

    @DisplayName("포인트 히스토리 정보를 조회한다.")
    @Test
    void selectAllByUserId() {
        // given
        long now = System.currentTimeMillis() % 1000;
        pointHistoryRepository.insert(1L, 2000L, CHARGE, now);
        pointHistoryRepository.insert(1L, 1000L, USE, now);
        pointHistoryRepository.insert(2L, 3000L, CHARGE, now);

        // when
        List<PointHistory> pointHistoryList = pointHistoryRepository.selectAllByUserId(1L);

        // then
        assertThat(pointHistoryList).extracting( "userId", "amount", "type")
                                    .containsExactlyInAnyOrder(
                                        tuple(1L, 2000L, CHARGE),
                                        tuple(1L, 1000L, USE)
                                     );
    }
}
