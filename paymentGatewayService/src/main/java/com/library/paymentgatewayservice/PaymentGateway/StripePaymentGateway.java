package com.library.paymentgatewayservice.PaymentGateway;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentLink;
import com.stripe.model.Price;
import com.stripe.param.PaymentLinkCreateParams;
import com.stripe.param.PriceCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
//@Primary
public class StripePaymentGateway implements PaymentGateway {
    @Value("${STRIPE_KEY_SECRET}")
    private String stripeSecretKey;

    @Override
    public String generatePaymentLink(Long orderId, Long amount) throws StripeException {
        Stripe.apiKey = stripeSecretKey;

        Map<String, String> metadata = new HashMap<>();
        metadata.put("order_id", orderId.toString());
        metadata.put("amount", amount.toString());
        metadata.put("product_name", "Gold Plan");

        PaymentLinkCreateParams params =
                PaymentLinkCreateParams.builder()
                        .setPaymentIntentData(
                                PaymentLinkCreateParams.PaymentIntentData.builder()
                                        .putAllMetadata(metadata)  // This will ensure metadata is passed to PaymentIntent
                                        .build()
                        )
                        .addLineItem(
                                PaymentLinkCreateParams.LineItem.builder()
                                        .setPrice(createPrice(amount, metadata).getId())
                                        .setQuantity(1L)
                                        .build()
                        )
                        .setAfterCompletion(
                                PaymentLinkCreateParams.AfterCompletion.builder()
                                        .setRedirect(
                                                PaymentLinkCreateParams.AfterCompletion.Redirect.builder()
                                                        .setUrl("http://google.com")
                                                        .build()
                                        )
                                        .setType(PaymentLinkCreateParams.AfterCompletion.Type.REDIRECT)
                                        .build()
                        )
                        .putAllMetadata(metadata)
                        .build();

        PaymentLink paymentLink = PaymentLink.create(params);
        return paymentLink.getUrl();
    }

    private Price createPrice(Long amount, Map<String, String> metadata) throws StripeException {
        PriceCreateParams priceParams =
                PriceCreateParams.builder()
                        .setCurrency("INR")
                        .setUnitAmount(amount * 100)
                        .setProductData(
                                PriceCreateParams.ProductData.builder()
                                        .setName("Gold Plan")
                                        .putAllMetadata(metadata)
                                        .build()
                        )
                        .putAllMetadata(metadata)
                        .build();

        return Price.create(priceParams);
    }
}