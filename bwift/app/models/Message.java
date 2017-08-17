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

    public Message() {
        this.id = this.generateId();
    }

    public String getId() {
        return this.id;
    }

    public JsonNode getJsonRepresentation() {
        ObjectNode json = Json.newObject();
        json.put("id", this.id);
        return json;
    }
}
