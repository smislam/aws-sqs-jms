package com.example.message;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.services.sqs.SqsClient;

@SpringBootApplication
public class MessageApplication {

	public static void main(String[] args) {
		SpringApplication.run(MessageApplication.class, args);
	}

	@Bean
	public SQSConnectionFactory createConnectionFactory() {
		return new SQSConnectionFactory(
				new ProviderConfiguration(),
				SqsClient.builder()
						.credentialsProvider(DefaultCredentialsProvider.builder().build())
						.build()
		);
	}
}
