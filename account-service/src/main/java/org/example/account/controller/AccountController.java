package org.example.account.controller;

import lombok.RequiredArgsConstructor;
import org.example.account.controller.dto.AccountRequestDto;
import org.example.account.controller.dto.AccountResponseDto;
import org.example.account.service.AccountService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/")
    public Long createAccount(@RequestBody AccountRequestDto accountRequestDto) {
        return accountService.createAccount(accountRequestDto);
    }

    @PutMapping("/{accountId}")
    public AccountResponseDto updateAccount(@PathVariable Long accountId, @RequestBody AccountRequestDto accountRequestDto) {
        return AccountResponseDto.build(accountService.updateAccount(accountId, accountRequestDto));
    }

    @GetMapping("/{accountId}")
    public AccountResponseDto getAccount(@PathVariable Long accountId) {
        return AccountResponseDto.build(accountService.getAccountById(accountId));
    }

    @DeleteMapping("/{accountId}")
    public AccountResponseDto deleteAccount(@PathVariable Long accountId) {
        return AccountResponseDto.build(accountService.deleteAccount(accountId));
    }
}
