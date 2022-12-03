package bank.dto;

import bank.domain.AccountEntry;
import bank.domain.Customer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

public class AccountDTO {

    private Long accountNumber;
    private Customer customer;
    private double balance;

    private Collection<AccountEntry> accountEntries;

    public AccountDTO(){}
    public AccountDTO(Long accountNumber, Collection<AccountEntry> accountEntries, Customer customer,double balance) {
        this.accountNumber = accountNumber;
        this.accountEntries = accountEntries;
        this.customer = customer;
        this.balance = balance;

    }

    public Long getAccountNumber() {
        return accountNumber;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Collection<AccountEntry> getAccountEntries() {
        return accountEntries;
    }


    public void setAccountEntries(Collection<AccountEntry> accountEntries) {
        this.accountEntries = accountEntries;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
