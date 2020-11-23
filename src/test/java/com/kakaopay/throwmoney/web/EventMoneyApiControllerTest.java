package com.kakaopay.throwmoney.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.throwmoney.web.dto.RequestThrowMoneyDto;
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

    static RequestThrowMoneyDto dto;

    @BeforeEach
    void setUp() {
        dto = RequestThrowMoneyDto.builder()
                .price(10000L)
                .numberOfTarget(5)
                .build();

    }

    @Test
    @DisplayName("머니 뿌리기 성공")
    void throwMoneyTest() throws Exception {

        // when
        mvc.perform(
                post("/api/kakaomoney/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto))
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
                        .content(mapper.writeValueAsString(dto))
                        .header("X-USER-ID", 11L)
                        .header("X-ROOM-ID", "room1")
        )
                // then
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}