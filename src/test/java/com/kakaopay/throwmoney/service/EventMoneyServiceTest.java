package com.kakaopay.throwmoney.service;

import com.kakaopay.throwmoney.domain.member.Member;
import com.kakaopay.throwmoney.domain.money.*;
import com.kakaopay.throwmoney.domain.money.strategy.GenerateTokenStrategy;
import com.kakaopay.throwmoney.web.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventMoneyServiceTest {

    @InjectMocks
    private EventMoneyService eventMoneyService;

    @Mock
    private EventMoneyRepository eventMoneyRepository;

    @Mock
    private MoneyRepository moneyRepository;

    @Mock
    private GenerateTokenStrategy generateTokenStrategy;

    private Long userId;
    private String roomId;
    private String token;
    private Long createId;
    private RequestReceiveMoneyDto requestReceiveMoneyDto;
    private RequestEventMoneyDto requestEventMoneyDto;
    private EventMoney eventMoney;

    private Money money;

    @BeforeEach
    void setUp() {
        userId = 1L;
        roomId = "room1";
        token = "abc";
        createId = 3L;

        requestReceiveMoneyDto = RequestReceiveMoneyDto.builder()
                .token(token)
                .build();

        requestEventMoneyDto = RequestEventMoneyDto.builder()
                .token(token)
                .build();

        money = Money.builder()
                .totalAmount(10000L)
                .member(null)
                .createId(createId)
                .modifyId(createId)
                .build();

        eventMoney = EventMoney.builder()
                .money(money)
                .eventStatus(EventStatus.WAITING)
                .amount(1000L)
                .expiredDate(LocalDateTime.now().plusMinutes(30))
                .token(token)
                .roomId(roomId)
                .eventType(EventType.THROW)
                .createId(createId)
                .modifyId(createId)
                .build();
    }

    @Test
    void throwMoneyTest() {

        RequestThrowMoneyDto dto = RequestThrowMoneyDto.builder()
                .numberOfTarget(5)
                .price(10000L)
                .build();


        when(moneyRepository.findByMemberId(userId)).thenReturn(Optional.of(new Money(10000L, new Member("라이언"), null, null)));
        when(generateTokenStrategy.generate()).thenReturn(token);

        ResponseThrowMoneyDto result = ResponseThrowMoneyDto.builder()
                .token(token)
                .build();

        ResponseThrowMoneyDto responseThrowMoneyDto = eventMoneyService.distributeMoney(dto, userId, roomId);
        assertThat(responseThrowMoneyDto).isEqualToComparingFieldByField(result);
    }

    @Test
    void receiveMoneyTest() {

        when(eventMoneyRepository.findTopByTokenAndEventStatusAndEventTypeOrderByIdDesc(token, EventStatus.WAITING, EventType.THROW)).thenReturn(Optional.of(eventMoney));
        when(moneyRepository.findByMemberId(userId)).thenReturn(Optional.of(Money.builder().totalAmount(9000L)
                .member(null)
                .createId(userId)
                .modifyId(userId)
                .build()));

        ResponseReceiveMoneyDto responseReceiveMoneyDto = eventMoneyService.receiveMoney(requestReceiveMoneyDto, userId, roomId);
        assertThat(responseReceiveMoneyDto.getPrice()).isEqualTo(eventMoney.getPrice());
    }

    @Test
    @DisplayName("본인이 뿌리기 한 머니 받기 시도 시 예외발생")
    void receiveMoneyOneSelfException1Test() {

        eventMoney = EventMoney.builder()
                .money(money)
                .eventStatus(EventStatus.WAITING)
                .amount(1000L)
                .expiredDate(LocalDateTime.now().plusMinutes(30))
                .token(token)
                .roomId(roomId)
                .eventType(EventType.THROW)
                .createId(createId)
                .modifyId(createId)
                .build();

        when(eventMoneyRepository.findTopByTokenAndEventStatusAndEventTypeOrderByIdDesc(token, EventStatus.WAITING, EventType.THROW)).thenReturn(Optional.of(eventMoney));
        assertThatThrownBy(() -> eventMoneyService.receiveMoney(requestReceiveMoneyDto, createId, roomId)).isInstanceOf(IllegalArgumentException.class).hasMessage("본인이 뿌리기한 머니는 받을 수 없습니다.");
    }

    @Test
    @DisplayName("유효기간 지난 머니 받기 시도 시 예외발생")
    void receiveMoneyAfterExpiredDateExceptionTest() {

        eventMoney = EventMoney.builder()
                .money(money)
                .eventStatus(EventStatus.WAITING)
                .amount(1000L)
                .expiredDate(LocalDateTime.now().minusMinutes(10))
                .token(token)
                .roomId(roomId)
                .eventType(EventType.THROW)
                .createId(createId)
                .modifyId(createId)
                .build();

        when(eventMoneyRepository.findTopByTokenAndEventStatusAndEventTypeOrderByIdDesc(token, EventStatus.WAITING, EventType.THROW)).thenReturn(Optional.of(eventMoney));
        assertThatThrownBy(() -> eventMoneyService.receiveMoney(requestReceiveMoneyDto, userId, roomId)).isInstanceOf(IllegalStateException.class).hasMessage("유효기간 10분이 지난 머니입니다.");
    }

    @Test
    @DisplayName("참여하지 않은 룸의 머니 받기 시도 시 예외발생")
    void receiveMoneyNotJoinedRoomExceptionTest() {

        String otherRoomId = "room3";

        eventMoney = EventMoney.builder()
                .money(money)
                .eventStatus(EventStatus.WAITING)
                .amount(1000L)
                .expiredDate(LocalDateTime.now().plusMinutes(10))
                .token(token)
                .roomId(roomId)
                .eventType(EventType.THROW)
                .createId(createId)
                .modifyId(createId)
                .build();

        when(eventMoneyRepository.findTopByTokenAndEventStatusAndEventTypeOrderByIdDesc(token, EventStatus.WAITING, EventType.THROW)).thenReturn(Optional.of(eventMoney));
        assertThatThrownBy(() -> eventMoneyService.receiveMoney(requestReceiveMoneyDto, userId, otherRoomId)).isInstanceOf(IllegalStateException.class).hasMessage("룸에 소속된 사용자가 아닙니다.");
    }

    @Test
    @DisplayName("이미 받은 머니 다시 받기 시도 시 예외발생")
    void receiveMoneyAlreadyReceiveExceptionTest() {

        eventMoney = EventMoney.builder()
                .money(money)
                .eventStatus(EventStatus.DONE)
                .amount(1000L)
                .expiredDate(LocalDateTime.now().plusMinutes(10))
                .token(token)
                .roomId(roomId)
                .eventType(EventType.THROW)
                .createId(createId)
                .modifyId(createId)
                .build();

        when(eventMoneyRepository.findTopByTokenAndEventStatusAndEventTypeOrderByIdDesc(token, EventStatus.WAITING, EventType.THROW)).thenReturn(Optional.of(eventMoney));
        assertThatThrownBy(() -> eventMoneyService.receiveMoney(requestReceiveMoneyDto, userId, roomId)).isInstanceOf(IllegalStateException.class).hasMessage("이미 받은 머니입니다.");
    }

    @Test
    @DisplayName("유효하지 않은 토큰 입력 시 예외발생")
    void getMoneyValidateTokenExceptionTest() {
        when(eventMoneyRepository.existsByToken(token)).thenReturn(false);
        assertThatThrownBy(() -> eventMoneyService.getEventMoneyList(requestEventMoneyDto, userId, roomId)).isInstanceOf(IllegalArgumentException.class).hasMessage("유효하지 않은 토큰입니다.");
    }

    @Test
    @DisplayName("다른 사람 머니 조회 시 예외발생")
    void hasOtherMoneyExceptionTest() {
        eventMoney = EventMoney.builder()
                .money(money)
                .eventStatus(EventStatus.DONE)
                .amount(1000L)
                .expiredDate(LocalDateTime.now().plusMinutes(10))
                .token(token)
                .roomId(roomId)
                .eventType(EventType.THROW)
                .createId(createId)
                .modifyId(createId)
                .build();


        List<EventMoney> eventMoneyList = Collections.singletonList(eventMoney);
        when(eventMoneyRepository.existsByToken(token)).thenReturn(true);
        when(eventMoneyRepository.findAllByTokenAndEventStatusAndSearchableDateAfter(token, EventStatus.DONE, LocalDateTime.now())).thenReturn(Optional.of(eventMoneyList));
        assertThatThrownBy(() -> eventMoneyService.getEventMoneyList(requestEventMoneyDto, userId, roomId)).isInstanceOf(IllegalArgumentException.class).hasMessage("다른 사람의 뿌리기 머니를 조회할 수 없습니다.");
    }


}