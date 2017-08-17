package repository;

import models.Node;
import play.Logger;

import java.util.List;

public class NodeRepository {
    /*
    This class keeps track of all nodes in the network (banks and companies).
    Since it's a singleton class, it can be accessed from anywhere (not a very nice design pattern, I know, but for now, it works :)
     */

    private static NodeRepository instance = null;

    protected NodeRepository() {
        // Exists only to defeat instantiation.
    }

    private void addNode(String name, boolean isCompany, String identificationCode, String bankAccount, String ipAddress, int port) {
        Node newNode = new Node(name, isCompany, identificationCode, bankAccount, ipAddress, port);
        newNode.save();
    }

    public List<Node> getNodes() {
        return Node.find.all();
    }

    public Node getBICNode(String bic) {
        Logger.debug("Looking for bank with bic " + bic);
        // return a node based on the bank identification code (used to lookup banks)
        List<Node> nodes = this.getNodes();
        for(Node node : nodes) {
            if(node.getIdentificationCode().equals(bic) && !node.isCompany()) {
                return node;
            }
        }
        return null;
    }

    public Node getCompanyNode(String bankAccount) {
        // return a node based on the bank account (used to lookup companies)
        Logger.debug("Fetching company with bank account " + bankAccount);
        List<Node> nodes = this.getNodes();
        for(Node node : nodes) {
            if(node.getBankAccount().equals(bankAccount) && node.isCompany()) {
                return node;
            }
        }
        return null;
    }

    public static NodeRepository getInstance() {
        if(instance == null) {
            instance = new NodeRepository();

            // create some banks
            if(Node.find.all().size() == 0) {
                instance.addNode("Maersk", true, "", "NL11NORD111111111", "127.0.0.1", 9000);
                instance.addNode("Nordea", false, "NORD", "", "127.0.0.1", 9000);
                instance.addNode("Company", true, "", "NL11NORD111111111", "127.0.0.1", 9000);
            }
        }
        return instance;
    }
}