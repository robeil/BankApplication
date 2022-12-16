package bank.service.impl;

import bank.domain.AccountEntry;
import bank.dto.AccountDTO;
import bank.event.ChangeEvent;
import bank.event.CreatedAccount;
import bank.event.DepositEvent;
import bank.event.Withdraw;
import bank.integration.EmailSender;
import bank.integration.JMSSender;
import bank.integration.TaxServiceJmsSender;
import bank.logging.LoggerService;
import bank.repository.AccountRepository;
import bank.domain.Account;
import bank.domain.Customer;
import bank.service.CurrencyConverterService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;

@Service
@Transactional
public class AccountService implements bank.service.AccountService {
    @Autowired
    private CurrencyConverterService currencyConverterService;
    @Autowired
    private TaxServiceJmsSender taxServiceJmsSender;
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private EmailSender emailSender;
    @Autowired
    private JMSSender jmsSender;
    @Autowired
    private LoggerService logger;

    //@PostConstruct
    public AccountDTO createAccount(long accountNumber, String customerName) throws JsonProcessingException {
        Account account = new Account(accountNumber);
        Customer customer = new Customer(customerName);
        account.setCustomer(customer);
        accountRepository.save(account);
        logger.log("createAccount with parameters accountNumber= "
                + accountNumber + " , customerName= " + customerName);
        /**
         * creating new account publish and send to kafka
         */
        CreatedAccount cAccount = new CreatedAccount(account.getAccountNumber(), customer.getId());
        ChangeEvent<CreatedAccount> createdAccount = new ChangeEvent<CreatedAccount>("create-account", cAccount);
         jmsSender.sendJMSMessage(createdAccount);
        logger.log("Account created : {}");
        // publisher.publishEvent(new ChangeEvent<CreatedAccount>());
        return modelMapper.map(account, AccountDTO.class);
    }

    public void deposit(long accountNumber, double amount) throws JsonProcessingException {
        Account account = accountRepository.findById(accountNumber).get();
        account.deposit(amount);
        accountRepository.save(account);
        logger.log("deposit with parameters accountNumber= " + accountNumber
                + " , amount= " + amount);
        if (amount > 10000) {
            jmsSender.sendRegularJMSMessage("Deposit of $ " + amount
                    + " to account with accountNumber= " + accountNumber);
            /**
             * sending message if the customer attempt to deposit more than 10,000
             */
            DepositEvent depositEvent = new DepositEvent(account.getAccountNumber(), account.getCustomer().getId(),amount);
            ChangeEvent<DepositEvent> depositMoney = new ChangeEvent<>("deposit-more-10000",depositEvent);
            taxServiceJmsSender.sendJMSMessageTax(depositMoney);
        }
        /**
         * sending message if the customer regular amount of money send to regular topic kafka
         */
        DepositEvent depositEvent = new DepositEvent(account.getAccountNumber(), account.getCustomer().getId(),amount);
        ChangeEvent<DepositEvent> depositMoney = new ChangeEvent<>("regular deposit",depositEvent);
        jmsSender.sendJMSMessage(depositMoney);
    }

    public AccountDTO getAccount(long accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        return modelMapper.map(account, AccountDTO.class);
    }

    @Override
    public AccountDTO updateAccount(Account account) {
        Account accountExist = accountRepository.findByAccountNumber(account.getAccountNumber());

        if (accountExist != null) {
            accountRepository.delete(accountExist); // remove the old
            accountRepository.save(account); // add the new
        }
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

    public void withdraw(long accountNumber, double amount) throws JsonProcessingException {
        Account account = accountRepository.findById(accountNumber).get();
        account.withdraw(amount);
        accountRepository.save(account);
        logger.log("withdraw with parameters accountNumber= " + accountNumber
                + " , amount= " + amount);
        Withdraw withdrawEvent = new Withdraw(account.getAccountNumber(),account.getCustomer().getId(),amount);
        ChangeEvent<Withdraw> withdrawChangeEvent = new ChangeEvent<>("Withdraw", withdrawEvent);
        jmsSender.sendJMSMessage(withdrawChangeEvent);
    }

    public void depositEuros(long accountNumber, double amount) throws JsonProcessingException {
        Account account = accountRepository.findById(accountNumber).get();
        double amountDollars = currencyConverterService.euroToDollars(amount);
        account.deposit(amountDollars);
        accountRepository.save(account);
        logger.log("depositEuros with parameters accountNumber= "
                + accountNumber + " , amount= " + amount);
        if (amountDollars > 10000) {
            jmsSender.sendRegularJMSMessage("Deposit of $ " + amount
                    + " to account with accountNumber= " + accountNumber);
        }
        /**
         * sending the changed event and send to kafka
         */
        DepositEvent depositEvent = new DepositEvent(account.getAccountNumber(), account.getCustomer().getId(),amount);
        ChangeEvent<DepositEvent> depositMoney = new ChangeEvent<>("deposit",depositEvent);
        jmsSender.sendJMSMessage(depositMoney);
    }

    public void withdrawEuros(long accountNumber, double amount) throws JsonProcessingException {
        Account account = accountRepository.findById(accountNumber).get();
        double amountDollars = currencyConverterService.euroToDollars(amount);
        account.withdraw(amountDollars);
        accountRepository.save(account);
        logger.log("withdrawEuros with parameters accountNumber= "
                + accountNumber + " , amount= " + amount);
        /**
         * sending the changed event to kafak
         */
        Withdraw withdrawEvent = new Withdraw(account.getAccountNumber(),account.getCustomer().getId(),amount);
        ChangeEvent<Withdraw> withdrawChangeEvent = new ChangeEvent<>("Withdraw", withdrawEvent);
        jmsSender.sendJMSMessage(withdrawChangeEvent);
    }

    public void transferFunds(long fromAccountNumber, long toAccountNumber,
                              double amount, String description) {
        Account fromAccount = accountRepository.findById(fromAccountNumber).get();
        Account toAccount = accountRepository.findById(toAccountNumber).get();
        fromAccount.transferFunds(toAccount, amount, description);
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
        logger.log("transferFunds with parameters fromAccountNumber= "
                + fromAccountNumber + " , toAccountNumber= " + toAccountNumber
                + " , amount= " + amount + " , description= " + description);
        if (amount > 10000) {
            jmsSender.sendRegularJMSMessage("TransferFunds of $ " + amount
                    + " from account with accountNumber= " + fromAccount
                    + " to account with accountNumber= " + toAccount);
        }

    }
}
