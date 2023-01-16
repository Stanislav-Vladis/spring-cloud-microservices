package org.example.deposit.controller;

import lombok.RequiredArgsConstructor;
import org.example.deposit.controller.dto.DepositRequestDto;
import org.example.deposit.controller.dto.DepositResponseDto;
import org.example.deposit.service.DepositService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DepositController {
    private final DepositService depositService;

    @PostMapping("/deposits")
    public DepositResponseDto deposit(@RequestBody DepositRequestDto depositRequestDto) {
        return depositService.deposit(depositRequestDto);
    }
}
