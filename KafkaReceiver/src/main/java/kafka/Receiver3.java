package kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;

public class Receiver3 {
    @KafkaListener(topics = {"topicB"}, groupId = "gid2")
    public void receive(@Payload String message){
        System.out.println("Receiver3 received message = "+ message);
    }
}
