package com.example.message;

import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import jakarta.jms.Message;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class SQSService {

    private final SQSConnectionFactory sqsConnectionFactory;
    private final JmsTemplate jmsTemplate;

    public SQSService(SQSConnectionFactory sqsConnectionFactory, JmsTemplate jmsTemplate) {
        this.sqsConnectionFactory = sqsConnectionFactory;
        this.jmsTemplate = jmsTemplate;
    }

    public String send(String queueName, String message) {
        jmsTemplate.convertAndSend(queueName, message);
        return "Message Sent: " + message;
    }

    public Message receive(String queueName) {
        return jmsTemplate.receive(queueName);
    }

    public String send(String message) {
        jmsTemplate.convertAndSend(message);
        return "Message Sent: " + message;
    }

    public Object receive() {
	    return jmsTemplate.receiveAndConvert();
    }

//    public String sendNoTemplate(String queueName, String message) throws JMSException {
//        SQSConnection sqsConnection = sqsConnectionFactory.createConnection();
//
//        if (!isValid(sqsConnection,queueName)) {
//            return null;
//        }
//        Session sqsSession = sqsConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//        MessageProducer producer = sqsSession.createProducer(sqsSession.createQueue(queueName));
//        TextMessage textMessage = sqsSession.createTextMessage(message);
//        producer.send(sqsSession.createTextMessage(message));
//        sqsConnection.close();
//
//        return "Message Sent: " + textMessage.getText();
//    }

//    public List<TextMessage> receiveNoTemplate(String queueName) throws JMSException {
//        SQSConnection sqsConnection = sqsConnectionFactory.createConnection();
//
//        if (!isValid(sqsConnection,queueName)) {
//            return null;
//        }
//        Session sqsSession = sqsConnection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
//
//        sqsConnection.start();
//        List<TextMessage> messages = getMessages(sqsSession.createConsumer(sqsSession.createQueue(queueName)));
//        sqsConnection.close();
//        return messages;
//    }
//
//    private List<TextMessage> getMessages(MessageConsumer consumer) throws JMSException {
//        List<TextMessage> receivedMessages = new ArrayList();
//        while (true) {
//            Message receivedMessage = consumer.receive(TimeUnit.SECONDS.toMillis(1));
//            if (receivedMessage == null) {
//                break;
//            }
//            receivedMessages.add((TextMessage) receivedMessage);
//            receivedMessage.acknowledge();
//        }
//        return receivedMessages;
//    }
//
//    private boolean isValid(SQSConnection sqsConnection, String queueName) throws JMSException {
//        return sqsConnection.getWrappedAmazonSQSClient().queueExists(queueName);
//    }
}
