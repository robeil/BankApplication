package bank.service.impl;


import org.springframework.stereotype.Service;

@Service
public class CurrencyConverterService implements bank.service.CurrencyConverterService {
    public double euroToDollars (double amount){
		System.out.println("CurrencyConverter: converting "+amount+" dollars to euros");
        return 1.57 * amount;
    }
    
    public double dollarsToEuros (double amount){
        return 0.637 * amount;
    }
}
