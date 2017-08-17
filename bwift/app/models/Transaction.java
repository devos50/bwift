package models;

import java.util.Random;

public class Transaction {
    /*
    This class represents an end-to-end transaction.
     */
    private String txid;

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

    public Transaction() {
        this.txid = this.generateId();
    }
}
