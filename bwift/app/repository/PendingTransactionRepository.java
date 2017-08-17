package repository;

import models.Transaction;
import play.Logger;

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
        Logger.debug("Adding pending transaction: " + tx.getTxid());
        pendingTransactions.put(tx.getTxid(), tx);
    }

    public void removePendingTransaction(String txid) {
        Logger.debug("Removing pending transaction: " + txid);
        pendingTransactions.remove(txid);
    }

    public Transaction getPendingTransaction(String txid) {
        Logger.debug("Looking for pending transaction: " + txid);
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
