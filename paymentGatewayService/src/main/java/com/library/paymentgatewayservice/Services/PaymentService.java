package com.library.paymentgatewayservice.Services;

import com.library.paymentgatewayservice.PaymentGateway.PaymentGateway;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    private PaymentGateway paymentGateway;

    public PaymentService(PaymentGateway paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    public String initiatePayment(Long orderId, double amount) throws RazorpayException, StripeException {
        long roundedAmount = (long) amount;
        return paymentGateway.generatePaymentLink(orderId, roundedAmount);
    }
}