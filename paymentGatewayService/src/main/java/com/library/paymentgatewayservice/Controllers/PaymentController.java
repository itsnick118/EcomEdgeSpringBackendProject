package com.library.paymentgatewayservice.Controllers;

import com.library.paymentgatewayservice.DTO.InitiatePaymentRequestDto;
import com.library.paymentgatewayservice.PaymentGateway.StripePaymentGateway;
import com.library.paymentgatewayservice.Services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private PaymentService paymentService;
    @Autowired
    private StripePaymentGateway stripePaymentGateway;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/")
    public String initiatePayment(@RequestBody InitiatePaymentRequestDto requestDto)  {
        try {
            return paymentService.initiatePayment(
                    requestDto.getOrderId(),
                    requestDto.getAmount()
            );
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occurred while initiating payment", e);
        }
    }
}
