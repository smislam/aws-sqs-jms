package com.example.message;

import jakarta.jms.JMSException;
import jakarta.jms.TextMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
    private SQSService sqSService;

    @Value("${sqs.name}")
    private String queueName;

    public MessageController(SQSService sqSService) {
        this.sqSService = sqSService;
    }

    @GetMapping("/")
    public String index() {
        return "Welcome to messaging app!";
    }

    @PostMapping("/send/{message}")
    public String send(@PathVariable String message) throws JMSException {
        return sqSService.send(queueName, message);
    }

    @GetMapping("/receive")
    public String receive() throws JMSException {
        return ((TextMessage) sqSService.receive(queueName)).getText();
    }
//    public List<String> receive(@PathVariable String queueName) throws JMSException {
//        List<String> justMessage = new ArrayList<>();
//        Optional.ofNullable(sqSService.receive(queueName)).orElse(Collections.emptyList()).stream().forEach(m -> {
//            try {
//                justMessage.add(m.getText());
//            } catch (JMSException e) {
//                e.printStackTrace();
//            }
//        });
//        return justMessage;
//    }
}
