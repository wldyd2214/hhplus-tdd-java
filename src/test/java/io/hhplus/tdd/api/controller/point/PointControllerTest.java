package io.hhplus.tdd.api.controller.point;

import static io.hhplus.tdd.point.TransactionType.CHARGE;
import static io.hhplus.tdd.point.TransactionType.USE;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.hhplus.tdd.api.service.point.PointHistoryService;
import io.hhplus.tdd.api.service.point.PointService;
import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PointController.class)
class PointControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected PointService pointService;

    @MockBean
    protected PointHistoryService pointHistoryService;

    // 컨트롤러 단위 테스트의 경우, jsonPath 로 각 항목들을 점검하게 되면 개발자의 실수에 의해 누락값이 존재할 수 있고 누락된 경우,
    // 나머지 검증값이 같다면 테스트 통과로 이어지는 불상사가 발생해요 ( Edge Case 가 존재하지만 테스트는 통과해 서비스의 오류로 이어질 수 있음 )

    @DisplayName("특정 유저의 포인트를 충전한다.")
    @Test
    void charge() throws Exception {
        // given
        long userId = 1;
        long amount = 1000;
        long updateMillis = System.currentTimeMillis();

        UserPoint mockUser = new UserPoint(userId, amount, updateMillis);
        given(pointService.chargeUserPoint(userId, amount)).willReturn(mockUser);

        // when // then
        mockMvc.perform(
                   patch("/point/{id}/charge", userId)
                       .content(String.valueOf(amount))
                       .contentType(MediaType.APPLICATION_JSON)
               )
               .andDo(print())
               .andExpectAll(
                   status().isOk(),
                   jsonPath("$.code").value("200"),
                   jsonPath("$.status").value("OK"),
                   jsonPath("$.message").value("OK"),
                   jsonPath("$.data.id").value(userId),
                   jsonPath("$.data.point").value(amount),
                   jsonPath("$.data.updateMillis").value(updateMillis)
               );
    }

    @DisplayName("음수 값을 충전하려는 경우 예외가 발생한다.")
    @Test
    void chargeWithNegative() throws Exception {
        // given
        long userId = 1;
        long amount = -1;

        // when // then
        mockMvc.perform(
                       patch("/point/{id}/charge", userId)
                               .content(String.valueOf(amount))
                               .contentType(MediaType.APPLICATION_JSON)
               )
               .andDo(print())
               .andExpectAll(
                   status().isBadRequest(),
                   jsonPath("$.code").value("400"),
                   jsonPath("$.status").value("BAD_REQUEST"),
                   jsonPath("$.message").value("양수의 값만 충전할 수 있습니다."),
                   jsonPath("$.data").isEmpty()
               );
    }

    @DisplayName("특정 유저의 포인트를 조회한다.")
    @Test
    void point() throws Exception {
        // given
        long userId = 1;
        long amount = 1000;
        long updateMillis = System.currentTimeMillis();

        UserPoint mockUser = new UserPoint(userId, amount, updateMillis);
        given(pointService.getUserPoint(userId)).willReturn(mockUser);

        // when // then
        mockMvc.perform(
                   get("/point/{id}", userId)
                       .contentType(MediaType.APPLICATION_JSON)
               )
               .andDo(print())
               .andExpectAll(
                   status().isOk(),
                   jsonPath("$.code").value("200"),
                   jsonPath("$.status").value("OK"),
                   jsonPath("$.message").value("OK"),
                   jsonPath("$.data.id").value(userId),
                   jsonPath("$.data.point").value(amount),
                   jsonPath("$.data.updateMillis").value(updateMillis)
               );
    }

    @DisplayName("특정 유저의 포인트를 사용한다.")
    @Test
    void use() throws Exception {
        // given
        long userId = 1;
        long amount = 1000;
        long updateMillis = System.currentTimeMillis();
        long point = 0;

        UserPoint mockUser = new UserPoint(userId, point, updateMillis);
        given(pointService.userPointUse(userId, amount)).willReturn(mockUser);

        // when // then
        mockMvc.perform(
                   patch("/point/{id}/use", userId)
                       .content(String.valueOf(amount))
                       .contentType(MediaType.APPLICATION_JSON)
               )
               .andDo(print())
               .andExpectAll(
                   status().isOk(),
                   jsonPath("$.code").value("200"),
                   jsonPath("$.status").value("OK"),
                   jsonPath("$.message").value("OK"),
                   jsonPath("$.data.id").value(userId),
                   jsonPath("$.data.point").value(point),
                   jsonPath("$.data.updateMillis").value(updateMillis)
               );
    }

    @DisplayName("특정 유저의 포인트 충전/이용 내역을 조회한다.")
    @Test
    void history() throws Exception {
        // given
        long userId = 1;
        long updateMillis = System.currentTimeMillis();

        List<PointHistory> mockPointHistories = List.of(
            createPointHistory(1, userId, 1000, CHARGE, updateMillis),
            createPointHistory(2, userId, 500, USE, updateMillis),
            createPointHistory(3, userId, 500, CHARGE, updateMillis)
        );

        given(pointHistoryService.getUserPointHistory(userId)).willReturn(mockPointHistories);

        // when // then
        mockMvc.perform(
                   get("/point/{id}/histories", userId)
                       .contentType(MediaType.APPLICATION_JSON)
               )
               .andDo(print())
               .andExpectAll(
                   status().isOk(),
                   jsonPath("$.code").value("200"),
                   jsonPath("$.status").value("OK"),
                   jsonPath("$.message").value("OK"),
                   jsonPath("$.data.[0].id").value(1),
                   jsonPath("$.data.[0].userId").value(userId),
                   jsonPath("$.data.[0].amount").value(1000),
                   jsonPath("$.data.[0].type").value(CHARGE.name()),
                   jsonPath("$.data.[0].updateMillis").value(updateMillis)
               );
    }

    private PointHistory createPointHistory(long id, long userid, long amount, TransactionType type, long updateMillis) {
        return new PointHistory(id, userid, amount, type, updateMillis);
    }
}