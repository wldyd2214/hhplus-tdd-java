package io.hhplus.tdd.api.service.point;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.PointHistoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PointHistoryService {

    private final PointHistoryRepository pointHistoryRepository;

    public List<PointHistory> getUserPointHistory(long id) {
        return selectPointHistoryByUserId(id);
    }

    private List<PointHistory> selectPointHistoryByUserId(long id) {
        return pointHistoryRepository.selectAllByUserId(id);
    }
}
