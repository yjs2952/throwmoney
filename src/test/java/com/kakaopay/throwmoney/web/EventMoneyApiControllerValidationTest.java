package com.kakaopay.throwmoney.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.throwmoney.service.EventMoneyService;
import com.kakaopay.throwmoney.web.dto.RequestThrowMoneyDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@WebMvcTest
class EventMoneyApiControllerValidationTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private EventMoneyService eventMoneyService;

    @Autowired
    private ObjectMapper mapper;

    private Long userId;
    private String roomId;
    RequestThrowMoneyDto dto;

    @BeforeEach
    void setUp(){
        userId = 1L;
        roomId = "roomId";
    }

    @ParameterizedTest
    @CsvSource(value = {" ,4", "1000,0", "10,11"})
    @DisplayName("요청 파라미터 오류 테스트")
    void throw_Money_Null_Or_Empty_Exception_Test(Long price, Integer numberOfMembers) throws Exception {

        // given
        RequestThrowMoneyDto dto = RequestThrowMoneyDto.builder()
                .price(price)
                .numberOfTarget(numberOfMembers)
                .build();

        when(eventMoneyService.distributeMoney(dto, userId, roomId)).thenCallRealMethod();

        // when
        mvc.perform(
                post("/api/kakaomoney/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto))
                .header("X-USER-ID", userId)
                .header("X-ROOM-ID", roomId)
        )
        // then
        .andExpect(status().isBadRequest())
        .andDo(print());
    }
}