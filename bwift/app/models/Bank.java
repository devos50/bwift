package models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.ebean.*;
import play.libs.Json;

import javax.persistence.*;

@Entity
public class Bank extends Model {
    /*
    Representation of a bank.
     */
    public static final Finder<Long, Bank> find = new Finder<>(Bank.class);

    @Id
    private String name;
    private String identificationCode;
    private String ipAddress;
    private int port;

    public Bank(String name, String identificationCode, String ipAddress, int port) {
        this.name = name;
        this.identificationCode = identificationCode;
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public String getName() {
        return this.name;
    }

    public String getIdentificationCode() {
        return this.identificationCode;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public int getPort() {
        return this.port;
    }

    public JsonNode getJsonRepresentation() {
        ObjectNode json = Json.newObject();
        json.put("name", this.name);
        json.put("identification_code", this.identificationCode);
        json.put("ip_address", this.ipAddress);
        json.put("port", this.port);
        return json;
    }

}
