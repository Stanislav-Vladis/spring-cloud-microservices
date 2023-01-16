package org.example.account.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.example.account.entity.Account;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponseDto {
    private Long accountId;
    private String name;
    private String email;
    private List<Long> bills;
    private String phone;
    private OffsetDateTime creationDate;

    public static AccountResponseDto build(Account account) {
        return AccountResponseDto.builder()
                .accountId(account.getAccountId())
                .name(account.getName())
                .email(account.getEmail())
                .phone(account.getPhone())
                .bills(account.getBills())
                .creationDate(account.getCreationDate())
                .build();
    }
}
