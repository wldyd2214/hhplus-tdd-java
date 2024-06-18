package io.hhplus.tdd.api.controller.point;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.hhplus.tdd.api.service.point.PointService;
import io.hhplus.tdd.point.UserPoint;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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

    @DisplayName("특정 유저의 포인트를 충전한다.")
    @Test
    void charge() throws Exception {
        // given
        Long userId = 1L;
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
        Long userId = 1L;
        long amount = -1L;

        // when // then
        mockMvc.perform(
                       patch("/point/{id}/charge", userId)
                               .content(String.valueOf(amount))
                               .contentType(MediaType.APPLICATION_JSON)
               )
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.status").value("400"))
               .andExpect(jsonPath("$.error").value("Bad Request"));
    }
}