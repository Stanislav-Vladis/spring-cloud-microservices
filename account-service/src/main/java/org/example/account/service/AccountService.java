package org.example.account.service;

import lombok.RequiredArgsConstructor;
import org.example.account.controller.dto.AccountRequestDto;
import org.example.account.entity.Account;
import org.example.account.exception.AccountNotFoundException;
import org.example.account.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public Long createAccount(AccountRequestDto accountRequestDto) {
        Account account = Account.builder()
                .name(accountRequestDto.getName())
                .email(accountRequestDto.getEmail())
                .phone(accountRequestDto.getPhone())
                .creationDate(OffsetDateTime.now())
                .bills(accountRequestDto.getBills())
                .build();
        return accountRepository.save(account).getAccountId();
    }

    public Account updateAccount(Long accountId, AccountRequestDto accountRequestDto) {
        Account account = Account.builder()
                .accountId(accountId)
                .name(accountRequestDto.getName())
                .email(accountRequestDto.getEmail())
                .phone(accountRequestDto.getPhone())
                .bills(accountRequestDto.getBills())
                .build();
        return accountRepository.save(account);
    }

    public Account getAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Unable to find account with id: " + accountId));
    }

    public Account deleteAccount(Long accountId) {
        Account deletedAccount = getAccountById(accountId);
        accountRepository.deleteById(accountId);
        return deletedAccount;
    }
}
