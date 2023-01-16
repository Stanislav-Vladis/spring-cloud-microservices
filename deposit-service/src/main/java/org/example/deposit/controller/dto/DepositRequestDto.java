package org.example.deposit.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@ToString
@NoArgsConstructor
public class DepositRequestDto {
    private Long accountId;
    private Long billId;
    private BigDecimal amount;
}
