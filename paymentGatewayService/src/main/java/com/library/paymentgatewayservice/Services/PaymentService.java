package com.library.paymentgatewayservice.Services;

import com.library.paymentgatewayservice.PaymentGateway.PaymentGateway;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    private PaymentGatewaySelector paymentGatewaySelector;

    public PaymentService(PaymentGatewaySelector paymentGatewaySelector) {
        this.paymentGatewaySelector = paymentGatewaySelector;
    }

    public String initiatePayment(Long orderId, Long amount) throws RazorpayException, StripeException {
       return paymentGatewaySelector
               .get()
               .generatePaymentLink(orderId, amount);
    }
}