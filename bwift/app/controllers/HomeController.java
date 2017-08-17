package controllers;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Bank;
import models.BankAccount;
import models.Transaction;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.libs.Json;
import play.libs.ws.WS;
import play.libs.ws.WSClient;
import play.mvc.*;
import repository.BankAccountRepository;
import repository.BankRepository;
import repository.TransactionRepository;

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

    public Result getAccounts() {
        ObjectNode result = Json.newObject();

        ArrayNode array = result.putArray("accounts");
        for(BankAccount account : BankAccountRepository.getInstance().getAccounts()) {
            array.add(account.getJsonRepresentation());
        }

        return ok(result);
    }

    public Result getTransactions() {
        ObjectNode result = Json.newObject();

        ArrayNode array = result.putArray("transactions");
        for(Transaction transaction : TransactionRepository.getInstance().getTransactions()) {
            array.add(transaction.getJsonRepresentation());
        }

        return ok(result);
    }

    public Result getBanks() {
        ObjectNode result = Json.newObject();

        ArrayNode array = result.putArray("banks");
        for(Bank bank : BankRepository.getInstance().getBanks()) {
            array.add(bank.getJsonRepresentation());
        }

        return ok(result);
    }

    public Result notifyTx() {
        DynamicForm dynamicForm = formFactory.form().bindFromRequest();

        String id = dynamicForm.get("id");
        String sourceAccount = dynamicForm.get("source");
        String destinationAccount = dynamicForm.get("destination");
        double amount = Double.parseDouble(dynamicForm.get("amount"));
        int timestamp = Integer.parseInt(dynamicForm.get("timestamp"));
        String description = dynamicForm.get("description");

        // find the corresponding bank account
        BankAccount account = BankAccountRepository.getInstance().getAccount(destinationAccount);
        if(account == null) {
            ObjectNode result = Json.newObject();
            result.put("success", false);
            result.put("error", "destination bank account not found");
            return ok(result);
        }

        account.add(amount);
        account.save();

        // store transaction to the database
        Transaction transaction = new Transaction(id, sourceAccount, destinationAccount, amount, timestamp, description);
        transaction.save();

        // TODO verify hash with tindermint

        ObjectNode result = Json.newObject();
        result.put("success", true);

        return ok(result);
    }

    public Result createTransaction() {
        DynamicForm dynamicForm = formFactory.form().bindFromRequest();

        String sourceAccount = dynamicForm.get("source");
        String destinationAccount = dynamicForm.get("destination");
        double amount = Double.parseDouble(dynamicForm.get("amount"));
        String description = dynamicForm.get("description");

        // check whether we have enough balance on this account
        BankAccount account = BankAccountRepository.getInstance().getAccount(destinationAccount);
        if(account.getBalance() >= amount) {
            ObjectNode result = Json.newObject();
            result.put("success", false);
            result.put("error", "not enough balance on bank account for transfer");
            return ok(result);
        }

        account.subtract(amount);
        account.save();

        // create the transaction
        Transaction tx = new Transaction(sourceAccount, destinationAccount, amount, System.currentTimeMillis(), description);
        tx.save();

        // put it on the blockchain
        ws.url("http://46.101.26.177:46657/broadcast_tx_commit?tx=\"" + tx.getHash() + "\"")
                .setContentType("application/x-www-form-urlencoded").get();

        ObjectNode result = Json.newObject();
        result.put("success", true);

        return ok(result);
    }

}
