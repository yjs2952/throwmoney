package com.kakaopay.throwmoney.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.throwmoney.service.EventMoneyService;
import com.kakaopay.throwmoney.web.dto.RequestReceiveMoneyDto;
import com.kakaopay.throwmoney.web.dto.RequestThrowMoneyDto;
import com.kakaopay.throwmoney.web.dto.ResponseThrowMoneyDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class EventMoneyApiControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private EventMoneyService eventMoneyService;

    private RequestThrowMoneyDto throwMoneyParams;
    private RequestReceiveMoneyDto receiveMoneyParams;

    @BeforeEach
    void setUp() {
        throwMoneyParams = RequestThrowMoneyDto.builder()
                .price(10000L)
                .numberOfTarget(5)
                .build();

        receiveMoneyParams = RequestReceiveMoneyDto.builder()
                .token("aaa")
                .build();
    }

    @Test
    @DisplayName("머니 뿌리기 테스트 성공")
    void throwMoneyTest() throws Exception {

        // when
        mvc.perform(
                post("/api/kakaomoney/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(throwMoneyParams))
                        .header("X-USER-ID", 1L)
                        .header("X-ROOM-ID", "room1")
        )
                // then
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("user id 가 유효하지 않을 시 예외발생")
    void throwMoneyExceptionTest() throws Exception {

        // when
        mvc.perform(
                post("/api/kakaomoney/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(throwMoneyParams))
                        .header("X-USER-ID", 11L)
                        .header("X-ROOM-ID", "room1")
        )
                // then
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("머니 받기 테스트 성공")
    void receiveMoneyTest() throws Exception {

        // given
        Long createId = 1L;
        Long userId = 2L;
        String roomId = "room1";

        ResponseThrowMoneyDto responseThrowMoneyDto = eventMoneyService.distributeMoney(throwMoneyParams, createId, roomId);
        receiveMoneyParams.setToken(responseThrowMoneyDto.getToken());

        // when
        mvc.perform(
                put("/api/kakaomoney/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(receiveMoneyParams))
                        .header("X-USER-ID", userId)
                        .header("X-ROOM-ID", roomId)
        )
                // then
                .andExpect(status().isOk())
                .andDo(print());
    }
}