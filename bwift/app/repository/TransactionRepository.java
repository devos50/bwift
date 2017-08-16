package repository;

import models.BankAccount;
import models.Transaction;

import java.util.List;

public class TransactionRepository {
    /*
    This class keeps track of all transactions performed by this node.
    Since it's a singleton class, it can be accessed from anywhere (not a very nice design pattern, I know, but for now, it works :)
     */

    private static TransactionRepository instance = null;

    protected TransactionRepository() {
        // Exists only to defeat instantiation.
    }

    public List<Transaction> getTransactions() {
        return Transaction.find.all();
    }

    public static TransactionRepository getInstance() {
        if(instance == null) {
            instance = new TransactionRepository();
        }
        return instance;
    }
}