package bank.integration.messageReceiver;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
public class TraceRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalDateTime localDateTime;
    private long accountNumber;
    private String operation;
    private double amount;


    public TraceRecord(LocalDateTime localDateTime, long accountNumber, String operation, double amount) {
        this.localDateTime = localDateTime;
        this.accountNumber = accountNumber;
        this.operation = operation;
        this.amount = amount;
    }
}
