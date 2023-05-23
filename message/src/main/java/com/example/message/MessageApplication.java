package com.example.message;

import com.amazon.sqs.javamessaging.AmazonSQSExtendedClient;
import com.amazon.sqs.javamessaging.ExtendedClientConfiguration;
import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.core.JmsTemplate;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.sqs.SqsClient;

@SpringBootApplication
public class MessageApplication {

	@Value("${sqs.name}")
	private String queueName;

	public static void main(String[] args) {
		SpringApplication.run(MessageApplication.class, args);
	}

	@Bean
	public SQSConnectionFactory createConnectionFactory() {

		S3Client s3Client = S3Client.builder()
				.credentialsProvider(DefaultCredentialsProvider.builder().build())
				.build();

		return new SQSConnectionFactory(
				new ProviderConfiguration(),
				new AmazonSQSExtendedClient(
					SqsClient.builder().credentialsProvider(DefaultCredentialsProvider.builder().build()).build(),
						new ExtendedClientConfiguration().withPayloadSupportEnabled(s3Client, queueName)));
	}

	// // Uncomment this code if you want JmsTemplate configured with specific queue.  That way, you will not have to pass the queuname in the controller.
	// @Bean
	// public JmsTemplate createJmsTemplate() {
	// 	JmsTemplate jmsTemplate = new JmsTemplate(this.createConnectionFactory());
	// 	jmsTemplate.setDefaultDestinationName(queueName);
	// 	return jmsTemplate;
	// }
}
