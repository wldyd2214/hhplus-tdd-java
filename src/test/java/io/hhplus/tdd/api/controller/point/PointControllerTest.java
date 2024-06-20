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

    // 컨트롤러 테스트 항목들에 대한 코드 리뷰를 받고 싶습니다.

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected PointService pointService;

    @MockBean
    protected PointHistoryService pointHistoryService;

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