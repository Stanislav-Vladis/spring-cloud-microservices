package org.example.bill.controller;

import lombok.RequiredArgsConstructor;
import org.example.bill.controller.dto.BillRequestDto;
import org.example.bill.controller.dto.BillResponseDto;
import org.example.bill.service.BillService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class BillController {
    private final BillService billService;

    @PostMapping("/")
    public Long createBill(@RequestBody BillRequestDto billRequestDto) {
        return billService.createBill(billRequestDto);
    }

    @PutMapping("/{billId}")
    public BillResponseDto updateBill(@PathVariable Long billId, @RequestBody BillRequestDto billRequestDto) {
        return BillResponseDto.build(billService.updateBill(billId, billRequestDto));
    }

    @GetMapping("/{billId}")
    public BillResponseDto getBill(@PathVariable Long billId) {
        return BillResponseDto.build(billService.getBillById(billId));
    }

    @DeleteMapping("/{billId}")
    public BillResponseDto deleteBill(@PathVariable Long billId) {
        return BillResponseDto.build(billService.deleteBill(billId));
    }

    @GetMapping("/account/{accountId}")
    public List<BillResponseDto> getBillsByAccountId(@PathVariable Long accountId) {
        return billService.getBillsByAccountId(accountId).stream()
                .map(BillResponseDto::build)
                .collect(Collectors.toList());
    }
}
