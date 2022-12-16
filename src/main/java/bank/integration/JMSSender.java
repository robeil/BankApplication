package bank.integration;

import bank.event.ChangeEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class JMSSender{
	@Autowired
	private KafkaTemplate kafkaTemplate;

	public void sendJMSMessage(ChangeEvent event) throws JsonProcessingException{
		ObjectMapper objectMapper = new ObjectMapper();
		String depositAsString = objectMapper.writeValueAsString(event);
		kafkaTemplate.send("regularTopic",depositAsString);
		System.out.println("On queue : regularTopic "+" This is from bank to TaxService");
	}


	public void sendRegularJMSMessage(String text) {
		System.out.println(text);
	}

}
