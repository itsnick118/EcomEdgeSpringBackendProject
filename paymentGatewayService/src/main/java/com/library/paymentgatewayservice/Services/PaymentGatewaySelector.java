package com.library.paymentgatewayservice.Services;

import com.library.paymentgatewayservice.PaymentGateway.PaymentGateway;
import com.library.paymentgatewayservice.PaymentGateway.RazorpayPaymentGateway;
import com.library.paymentgatewayservice.PaymentGateway.StripePaymentGateway;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class PaymentGatewaySelector {
    private RazorpayPaymentGateway razorpayPaymentGateway;
    private StripePaymentGateway stripePaymentGateway;
    private static Random random = new Random();

    public PaymentGatewaySelector(RazorpayPaymentGateway razorpayPaymentGateway, StripePaymentGateway stripePaymentGateway) {
        this.razorpayPaymentGateway = razorpayPaymentGateway;
        this.stripePaymentGateway = stripePaymentGateway;
    }

    public PaymentGateway get() {
        int isEven = random.nextInt(100);
        if (isEven % 2 == 0) {
            return razorpayPaymentGateway;
        } else {
            return stripePaymentGateway;
        }
    }
}
