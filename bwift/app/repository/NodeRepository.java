package repository;

import models.Node;

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

    private void addNode(String name, boolean isCompany, String ipAddress, int port) {
        Node newNode = new Node(name, isCompany, ipAddress, port);
        newNode.save();
    }

    public List<Node> getNodes() {
        return Node.find.all();
    }

    public Node getNode(String name) {
        List<Node> nodes = this.getNodes();
        for(Node bank : nodes) {
            if(bank.getName().equals(name)) {
                return bank;
            }
        }
        return null;
    }

    public static NodeRepository getInstance() {
        if(instance == null) {
            instance = new NodeRepository();

            // create some banks
            if(Node.find.all().size() == 0) {
                instance.addNode("Maersk", true, "127.0.0.1", 9000);
                instance.addNode("Nordea", false, "127.0.0.1", 9001);
                instance.addNode("Company", true, "127.0.0.1", 9002);
            }
        }
        return instance;
    }
}