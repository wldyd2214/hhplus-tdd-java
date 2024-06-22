package io.hhplus.tdd.api.controller.point;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.hhplus.tdd.api.service.point.PointHistoryService;
import io.hhplus.tdd.api.service.point.PointService;
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
        long userId = 1L;
        long amount = 1000L;

        // when // then
        mockMvc.perform(
                   patch("/point/{id}/charge", userId)
                       .content(String.valueOf(amount))
                       .contentType(MediaType.APPLICATION_JSON)
               )
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.code").value("200"))
               .andExpect(jsonPath("$.status").value("OK"))
               .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("음수 값을 충전하려는 경우 예외가 발생한다.")
    @Test
    void chargeWithNegative() throws Exception {
        // given
        long userId = 1L;
        long amount = -1L;

        // when // then
        mockMvc.perform(
                       patch("/point/{id}/charge", userId)
                               .content(String.valueOf(amount))
                               .contentType(MediaType.APPLICATION_JSON)
               )
               .andDo(print())
               .andExpect(status().isBadRequest());
    }

    @DisplayName("특정 유저의 포인트를 조회한다.")
    @Test
    void point() throws Exception {
        // given
        long userId = 1L;

        // when // then
        mockMvc.perform(
                   get("/point/{id}", userId)
                       .contentType(MediaType.APPLICATION_JSON)
               )
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.code").value("200"))
               .andExpect(jsonPath("$.status").value("OK"))
               .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("특정 유저의 포인트를 사용한다.")
    @Test
    void use() throws Exception {
        // given
        long userId = 1L;
        long amount = 1000L;

        // when // then
        mockMvc.perform(
                   patch("/point/{id}/use", userId)
                       .content(String.valueOf(amount))
                       .contentType(MediaType.APPLICATION_JSON)
               )
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.code").value("200"))
               .andExpect(jsonPath("$.status").value("OK"))
               .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("특정 유저의 포인트 충전/이용 내역을 조회한다.")
    @Test
    void history() throws Exception {
        // given
        long userId = 1L;

        // when // then
        mockMvc.perform(
                   get("/point/{id}/histories", userId)
                       .contentType(MediaType.APPLICATION_JSON)
               )
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.code").value("200"))
               .andExpect(jsonPath("$.status").value("OK"))
               .andExpect(jsonPath("$.message").value("OK"));
    }
}