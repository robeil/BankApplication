package bank.integration;

import bank.event.ChangeEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class TaxServiceJmsSender {
    @Autowired
    private KafkaTemplate kafkaTemplate;

    public void sendJMSMessageTax(ChangeEvent event) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String depositAsString = objectMapper.writeValueAsString(event);
        kafkaTemplate.send("taxMDB",depositAsString);
        System.out.println("On queue : taxMDB "+" This is from bank to TaxService");
    }

}
