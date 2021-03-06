package com.kakaopay.throwmoney.web;

import com.kakaopay.throwmoney.service.EventMoneyService;
import com.kakaopay.throwmoney.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/api/kakaomoney")
@RequiredArgsConstructor
@RestController
public class EventMoneyApiController {

    private final EventMoneyService eventMoneyService;

    @PostMapping("/")
    public ResponseThrowMoneyDto throwMoney(@RequestBody @Valid RequestThrowMoneyDto params, @RequestHeader(value = "X-USER-ID") Long userId, @RequestHeader("X-ROOM-ID") String roomId) {
        return eventMoneyService.distributeMoney(params, userId, roomId);
    }

    @PutMapping("/")
    public ResponseReceiveMoneyDto receiveMoney(@RequestBody @Valid RequestReceiveMoneyDto params, @RequestHeader(value = "X-USER-ID") Long userId, @RequestHeader("X-ROOM-ID") String roomId) {
        return eventMoneyService.receiveMoney(params, userId, roomId);
    }

    @GetMapping("/")
    public ResponseEventMoneyDto getEventMoneyList(@RequestBody @Valid RequestEventMoneyDto params, @RequestHeader(value = "X-USER-ID") Long userId, @RequestHeader("X-ROOM-ID") String roomId) {
        return eventMoneyService.getEventMoneyList(params, userId, roomId);
    }
}
