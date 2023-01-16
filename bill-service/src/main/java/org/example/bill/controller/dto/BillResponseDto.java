package org.example.bill.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.example.bill.entity.Bill;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BillResponseDto {
    private Long billId;
    private Long accountId;
    private BigDecimal amount;
    private Boolean isDefault;
    private OffsetDateTime creationDate;
    private Boolean overdraftEnabled;

    public static BillResponseDto build(Bill bill) {
        return BillResponseDto.builder()
                .billId(bill.getBillId())
                .accountId(bill.getAccountId())
                .amount(bill.getAmount())
                .isDefault(bill.getIsDefault())
                .creationDate(bill.getCreationDate())
                .overdraftEnabled(bill.getOverdraftEnabled())
                .build();
    }
}
