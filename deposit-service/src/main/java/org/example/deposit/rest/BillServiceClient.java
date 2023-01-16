package org.example.deposit.rest;

import org.example.deposit.rest.dto.BillRequestDto;
import org.example.deposit.rest.dto.BillResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "bill-service")
public interface BillServiceClient {

    @RequestMapping(value = "bills/{billId}", method = RequestMethod.PUT)
    void updateBill(@PathVariable("billId") Long billId, BillRequestDto billRequestDTO);

    @RequestMapping(value = "bills/{billId}", method = RequestMethod.GET)
    BillResponseDto getBillById(@PathVariable("billId") Long billId);

    @RequestMapping(value = "bills/account/{accountId}", method = RequestMethod.GET)
    List<BillResponseDto> getBillsByAccountId(@PathVariable("accountId") Long accountId);
}
