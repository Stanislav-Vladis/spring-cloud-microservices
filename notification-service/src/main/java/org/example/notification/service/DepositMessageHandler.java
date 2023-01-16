package org.example.notification.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.notification.config.RabbitMQConfig;
import org.example.notification.service.dto.DepositResponseDto;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DepositMessageHandler {
    private final JavaMailSender javaMailSender;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_DEPOSIT)
    public void receive(Message message) {

        DepositResponseDto depositResponseDto = messageMapToDepositResponseDto(message);
        sendMailMessage(depositResponseDto);
    }

    @SneakyThrows
    private DepositResponseDto messageMapToDepositResponseDto(Message message) {

        System.out.println(message);
        byte[] body = message.getBody();
        String jsonBody = new String(body);
        ObjectMapper objectMapper = new ObjectMapper();

        DepositResponseDto depositResponseDto = objectMapper.readValue(jsonBody, DepositResponseDto.class);
        System.out.println(depositResponseDto);
        return depositResponseDto;
    }

    @SneakyThrows
    private void sendMailMessage(DepositResponseDto depositResponseDto) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(depositResponseDto.getEmail());
        mailMessage.setFrom("lori@cat.xyz");
        mailMessage.setSubject("Deposit");
        mailMessage.setText("Make deposit, sum:" + depositResponseDto.getAmount());
        javaMailSender.send(mailMessage);
    }
}
