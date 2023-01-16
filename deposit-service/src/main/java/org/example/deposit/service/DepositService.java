package org.example.deposit.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.deposit.controller.dto.DepositRequestDto;
import org.example.deposit.controller.dto.DepositResponseDto;
import org.example.deposit.entity.Deposit;
import org.example.deposit.exception.DepositServiceException;
import org.example.deposit.repository.DepositRepository;
import org.example.deposit.rest.dto.AccountResponseDto;
import org.example.deposit.rest.AccountServiceClient;
import org.example.deposit.rest.dto.BillRequestDto;
import org.example.deposit.rest.dto.BillResponseDto;
import org.example.deposit.rest.BillServiceClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class DepositService {
    private static final String TOPIC_EXCHANGE_DEPOSIT = "js.deposit.notify.exchange";
    private static final String ROUTING_KEY_DEPOSIT = "js.key.deposit";
    private final DepositRepository depositRepository;
    private final AccountServiceClient accountServiceClient;
    private final BillServiceClient billServiceClient;
    private final RabbitTemplate rabbitTemplate;

    public DepositResponseDto deposit(DepositRequestDto depositRequestDto) {
        DepositResponseDto depositResponseDto = new DepositResponseDto();
        Long accountId = depositRequestDto.getAccountId();
        Long billId = depositRequestDto.getBillId();
        BigDecimal amount = depositRequestDto.getAmount();

        if (accountId == null && billId == null) {
            throw new DepositServiceException("Account is null and bill is null");
        }

        if (billId != null) {

            BillResponseDto billResponseDto = billServiceClient.getBillById(billId);
            BillRequestDto billRequestDto = buildBillRequest(amount, billResponseDto);
            billServiceClient.updateBill(billId, billRequestDto);

            AccountResponseDto accountResponseDto = accountServiceClient.getAccountById(billResponseDto.getAccountId());
            Deposit deposit = buildDeposit(billId, depositRequestDto, accountResponseDto);
            depositRepository.save(deposit);

            depositResponseDto = createResponse(amount, accountResponseDto);
        }

        if (accountId != null) {

            BillResponseDto defaultBillResponseDto = getDefaultBill(accountId);

            BillRequestDto billRequestDto = buildBillRequest(amount, defaultBillResponseDto);
            billServiceClient.updateBill(defaultBillResponseDto.getBillId(), billRequestDto);

            AccountResponseDto accountResponseDto = accountServiceClient.getAccountById(accountId);
            Deposit deposit = buildDeposit(defaultBillResponseDto.getBillId(), amount, accountResponseDto);
            depositRepository.save(deposit);

            depositResponseDto = createResponse(amount, accountResponseDto);
        }

        return depositResponseDto;
    }

    private BillRequestDto buildBillRequest(BigDecimal amount, BillResponseDto billResponseDTO) {

        return BillRequestDto.builder()
                .accountId(billResponseDTO.getAccountId())
                .creationDate(billResponseDTO.getCreationDate())
                .isDefault(billResponseDTO.getIsDefault())
                .overdraftEnabled(billResponseDTO.getOverdraftEnabled())
                .amount(billResponseDTO.getAmount().add(amount))
                .build();
    }

    private Deposit buildDeposit(Long billId, DepositRequestDto depositRequestDto, AccountResponseDto accountResponseDto) {

        return Deposit.builder()
                .billId(billId)
                .amount(depositRequestDto.getAmount())
                .creationDate(OffsetDateTime.now())
                .email(accountResponseDto.getEmail())
                .build();
    }

    private Deposit buildDeposit(Long billId, BigDecimal amount, AccountResponseDto accountResponseDto) {

        return Deposit.builder()
                .billId(billId)
                .amount(amount)
                .creationDate(OffsetDateTime.now())
                .email(accountResponseDto.getEmail())
                .build();
    }

    private DepositResponseDto createResponse(BigDecimal amount, AccountResponseDto accountResponseDto) {

        DepositResponseDto depositResponseDto = DepositResponseDto.builder()
                .amount(amount)
                .email(accountResponseDto.getEmail())
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            rabbitTemplate.convertAndSend(TOPIC_EXCHANGE_DEPOSIT,
                    ROUTING_KEY_DEPOSIT,
                    objectMapper.writeValueAsString(depositResponseDto));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new DepositServiceException("Can't send message to RabbitMQ");
        }

        return depositResponseDto;
    }

    private BillResponseDto getDefaultBill(Long accountId) {

        return billServiceClient.getBillsByAccountId(accountId).stream()
                .filter(BillResponseDto::getIsDefault)
                .findAny()
                .orElseThrow(() -> new DepositServiceException("Unable to find default bill for account: " + accountId));
    }
}
