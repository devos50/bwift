package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Message;
import models.Node;
import models.Transaction;
import play.Logger;
import play.data.FormFactory;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Result;
import repository.NodeRepository;
import repository.PendingMessageRepository;
import repository.PendingTransactionRepository;

import javax.inject.Inject;
import java.util.Objects;
import java.util.concurrent.CompletionStage;

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

    private Result error(String msg) {
        Logger.error("Error when performing request: " + msg);
        ObjectNode result = Json.newObject();
        result.put("success", false);
        result.put("error", msg);
        return ok(result);
    }

    public Result createTransaction() {
        Logger.info("Creating a new transaction");
        JsonNode json = request().body().asJson();
        String sourceAccount = json.get("source").asText();
        String destinationAccount = json.get("destination").asText();
        double amount = Double.parseDouble(json.get("amount").asText());
        String currency = json.get("currency").asText();
        int valueDate = Integer.parseInt(json.get("value_date").asText());
        String customerName = json.get("customer_name").asText();
        String customerAddress = json.get("customer_address").asText();

        // determine to which company we need to send the start_transaction message
        Node companyNode = NodeRepository.getInstance().getCompanyNode(destinationAccount);
        if(companyNode == null) {
            return this.error("first company node not found");
        }

        // create a new transaction and add it to the pending transactions
        Transaction tx = new Transaction(sourceAccount, destinationAccount, amount, currency);
        tx.save();
        PendingTransactionRepository.getInstance().addPendingTransaction(tx);

        // send a start_transaction message to the final company
        Message start_tx_message = new Message(tx.getTxid(), "start_transaction", -1, json.deepCopy());
        start_tx_message.sign1();
        PendingMessageRepository.getInstance().addPendingMessage(start_tx_message);

        ws.url("http://localhost:9000/msg").post(start_tx_message.getJsonRepresentation());

        ObjectNode result = Json.newObject();
        result.put("success", true);
        return ok(result);
    }

    private Result handleStartTransaction(Message msg) {
        ObjectNode result = Json.newObject();
        result.put("success", true);
        return ok(result);
    }

    private Result handleFirstStep(Message msg) {
        ObjectNode result = Json.newObject();
        result.put("success", true);
        return ok(result);
    }

    private Result handleSecondStep(Message msg) {
        ObjectNode result = Json.newObject();
        result.put("success", true);
        return ok(result);
    }

    private Result handleThirdStep(Message msg) {
        ObjectNode result = Json.newObject();
        result.put("success", true);
        return ok(result);
    }

    private Result handleEndTransaction(Message msg) {
        Transaction tx = PendingTransactionRepository.getInstance().getPendingTransaction(msg.getTxid());
        tx.setComplete();
        tx.save();
        PendingTransactionRepository.getInstance().removePendingTransaction(msg.getTxid());

        ObjectNode result = Json.newObject();
        result.put("success", true);
        return ok(result);
    }

    public Result processMessage() {
        /*
        Process an incoming message
         */
        JsonNode json = request().body().asJson();

        String msgType = json.get("type").asText();
        int step = json.get("step").asInt();
        ObjectNode payload = json.get("payload").deepCopy();
        Logger.info("Received incoming message (type: " + msgType + ")");
        Message newMessage = new Message(json.get("id").asText(), json.get("txid").asText(), msgType, step, payload);
        newMessage.sign2();

        Result result;
        boolean didError = false;

        if(Objects.equals(msgType, "start_transaction")) {
            result = this.handleStartTransaction(newMessage);
        }
        else if(Objects.equals(msgType, "first_step")) {
            result = this.handleFirstStep(newMessage);
        }
        else if(Objects.equals(msgType, "second_step")) {
            result = this.handleSecondStep(newMessage);
        }
        else if(Objects.equals(msgType, "third_step")) {
            result = this.handleThirdStep(newMessage);
        }
        else if(Objects.equals(msgType, "end_transaction")) {
            result = this.handleEndTransaction(newMessage);
        }
        else {
            didError = true;
            result = this.error("cannot handle message type");
        }

        if(!didError) {
            // send ack back
            ObjectNode resultJson = Json.newObject();
            resultJson.put("msg_id", newMessage.getId());
            resultJson.put("signature2", newMessage.getSignature2());

            Logger.debug("Sending ack with json: " + resultJson.toString());
            ws.url("http://localhost:9000/ack").post(resultJson);
        }
        return result;
    }

    public Result processAck() {
        /*
        Process an acknowledgement message
         */
        JsonNode json = request().body().asJson();
        Logger.info("Received ack: " + json.toString());
        String msgId = json.get("msg_id").asText();

        String signature = json.get("signature2").asText();

        // first check whether we have a pending message in the repository
        Message msg = PendingMessageRepository.getInstance().getPendingMessage(msgId);
        if(msg == null) {
            return this.error("cannot find pending message");
        }

        PendingMessageRepository.getInstance().removePendingMessage(msg.getId());
        msg.setSignature2(signature);

        // TODO Jason: add message to tendermint

        if(msg.getType().equals("start_transaction")) {
            String identificationCode = msg.getPayload().get("source").toString().substring(5, 9);
            Node bank = NodeRepository.getInstance().getBICNode(identificationCode);
            if(bank == null) {
                return this.error("cannot find bank");
            }

            Message newMsg = new Message(msg.getTxid(), "first_step", 1, msg.getPayload());
            PendingMessageRepository.getInstance().addPendingMessage(newMsg);

            String url = "http://" + bank.getIpAddress() + ":" + bank.getPort() + "/msg";
            Logger.debug("Sending first_step message to " + url);
            ws.url(url).post(newMsg.getJsonRepresentation());
        }
        else if(msg.getType().equals("first_step")) {
            String identificationCode = msg.getPayload().get("destination").toString().substring(5, 9);
            Node bank = NodeRepository.getInstance().getBICNode(identificationCode);
            if(bank == null) {
                return this.error("cannot find bank");
            }

            Message newMsg = new Message(msg.getTxid(), "second_step", 2, msg.getPayload());
            PendingMessageRepository.getInstance().addPendingMessage(newMsg);

            ws.url("http://" + bank.getIpAddress() + ":" + bank.getPort() + "/msg").post(newMsg.getJsonRepresentation());
        }
        else if(msg.getType().equals("second_step")) {
            String destinationIban = msg.getPayload().get("destination").asText();
            Node company = NodeRepository.getInstance().getCompanyNode(destinationIban);
            if(company == null) {
                return this.error("cannot find company");
            }

            Message newMsg = new Message(msg.getTxid(), "third_step", 4, msg.getPayload());
            PendingMessageRepository.getInstance().addPendingMessage(newMsg);

            ws.url("http://" + company.getIpAddress() + ":" + company.getPort() + "/msg").post(newMsg.getJsonRepresentation());
        }
        else if(msg.getType().equals("third_step")) {
            String sourceIban = msg.getPayload().get("source").asText();
            Node company = NodeRepository.getInstance().getCompanyNode(sourceIban);
            if(company == null) {
                return this.error("cannot find company");
            }

            Message newMsg = new Message(msg.getTxid(), "end_transaction", -1, msg.getPayload());
            PendingMessageRepository.getInstance().addPendingMessage(newMsg);

            ws.url("http://" + company.getIpAddress() + ":" + company.getPort() + "/msg").post(newMsg.getJsonRepresentation());
        }
        else if(msg.getType().equals("end_transaction")) {

        }

        ObjectNode result = Json.newObject();
        result.put("success", true);
        return ok(result);
    }

    //ADDED TENDERMINT API
    public CompletionStage<String> appendTxBC(String value) {
        String tendermintURL = "http://46.101.26.177:46657/broadcast_tx_sync?tx=\"" + value + "\"";
        CompletionStage<WSResponse> jsonReponse = ws.url(tendermintURL).setContentType("application/x-www-form-urlencoded").get();
        return jsonReponse.thenApply(response -> response.asJson().get("error").asText());
    }

    public CompletionStage<JsonNode> queryTxBC(String value) {
        String tendermintURL = "http://46.101.26.177:46657/abci_query?path=\"\"&data=\"" + value + "\"&prove=false";
        CompletionStage<WSResponse> jsonReponse = ws.url(tendermintURL).setContentType("application/x-www-form-urlencoded").get();
        return jsonReponse.thenApply(response -> Json.toJson(response.asJson().get("result").asText()));
    }

}
