package io.hhplus.tdd.api.service.point;

import io.hhplus.tdd.point.UserPoint;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PointService {

    public UserPoint charge(long id, long amount, LocalDateTime registeredDateTime) {
        return null;
    }
}
