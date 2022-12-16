package bank.integration.messageReceiver;

import bank.event.ChangeEvent;
import bank.event.CreatedAccount;
import bank.event.DepositEvent;
import bank.event.Withdraw;
import bank.service.TraceRecordService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AccountReceiver {
    @Autowired
    private  TraceRecordService traceRecordService;

    @KafkaListener(topics = {"regularTopic"})
    public void receive(@Payload String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            /**
             * reading the created account from kafka
             */
            ChangeEvent changeEvent = objectMapper.readValue(message, ChangeEvent.class);
            if(changeEvent.getOperation().equals("create-account")){
                ChangeEvent<CreatedAccount> cAccount = objectMapper.readValue(message,
                        new TypeReference<ChangeEvent<CreatedAccount>>() {});
                CreatedAccount createdAccount = cAccount.getDescription();
                TraceRecord traceRecord = new TraceRecord(LocalDateTime.now(),
                        createdAccount.getAccountNumber(),
                        changeEvent.getOperation(),
                        0.0
                        );
                /**
                 * saving the created TraceRecord to db
                 */
               traceRecordService.save(traceRecord);

            }
            /**
             * reading the deposited money from kafka
             */
            if(changeEvent.getOperation().equals("deposit")){
                ChangeEvent<DepositEvent> depositAccount = objectMapper.readValue(message,
                        new TypeReference<ChangeEvent<DepositEvent>>() {});
                DepositEvent deposit = depositAccount.getDescription();
                TraceRecord traceRecord = new TraceRecord(LocalDateTime.now(),
                        deposit.getAccountNumber(),
                        changeEvent.getOperation(),
                        deposit.getAmount()
                );
                /**
                 * saving the deposited TraceRecord to db
                 */
                traceRecordService.save(traceRecord);
            }
            /**
             * reading the withdrawal money from kafka
             */
            if(changeEvent.getOperation().equals("withdraw")){
                ChangeEvent<Withdraw> withAccount = objectMapper.readValue(message,
                        new TypeReference<ChangeEvent<Withdraw>>() {});
                Withdraw withdrawAmount = withAccount.getDescription();
                TraceRecord traceRecord = new TraceRecord(LocalDateTime.now(),
                        withdrawAmount.getAccountNumber(),
                        changeEvent.getOperation(),
                        withdrawAmount.getAmount()
                );
                /**
                 * saving the withdrawal TraceRecord to db
                 */
                traceRecordService.save(traceRecord);
            }

        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
    }
}
