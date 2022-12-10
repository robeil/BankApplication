package bank.integration;

import org.springframework.stereotype.Component;

@Component
public class EmailSender {
    public void sendEmail(String emailAddress){
        System.out.println("Sending email to "+emailAddress+" , message = ");
    }
}
