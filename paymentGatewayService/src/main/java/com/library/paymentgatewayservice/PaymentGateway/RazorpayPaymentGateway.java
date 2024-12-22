package com.library.paymentgatewayservice.PaymentGateway;


import com.razorpay.Order;
import com.razorpay.PaymentLink;
import org.json.JSONObject;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Primary
public class RazorpayPaymentGateway implements PaymentGateway{

    private  RazorpayClient razorpayClient;

    public RazorpayPaymentGateway(RazorpayClient razorpayClient) {
        this.razorpayClient = razorpayClient;
    }

    @Override
    public String generatePaymentLink(Long orderId, Long amount) throws RazorpayException {
        JSONObject paymentLinkRequest = new JSONObject();
        paymentLinkRequest.put("amount",amount*100);
        paymentLinkRequest.put("currency","INR");
        paymentLinkRequest.put("expire_by", System.currentTimeMillis() + 10 * 60 * 1000);
        paymentLinkRequest.put("reference_id", orderId.toString());
        paymentLinkRequest.put("description","Payment for orderID"+ orderId.toString());

        JSONObject customer = new JSONObject();
        customer.put("OrderId", orderId);
        paymentLinkRequest.put("customer",customer);

        JSONObject notify = new JSONObject();
        notify.put("sms",true);
        notify.put("email",true);
        paymentLinkRequest.put("notify",notify);
        paymentLinkRequest.put("reminder_enable",true);
        JSONObject notes = new JSONObject();
        paymentLinkRequest.put("notes",notes);
        paymentLinkRequest.put("callback_url","https://www.scaler.com/academy/mentee-dashboard/todos");
        paymentLinkRequest.put("callback_method","get");

        PaymentLink payment = razorpayClient.paymentLink.create(paymentLinkRequest);
        return payment.toString();
    }
}
