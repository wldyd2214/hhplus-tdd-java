package io.hhplus.tdd.api.controller.point.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PointChargeRequest {

    private long amount;

    @Builder
    public PointChargeRequest(long amount) {
        this.amount = amount;
    }
}
