package models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.ebean.*;
import play.libs.Json;

import javax.persistence.*;

@Entity
public class BankAccount extends Model {
    /*
    Representation of a bank account.
     */
    public static final Finder<Long, BankAccount> find = new Finder<>(BankAccount.class);

    @Id
    private String address;
    private double balance;

    public BankAccount(String address) {
        this.address = address;
        this.balance = 1000.0; // initial balance
    }

    public void add(double toAdd) {
        assert toAdd > 0;
        this.balance += toAdd;
    }

    public void subtract(double toSub) {
        assert this.balance - toSub >= 0;
        this.balance -= toSub;
    }

    public double getBalance() {
        return this.balance;
    }

    public String getAddress() {
        return this.address;
    }

    public JsonNode getJsonRepresentation() {
        ObjectNode json = Json.newObject();
        json.put("address", this.address);
        json.put("balance", this.balance);
        return json;
    }

}
