package io.hhplus.tdd.point;

import io.hhplus.tdd.database.UserPointTable;
import org.springframework.stereotype.Repository;

@Repository
public class PointRepositoryImpl implements PointRepository {
    UserPointTable userPointTable = new UserPointTable();

    @Override
    public UserPoint insertOrUpdate(long id, long amount) {
        return userPointTable.insertOrUpdate(id, amount);
    }

    @Override
    public UserPoint selectById(Long id) {
        return userPointTable.selectById(id);
    }
}
