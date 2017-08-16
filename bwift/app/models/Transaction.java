package models;

import java.io.UnsupportedEncodingException;
import java.security.*;

public class Transaction {
    /*
    Representation of a transaction.
     */
    private String sourceAccount;
    private String destinationAccount;
    private double amount;
    private int timestamp;

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public String getSourceAccount() {
        return this.sourceAccount;
    }

    public String getDestinationAccount() {
        return this.destinationAccount;
    }

    public double getAmount() {
        return this.amount;
    }

    public int getTimestamp() {
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
}
