package models;

import java.util.HashMap;
import java.util.Map;

public class PrivateAccountsRepository {
    /*
    This class keeps track of all accounts of a specific bank.
    Since it's a singleton class, it can be accessed from anywhere (not a very nice design pattern, I know, but for now, it works :)
     */

    private static PrivateAccountsRepository instance = null;
    private Map<String, BankAccount> accounts = new HashMap<>();

    protected PrivateAccountsRepository() {
        // Exists only to defeat instantiation.
    }

    private void addAccount(String address) {
        this.accounts.put(address, new BankAccount(address));
    }

    private BankAccount getAccount(String address) {
        if(accounts.containsKey(address)) { return accounts.get(address); }
        return null;
    }

    public static PrivateAccountsRepository getInstance() {
        if(instance == null) {
            instance = new PrivateAccountsRepository();

            // create some dummy accounts
            instance.addAccount("DUM1");
            instance.addAccount("DUM2");
            instance.addAccount("DUM3");
            instance.addAccount("DUM4");
            instance.addAccount("DUM5");

        }
        return instance;
    }
}