package repository;

import models.Transaction;

import java.util.HashMap;
import java.util.Map;

public class PendingTransactionRepository {
    /*
    This class keeps track of pending end-to-end transaction (not individual messages!).
    Transactions are identified by their transaction ID which is generated randomly.
     */
    private Map<String, Transaction> pendingTransactions = new HashMap<>();
    private static PendingTransactionRepository instance = null;

    protected PendingTransactionRepository() {
        // Exists only to defeat instantiation.
    }

    public void addPendingTransaction(Transaction tx) {
        pendingTransactions.put(tx.getTxid(), tx);
    }

    public void removePendingTransaction(String txid) {
        pendingTransactions.remove(txid);
    }

    public Transaction getPendingTransaction(String txid) {
        if(!pendingTransactions.containsKey(txid)) {
            return null;
        }
        return pendingTransactions.get(txid);
    }

    public static PendingTransactionRepository getInstance() {
        if(instance == null) {
            instance = new PendingTransactionRepository();
        }
        return instance;
    }
}
