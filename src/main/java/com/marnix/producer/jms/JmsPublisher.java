package com.marnix.producer.jms;

import com.marnix.producer.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class JmsPublisher {
	@Autowired
	JmsTemplate jmsTemplate;
	
	@Value("${jsa.activemq.topic}")
	String topic;
	
	public void send(String product){
		jmsTemplate.convertAndSend(topic, product);
	}
}
