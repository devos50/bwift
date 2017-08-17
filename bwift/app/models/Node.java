package models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.ebean.*;
import play.libs.Json;

import javax.persistence.*;

@Entity
public class Node extends Model {
    /*
    Representation of a bank.
     */
    public static final Finder<Long, Node> find = new Finder<>(Node.class);

    @Id
    private String name;
    private boolean isCompany; // true iff this node is a company, else false
    private String identificationCode; // banks should have an identification code
    private String bankAccount; // companies should have a bank account
    private String ipAddress;
    private int port;

    public Node(String name, boolean isCompany, String identificationCode, String bankAccount, String ipAddress, int port) {
        this.name = name;
        this.isCompany = isCompany;
        this.identificationCode = identificationCode;
        this.bankAccount = bankAccount;
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public String getName() {
        return this.name;
    }

    public boolean isCompany() {
        return this.isCompany;
    }

    public String getIdentificationCode() {
        return this.identificationCode;
    }

    public String getBankAccount() {
        return this.bankAccount;
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
        json.put("is_company", this.isCompany);
        json.put("identification_code", this.identificationCode);
        json.put("bank_account", this.bankAccount);
        json.put("ip_address", this.ipAddress);
        json.put("port", this.port);
        return json;
    }

}