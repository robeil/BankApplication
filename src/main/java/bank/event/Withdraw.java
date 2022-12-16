package bank.event;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Withdraw {
    private long accountNumber;
    private long customerId;
    private  double amount;
}
