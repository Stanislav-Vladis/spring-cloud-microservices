package org.example.deposit.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
}
