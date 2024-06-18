package io.hhplus.tdd.point;

public interface PointRepository {
    UserPoint insertOrUpdate(long id, long amount);
    UserPoint selectById(Long id);
}
