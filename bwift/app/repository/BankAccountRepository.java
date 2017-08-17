package repository;

import models.BankAccount;

import java.util.List;

public class BankAccountRepository {
    /*
    This class keeps track of all accounts of a specific bank.
    Since it's a singleton class, it can be accessed from anywhere (not a very nice design pattern, I know, but for now, it works :)
     */

    private static BankAccountRepository instance = null;

    protected BankAccountRepository() {
        // Exists only to defeat instantiation.
    }

    private void addAccount(String address) {
        BankAccount account = new BankAccount(address);
        account.save();
    }

    public List<BankAccount> getAccounts() {
        return BankAccount.find.all();
    }

    public BankAccount getAccount(String address) {
        List<BankAccount> accounts = this.getAccounts();
        for(BankAccount account : accounts) {
            if(account.getAddress().equals(address)) {
                return account;
            }
        }
        return null;
    }

    public static BankAccountRepository getInstance() {
        if(instance == null) {
            instance = new BankAccountRepository();

            // create some dummy accounts
            if(BankAccount.find.all().size() == 0) {
                instance.addAccount("DK11BNKA0111111111");
                instance.addAccount("DK11BNKB0111111111");
                instance.addAccount("DK11BNKC0111111111");
                instance.addAccount("DK11BNKD0111111111");
                instance.addAccount("DK11BNKE0111111111");
            }
        }
        return instance;
    }
}