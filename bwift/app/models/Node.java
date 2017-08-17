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
    private String ipAddress;
    private int port;

    public Node(String name, boolean isCompany, String ipAddress, int port) {
        this.name = name;
        this.isCompany = isCompany;
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public String getName() {
        return this.name;
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
        json.put("ip_address", this.ipAddress);
        json.put("port", this.port);
        return json;
    }

}