package io.hhplus.tdd.api.service.point;

import io.hhplus.tdd.point.PointRepository;
import io.hhplus.tdd.point.UserPoint;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PointService {

    private final PointRepository pointRepository;

    public UserPoint charge(long id, long amount) {
        return UserPointCharge(id, amount);
    }

    public UserPoint UserPointCharge(long id, long amount) {
        return pointRepository.insertOrUpdate(id, amount);
    }
}
