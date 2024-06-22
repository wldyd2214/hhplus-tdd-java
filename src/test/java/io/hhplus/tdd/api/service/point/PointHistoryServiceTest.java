package io.hhplus.tdd.api.service.point;

import static io.hhplus.tdd.point.TransactionType.CHARGE;
import static io.hhplus.tdd.point.TransactionType.USE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.PointHistoryRepository;
import io.hhplus.tdd.point.UserPoint;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PointHistoryServiceTest {

    @Autowired
    private PointHistoryRepository pointHistoryRepository;

    @Autowired
    private PointHistoryService pointHistoryService;

    @DisplayName("특정 유저의 포인트 충전/이용 내역을 조회한다.")
    @Test
    void getUserPointHistory() {
        // given
        long userId = 1;
        long now = System.currentTimeMillis() % 1000;
        pointHistoryRepository.insert(userId, 2000L, CHARGE, now);
        pointHistoryRepository.insert(userId, 1000L, USE, now);
        // Q: 확인하고자 하는 것은, 동일한 유저에 대한 History가 잘 쌓여있는가? 인데, 2번 유저에 대한 given 이 필요할까요?
        // A: 여러 데이터 사이에서 특정 유저의 데이터만 잘 가져오는지를 확인하면 좀 더 신뢰성이 있을 것 같아 2번 유저에 대한 given을 추가했었습니다.
//        pointHistoryRepository.insert(2L, 3000L, CHARGE, now);
//        pointHistoryRepository.insert(2L, 3000L, USE, now);

        // when
        List<PointHistory> pointHistoryList = pointHistoryService.getUserPointHistory(userId);

        // then
        assertThat(pointHistoryList).hasSize(2);
        assertThat(pointHistoryList).extracting( "userId", "amount", "type", "updateMillis")
                             .containsExactlyInAnyOrder(
                                 tuple(userId, 2000L, CHARGE, now),
                                 tuple(userId, 1000L, USE, now)
                             );
    }

}
