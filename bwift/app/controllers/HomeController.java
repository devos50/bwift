package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Message;
import models.Node;
import models.Transaction;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.mvc.*;
import repository.NodeRepository;
import repository.PendingTransactionRepository;

import javax.inject.Inject;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */

    @Inject FormFactory formFactory;
    @Inject WSClient ws;

    public Result index() {
        return ok(views.html.index.render());
    }

    public Result getNodes() {
        ObjectNode result = Json.newObject();

        ArrayNode array = result.putArray("nodes");
        for(Node node : NodeRepository.getInstance().getNodes()) {
            array.add(node.getJsonRepresentation());
        }

        return ok(result);
    }

    public Result createTransaction() {
        JsonNode json = request().body().asJson();
        String sourceAccount = json.get("source").asText();
        String destinationAccount = json.get("destination").asText();
        double amount = Double.parseDouble(json.get("amount").asText());
        String currency = json.get("currency").asText();
        int valueDate = Integer.parseInt(json.get("value_date").asText());
        String customerName = json.get("customer_address").asText();
        String customerAddress = json.get("customer_address").asText();

        // determine to which company we need to send the start_transaction message
        Node companyNode = NodeRepository.getInstance().getCompanyNode(destinationAccount);
        if(companyNode == null) {
            ObjectNode result = Json.newObject();
            result.put("success", false);
            result.put("error", "final company node not found");
            return ok(result);
        }

        // create a new transaction and add it to the pending transactions
        Transaction tx = new Transaction();
        PendingTransactionRepository.getInstance().addPendingTransaction(tx);

        // send a start_transaction message to the final company
        Message start_tx_message = new Message();
        // TODO add more data to it

        ws.url("http://localhost:9000").post(start_tx_message.getJsonRepresentation());

        ObjectNode result = Json.newObject();
        result.put("success", true);
        return ok(result);
    }

}
