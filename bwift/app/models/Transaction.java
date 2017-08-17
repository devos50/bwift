package models;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Random;

@Entity
public class Transaction extends Model {
    /*
    This class represents an end-to-end transaction.
     */
    public static final Finder<Long, Transaction> find = new Finder<>(Transaction.class);

    @Id
    private String txid;
    private boolean isComplete;
    private String sourceAccount;
    private String destinationAccount;
    private double amount;
    private String currency;

    public Transaction(String sourceAccount, String destinationAccount, double amount, String currency) {
        this.txid = this.generateId();
        this.isComplete = false;
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
        this.currency = currency;
    }

    private String generateId() {
        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }

    public String getTxid() {
        return this.txid;
    }

    public boolean isComplete() {
        return this.isComplete;
    }

    public void setComplete() {
        this.isComplete = true;
    }

    public String getSourceAccount() {
        return this.sourceAccount;
    }

    public String getDestinationAccount() {
        return this.destinationAccount;
    }

    public double getAmount() {
        return this.amount;
    }

    public String getCurrency() {
        return this.currency;
    }
}
