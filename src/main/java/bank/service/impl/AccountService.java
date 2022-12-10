package bank.service.impl;

import bank.domain.AccountEntry;
import bank.dto.AccountDTO;
import bank.integration.EmailSender;
import bank.repository.AccountRepository;
import bank.domain.Account;
import bank.domain.Customer;
import bank.logging.ILogger;
import bank.service.CurrencyConverterService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class AccountService implements bank.service.AccountService {
    @Autowired
    private CurrencyConverterService currencyConverterService;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private EmailSender emailSender;
    @Autowired
    private ILogger logger;

    //@PostConstruct
    public AccountDTO createAccount(long accountNumber, String customerName) {
        Account account = new Account(accountNumber);
        Customer customer = new Customer(customerName);

        AccountDTO accountDTO = new AccountDTO();

        accountDTO.setCustomer(customer);

        account.setCustomer(customer);
        accountRepository.save(account);
        logger.log("createAccount with parameters accountNumber= " + accountNumber + " , customerName= " + customerName);
        return modelMapper.map(account, AccountDTO.class);
    }

    public void deposit(long accountNumber, double amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        account.deposit(amount);
        updateAccount(account);
        logger.log("deposit with parameters accountNumber= " + accountNumber + " , amount= " + amount);
        if (amount > 10000) {
            //emailSender.sendEmail("Deposit of $ " + amount + " to account with accountNumber= " + accountNumber);
        }
    }

    public AccountDTO getAccount(long accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        //FromAccountToDTO fromToDto = new FromAccountToDTO(account);
        // return fromToDto.getAccountDTO();
        return modelMapper.map(account, AccountDTO.class);
    }

    @Override
    public AccountDTO updateAccount(Account account) {
        Account accountExist = accountRepository.findByAccountNumber(account.getAccountNumber());

        if (accountExist != null) {
            accountRepository.delete(accountExist); // remove the old
            accountRepository.save(account); // add the new
        }
        //FromAccountToDTO fromToDto = new FromAccountToDTO(account);
        //return fromToDto.getAccountDTO();
        return modelMapper.map(account, AccountDTO.class);
    }

    public Collection<AccountDTO> getAllAccounts() {
        var allAccounts = accountRepository.findAll();
        Collection<AccountDTO> accountListDto = new ArrayList<>();

        for (Account account : allAccounts) {
            Collection<AccountEntry> accountEntry = account.getEntryList();
            AccountDTO accountDTO = modelMapper.map(account, AccountDTO.class);
            accountDTO.setAccountEntries(accountEntry);
            accountListDto.add(accountDTO);
        }
        return accountListDto;
    }

    public void withdraw(long accountNumber, double amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        account.withdraw(amount);
        updateAccount(account);
        logger.log("withdraw with parameters accountNumber= " + accountNumber + " , amount= " + amount);
    }

    public void depositEuros(long accountNumber, double amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        double amountDollars = currencyConverterService.euroToDollars(amount);
        account.deposit(amountDollars);
        updateAccount(account);
        logger.log("depositEuros with parameters accountNumber= " + accountNumber + " , amount= " + amount);
        if (amountDollars > 10000) {
            //emailSender.sendEmail("Deposit of $ " + amount + " to account with accountNumber= " + accountNumber);

        }
    }

    public void withdrawEuros(long accountNumber, double amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        double amountDollars = currencyConverterService.euroToDollars(amount);
        account.withdraw(amountDollars);
        updateAccount(account);
        logger.log("withdrawEuros with parameters accountNumber= " + accountNumber + " , amount= " + amount);
    }

    public void transferFunds(long fromAccountNumber, long toAccountNumber, double amount, String description) {
        Account fromAccount = accountRepository.findByAccountNumber(fromAccountNumber);
        Account toAccount = accountRepository.findByAccountNumber(toAccountNumber);
        fromAccount.transferFunds(toAccount, amount, description);
        updateAccount(fromAccount);
        updateAccount(toAccount);
        logger.log("transferFunds with parameters fromAccountNumber= " + fromAccountNumber + " , toAccountNumber= " + toAccountNumber + " , amount= " + amount + " , description= " + description);
        if (amount > 10000) {
           /// jmsSender.sendJMSMessage("TransferFunds of $ " + amount + " from account with accountNumber= " + fromAccount + " to account with accountNumber= " + toAccount);
        }
    }
}
