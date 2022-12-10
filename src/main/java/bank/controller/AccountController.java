package bank.controller;

import bank.domain.Account;
import bank.dto.AccountDTO;
import bank.service.AccountService;
import bank.service.impl.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private BankService bankService;

    @PostMapping("/createcustomer/{customerId}/{customerName}/{emailAddress}/{accountNumber}")
    public ResponseEntity<?> createCustomerAndCustomer(@PathVariable int customerId,
                                                       @PathVariable String customerName,
                                                       @PathVariable String emailAddress,
                                                       @PathVariable long accountNumber) {
        bankService.createCustomerAndAccount(customerId,customerName,emailAddress,accountNumber);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getAllAccounts() {
        var allAccounts = accountService.getAllAccounts();
        return ResponseEntity.ok(allAccounts);
    }

    @GetMapping("/get/{accountNumber}")
    public ResponseEntity<?> getAccount(@PathVariable long accountNumber) {
        var account = accountService.getAccount(accountNumber);
        return ResponseEntity.ok(account);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateAccount(@RequestBody Account account) {
        var updateAccount = accountService.updateAccount(account);
        return ResponseEntity.ok(updateAccount);
    }

    @PostMapping("/deposit/{accountNumber}")
    public ResponseEntity<?> deposit(@PathVariable long accountNumber, @RequestBody double amount) {
        accountService.deposit(accountNumber, amount);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping("/withdraw/{accountNumber}")
    public ResponseEntity<?> withdraw(@PathVariable long accountNumber, @RequestBody double amount) {
        accountService.withdraw(accountNumber, amount);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping("/withdraweures/{accountNumber}")
    public ResponseEntity<?> withdrawEuros(@PathVariable long accountNumber, @RequestBody double amount) {
        accountService.withdrawEuros(accountNumber, amount);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping("/depositeures/{accountNumber}")
    public ResponseEntity<?> depositEuros(@PathVariable long accountNumber, @RequestBody double amount) {
        accountService.depositEuros(accountNumber, amount);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping("/createaccount/{accountNumber}/{customerName}")
    public ResponseEntity<?> createAccount(@PathVariable long accountNumber, @PathVariable String customerName) {
        AccountDTO account = accountService.createAccount(accountNumber, customerName);
        return ResponseEntity.ok(account);
    }

    @PostMapping("/transfer/{fromAccountNumber}/{toAccountNumber}/{amount}/{description}")
    public ResponseEntity<?> transferFunds(@PathVariable long fromAccountNumber,
                                           @PathVariable long toAccountNumber,
                                           @PathVariable double amount,
                                           @PathVariable String description) {
        accountService.transferFunds(fromAccountNumber, toAccountNumber, amount, description);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
