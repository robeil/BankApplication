package bank.service;

import bank.domain.Account;
import bank.dto.AccountDTO;

import java.util.Collection;



public interface AccountService {


    public Collection<AccountDTO> getAllAccounts();
    public AccountDTO getAccount(long accountNumber);
    public AccountDTO updateAccount( Account account);
    public void deposit (long accountNumber, double amount);
    public void withdraw (long accountNumber, double amount);
    public void depositEuros (long accountNumber, double amount);
    public void withdrawEuros (long accountNumber, double amount);

    public AccountDTO createAccount(long accountNumber, String customerName);
    public void transferFunds(long fromAccountNumber, long toAccountNumber, double amount, String description);
}
