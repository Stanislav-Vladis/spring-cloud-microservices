package org.example.bill.service;

import lombok.RequiredArgsConstructor;
import org.example.bill.controller.dto.BillRequestDto;
import org.example.bill.entity.Bill;
import org.example.bill.exception.BillNotFoundException;
import org.example.bill.repository.BillRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BillService {
    private final BillRepository billRepository;

    public Long createBill(BillRequestDto billRequestDto) {
        Bill bill = Bill.builder()
                .accountId(billRequestDto.getAccountId())
                .amount(billRequestDto.getAmount())
                .isDefault(billRequestDto.getIsDefault())
                .creationDate(OffsetDateTime.now())
                .overdraftEnabled(billRequestDto.getOverdraftEnabled())
                .build();
        return billRepository.save(bill).getBillId();
    }

    public Bill updateBill(Long billId, BillRequestDto billRequestDto) {
        Bill bill = Bill.builder()
                .billId(billId)
                .accountId(billRequestDto.getAccountId())
                .amount(billRequestDto.getAmount())
                .isDefault(billRequestDto.getIsDefault())
                .creationDate(OffsetDateTime.now())
                .overdraftEnabled(billRequestDto.getOverdraftEnabled())
                .build();
        return billRepository.save(bill);
    }

    public Bill getBillById(Long billId) {
        return billRepository.findById(billId)
                .orElseThrow(() -> new BillNotFoundException("Unable to find bill with id: " + billId));
    }

    public Bill deleteBill(Long billId) {
        Bill deletedBill = getBillById(billId);
        billRepository.deleteById(billId);
        return deletedBill;
    }

    public List<Bill> getBillsByAccountId(Long accountId) {
        return billRepository.getBillsByAccountId(accountId);
    }
}
