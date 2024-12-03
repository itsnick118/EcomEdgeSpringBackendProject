package com.library.paymentgatewayservice.PaymentGateway;


import com.razorpay.PaymentLink;
import org.json.JSONObject;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.springframework.stereotype.Component;

@Component
//@Primary
public class RazorpayPaymentGateway implements PaymentGateway{

    private  RazorpayClient razorpayClient;

    public RazorpayPaymentGateway(RazorpayClient razorpayClient) {
        this.razorpayClient = razorpayClient;
    }

    @Override
    public String generatePaymentLink(Long orderId, Long amount) throws RazorpayException {
        //call to razorpay api to generate payment link
        JSONObject paymentLinkRequest = new JSONObject();
        paymentLinkRequest.put("amount",amount);
        paymentLinkRequest.put("currency","INR");
//        paymentLinkRequest.put("accept_partial",true);
//        paymentLinkRequest.put("first_min_partial_amount",100);
        paymentLinkRequest.put("expire_by",1691097057);
        paymentLinkRequest.put("reference_id",orderId.toString());
        paymentLinkRequest.put("description","Payment for orderID"+ orderId.toString());

        JSONObject customer = new JSONObject();
        customer.put("name","+919000090000");
        customer.put("contact","Gaurav Kumar");
        customer.put("email","gaurav.kumar@example.com");
        paymentLinkRequest.put("customer",customer);

        JSONObject notify = new JSONObject();
        notify.put("sms",true);
        notify.put("email",true);
        paymentLinkRequest.put("notify",notify);
        paymentLinkRequest.put("reminder_enable",true);
        JSONObject notes = new JSONObject();
        //notes.put("policy_name","Jeevan Bima");
        paymentLinkRequest.put("notes",notes);
        paymentLinkRequest.put("callback_url","https://www.scaler.com/academy/mentee-dashboard/todos");
        paymentLinkRequest.put("callback_method","get");

        PaymentLink payment = razorpayClient.paymentLink.create(paymentLinkRequest);
        return payment.toString();
    }
}
