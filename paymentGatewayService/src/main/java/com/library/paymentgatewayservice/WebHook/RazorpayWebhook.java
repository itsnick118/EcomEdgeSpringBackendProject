package com.library.paymentgatewayservice.WebHook;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.paymentgatewayservice.DTO.OrderPaymentUpdateDTO;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/RazorpayPaymentCallBack")
public class RazorpayWebhook {
    private RazorpayClient razorpayClient;
    private KafkaTemplate<String, String> kafkaTemplate;

    public RazorpayWebhook(RazorpayClient razorpayClient, KafkaTemplate<String, String> kafkaTemplate) {
        this.razorpayClient = razorpayClient;
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping("/razorPayWebhook")
    public void handleWebhook(@RequestBody String payload) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode payloadJson = objectMapper.readTree(payload);
            JsonNode entity = payloadJson.path("payload").path("payment").path("entity");
            JSONObject orderDetails = razorpayClient.orders.fetch(entity.path("order_id").asText()).toJson();
            String eventType = payloadJson.path("event").asText();
            OrderPaymentUpdateDTO orderPaymentUpdateDTO = new OrderPaymentUpdateDTO();
            orderPaymentUpdateDTO.setOrderId(Long.valueOf(orderDetails.get("receipt").toString()));

            switch (eventType) {
                case "payment.captured":
                    orderPaymentUpdateDTO.setMessage("Success");
                    sendKafkaMessage(orderPaymentUpdateDTO, objectMapper.writeValueAsString(orderPaymentUpdateDTO));
                    break;

                case "payment.failed":
                    orderPaymentUpdateDTO.setMessage("Fail");
                    sendKafkaMessage(orderPaymentUpdateDTO, objectMapper.writeValueAsString(orderPaymentUpdateDTO));
                    break;

                default:
                    System.out.println("Unhandled event type: " + eventType);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error processing webhook payload", e);
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
