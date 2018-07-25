package com.fundoonotes.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.fundoonotes")
public class RabbitMQConfig {


	public static final String REDIS_QUEUE_NAME = "Redis-Durable-Queue";
	
	public static final String ELASTIC_QUEUE_NAME = "Elastic-Durable-Queue";

	@Bean
	public MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	
}
