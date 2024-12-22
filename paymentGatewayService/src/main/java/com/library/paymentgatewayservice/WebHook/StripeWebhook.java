package com.library.paymentgatewayservice.WebHook;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonSyntaxException;
import com.library.paymentgatewayservice.DTO.OrderPaymentUpdateDTO;
import com.stripe.model.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/StripePaymentCallBack")
public class StripeWebhook {
    private KafkaTemplate<String, String> kafkaTemplate;

    public StripeWebhook(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping("/stripeWebhook")
    public void stripeWebhook(@RequestBody String payload) throws JsonProcessingException {
        System.out.println("Debug");
        Event event = null;

        try {
            event = Event.GSON.fromJson(payload, Event.class);
        } catch (JsonSyntaxException e) {
            throw new RuntimeException("Unexpected error while processing the payload", e);
        }

        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        PaymentIntent paymentIntent = (PaymentIntent) dataObjectDeserializer.getObject()
                .orElseThrow(() -> new RuntimeException("Failed to deserialize payment intent"));

        Map<String, String> metadata = paymentIntent.getMetadata();
        ObjectMapper objectMapper = new ObjectMapper();

        OrderPaymentUpdateDTO orderPaymentUpdateDTO = new OrderPaymentUpdateDTO();
        orderPaymentUpdateDTO.setOrderId(Long.valueOf(metadata.get("order_id")));

        switch (event.getType()) {
            case "payment_intent.succeeded":
                orderPaymentUpdateDTO.setMessage("Success");
                sendKafkaMessage(orderPaymentUpdateDTO, objectMapper.writeValueAsString(orderPaymentUpdateDTO));
                break;

            case "payment_intent.payment_failed":
                orderPaymentUpdateDTO.setMessage("Fail");
                sendKafkaMessage(orderPaymentUpdateDTO, objectMapper.writeValueAsString(orderPaymentUpdateDTO));
                break;

            default:
                System.out.println("Unhandled event type: " + event.getType());
                break;
        }
    }

    private void sendKafkaMessage(OrderPaymentUpdateDTO orderPaymentUpdateDTO, String payload) {
        try {
            kafkaTemplate.send(
                    "updatePaymentStatus", payload);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send Kafka message", e);
        }
    }
}