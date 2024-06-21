package io.hhplus.tdd.api.service.point;

import static io.hhplus.tdd.point.TransactionType.CHARGE;
import static io.hhplus.tdd.point.TransactionType.USE;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.PointHistoryRepository;
import io.hhplus.tdd.point.PointRepository;
import io.hhplus.tdd.point.UserPoint;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PointService {

    private final PointRepository pointRepository;

    private final PointHistoryRepository pointHistoryRepository;

    public UserPoint chargeUserPoint(long id, long amount) {
        return chargeUserPointProcess(id, amount);
    }

    public UserPoint getUserPoint(long id) {
        return selectUserPointById(id);
    }

    public UserPoint userPointUse(long id, long amount) {
        return userPointUsedProcess(id, amount);
    }

    private UserPoint chargeUserPointProcess(long id, long amount) {
        UserPoint userPoint = selectUserPointById(id);
        long sumPoint = userPoint.point() + amount;
        insertPointChargeHistory(id, amount);
        return insertUserPointOrUpdate(id, sumPoint);
    }

    // TODO: userPointUse 메소드에서 부터 호출되는 userPointUsedProcess 비지니스 로직의 클린 코드 리뷰를 받고 싶습니다.
    private UserPoint userPointUsedProcess(long id, long amount) {
        UserPoint userPoint = selectUserPointById(id);
        System.out.println(String.format("%d 유저의 %d 포인트가 존재하고 %d 만큼 사용하고 싶어요.", userPoint.point(), amount));
        if (userPoint.point() < amount)
            throw new IllegalArgumentException("보유 포인트가 부족합니다.");

        long usedPoint = usedPointCalculator(userPoint.point(), amount);

        UserPoint resultUserPoint = insertUserPointOrUpdate(userPoint.id(), usedPoint);
        insertPointUseHistory(id, amount);

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

    private PointHistory insertPointChargeHistory(long id, long amount) {
        return pointHistoryRepository.insert(id, amount, CHARGE, System.currentTimeMillis() % 1000);
    }

    private PointHistory insertPointUseHistory(long id, long amount) {
        return pointHistoryRepository.insert(id, amount, USE, System.currentTimeMillis() % 1000);
    }
}
