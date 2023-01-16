package org.example.account.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor
public class AccountRequestDto {
    private String name;
    private String email;
    private String phone;
    private List<Long> bills;
    private OffsetDateTime creationDate;
}
