package bank.service.impl;

import bank.domain.Account;
import bank.domain.Customer;
import bank.integration.EmailSender;
import bank.repository.AccountRepository;
import bank.repository.CustomerRepository;
import bank.service.TraceRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

@Service
public class BankService {
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private EmailSender emailSender;
	@Autowired
	private TraceRecordService traceRecordService;

	@Transactional
	public void createCustomerAndAccount(int customerId, String customerName, String emailAddress, long AccountNumber){
		try {
			Account account = new Account(AccountNumber);
			accountRepository.save(account);
			Customer customer = new Customer(customerName);
			customer.setAccount(account);
			customerRepository.saveCustomer(customer);
			emailSender.sendEmail("silukeen1@gmail.com","Bank Application","Message from bank application Enterprise Architecture");
			TraceRecord traceRecord = new TraceRecord(new Date(), "Customer "+customerName+" created with account" +AccountNumber);
			traceRecordService.save(traceRecord);

		}
		catch(Exception e) {
			emailSender.sendEmail("silukeen1@gmail.com","Bank Application","Message from bank application Enterprise Architecture");
			TraceRecord traceRecord = new TraceRecord(new Date(), "Error: could not create customer "+customerName+" with account "+AccountNumber);
			traceRecordService.save(traceRecord);
			throw e;
		}
	}

}
