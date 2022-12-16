package bank.controller;

import bank.dto.AccountDTO;

import java.util.ArrayList;
import java.util.Collection;

public class Accounts {
    Collection<AccountDTO> accountList = new ArrayList<AccountDTO>();

    public Collection<AccountDTO> getAccountList() {
        return accountList;
    }

    public void setAccountList(Collection<AccountDTO> accountList) {
        this.accountList = accountList;
    }
}
