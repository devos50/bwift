package models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.Logger;
import play.libs.Json;
import util.GenerateKeys;
import util.HexUtil;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Random;

public class Message {
    /*
    This class represents a message in the system.
     */
    private String id;
    private String txid;
    private String type;
    private int step;
    private ObjectNode payload;
    private String signature1;
    private String signature2;

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

    public Message(String txid, String type, int step, ObjectNode payload) {
        this.id = this.generateId();
        this.txid = txid;
        this.type = type;
        this.step = step;
        this.payload = payload;
    }

    public Message(String id, String txid, String type, int step, ObjectNode payload) {
        this.id = id;
        this.txid = txid;
        this.type = type;
        this.step = step;
        this.payload = payload;
    }

    public String getId() {
        return this.id;
    }

    public String getTxid() {
        return this.txid;
    }

    public String getType() {
        return this.type;
    }

    public int getStep() {
        return this.step;
    }

    public ObjectNode getPayload() {
        return this.payload;
    }

    public String getSignature1() {
        return this.signature1;
    }

    public String getSignature2() {
        return this.signature2;
    }

    public void setSignature2(String sig) {
        this.signature2 = sig;
    }

    public JsonNode getJsonRepresentation() {
        ObjectNode json = Json.newObject();
        json.put("id", this.id);
        json.put("txid", this.txid);
        json.put("type", this.type);
        json.put("step", this.step);
        json.putObject("payload").setAll(this.payload);
        return json;
    }

    public void sign1() {
        try {
            Signature rsa = Signature.getInstance("SHA1withRSA");
            rsa.initSign(GenerateKeys.getPrivateKey());
            rsa.update(this.getJsonRepresentation().toString().getBytes());
            this.signature1 = HexUtil.bytesToHex(rsa.sign());
            Logger.debug("Party 1 signing message with type " + this.getType() + ": " + this.signature1);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    public void sign2() {
        try {
            Signature rsa = Signature.getInstance("SHA1withRSA");
            rsa.initSign(GenerateKeys.getPrivateKey());
            rsa.update(this.getJsonRepresentation().toString().getBytes());
            this.signature2 = HexUtil.bytesToHex(rsa.sign());
            Logger.debug("Party 2 signing message with type " + this.getType() + ": " + this.signature2);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }
}
