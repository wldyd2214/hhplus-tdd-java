package io.hhplus.tdd.api.controller.point;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.hhplus.tdd.api.controller.point.request.PointChargeRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PointController.class)
class PointControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @DisplayName("특정 유저의 포인트를 충전한다.")
    @Test
    void charge() throws Exception {
        // given
        PointChargeRequest request = PointChargeRequest.builder()
                                                       .amount(Long.valueOf(1000))
                                                       .build();

        // when // then
        mockMvc.perform(
                    patch("/point/{id}/charge", 1)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
               .andDo(print())
               .andExpect(status().isOk());
    }
}