package io.hhplus.tdd.api.service.point;

import static io.hhplus.tdd.point.TransactionType.CHARGE;
import static io.hhplus.tdd.point.TransactionType.USE;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.PointHistoryRepository;
import io.hhplus.tdd.point.PointRepository;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PointService {

    private final PointRepository pointRepository;

    private final PointHistoryRepository pointHistoryRepository;

    // Q: 불필요하게 private 으로 함수를 다 쪼개놓아서 오히려 테스트/가독성 측면에서 좋지 않은 방향성으로 보입니다.
    // Q: 또한 amount 도 음수이고, user 의 포인트 또한 음수인 경우 ?
    // Q: validation 을 통과해 포인트를 마치 충전하는 것과 같은 calculation 이 일어날 텐데, 이에 대한 고려가 부족합니다.

    public UserPoint getUserPoint(long id) {
        return selectUserPointById(id);
    }

    public UserPoint chargeUserPoint(long id, long amount) {
        UserPoint userPoint = selectUserPointById(id);

        insertPointHistory(id, amount, CHARGE);

        long sumPoint = userPoint.point() + amount;
        return insertUserPointOrUpdate(id, sumPoint);
    }

    public UserPoint userPointUse(long id, long amount) {
        UserPoint userPoint = selectUserPointById(id);
        System.out.println(String.format("%d 유저의 %d 포인트가 존재하고 %d 만큼 사용하고 싶어요.", id, userPoint.point(), amount));
        if (userPoint.point() < amount)
            throw new IllegalArgumentException("보유 포인트가 부족합니다.");

        insertPointHistory(id, amount, USE);

        long usedPoint = usedPointCalculator(userPoint.point(), amount);
        UserPoint resultUserPoint = insertUserPointOrUpdate(userPoint.id(), usedPoint);
        return resultUserPoint;
    }

    private long usedPointCalculator(long userPoint, long minusAmount) {
        return userPoint - minusAmount;
    }

    private UserPoint insertUserPointOrUpdate(long id, long amount) {
        return pointRepository.insertOrUpdate(id, amount);
    }

    private UserPoint selectUserPointById(long id) {
        return pointRepository.selectById(id);
    }

    private PointHistory insertPointHistory(long id, long amount, TransactionType type) {
        return pointHistoryRepository.insert(id, amount, type, System.currentTimeMillis() % 1000);
    }
}
