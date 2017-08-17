package models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;

import java.util.Random;

public class Message {
    /*
    This class represents a message in the system.
     */
    private String id;
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

    public Message(String type, int step, ObjectNode payload) {
        this.id = this.generateId();
        this.type = type;
        this.step = step;
        this.payload = payload;
    }

    public Message(String id, String type, int step, ObjectNode payload) {
        this.id = id;
        this.type = type;
        this.step = step;
        this.payload = payload;
    }

    public String getId() {
        return this.id;
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
        json.put("type", this.type);
        json.put("step", this.step);
        json.putObject("payload").setAll(this.payload);
        return json;
    }

    public void sign1() {
        // sign the message as the first party
        this.signature1 = "00000";
    }

    public void sign2() {
        // sign the message as the first party
        this.signature2 = "00000";
    }
}
