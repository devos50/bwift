package models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.ebean.Finder;
import io.ebean.Model;
import play.libs.Json;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.util.Random;

@Entity
public class Transaction extends Model {
    /*
    Representation of a transaction.
     */
    public static final Finder<Long, Transaction> find = new Finder<>(Transaction.class);

    @Id
    private String id;
    private String sourceAccount;
    private String destinationAccount;
    private double amount;
    private long timestamp;

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

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

    public Transaction(String sourceAccount, String destinationAccount, double amount, long timestamp) {
        this.id = this.generateId();
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
        this.timestamp = timestamp;
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

    public long getTimestamp() {
        return this.timestamp;
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public String getHash() {
        try {
            String txString = this.sourceAccount + "-" + this.destinationAccount + "-" + this.amount + "-" + this.timestamp;
            byte[] bytesOfMessage = txString.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            return Transaction.bytesToHex(md.digest(bytesOfMessage));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JsonNode getJsonRepresentation() {
        ObjectNode json = Json.newObject();
        json.put("id", this.id);
        json.put("source", this.sourceAccount);
        json.put("destination", this.destinationAccount);
        json.put("amount", this.amount);
        json.put("timestamp", this.timestamp);
        return json;
    }
}
