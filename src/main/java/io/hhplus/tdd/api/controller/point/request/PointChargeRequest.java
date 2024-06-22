package io.hhplus.tdd.api.controller.point.request;

import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PointChargeRequest {

    @Positive(message = "양수의 값만 충전할 수 있습니다.")
    private long amount;

    @Builder
    public PointChargeRequest(long amount) {
        this.amount = amount;
    }
}
