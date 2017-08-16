package controllers;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.BankAccount;
import models.Transaction;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.*;
import repository.BankAccountRepository;
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

    public Result createTransaction() {
        DynamicForm dynamicForm = formFactory.form().bindFromRequest();

        String sourceAccount = dynamicForm.get("source");
        String destinationAccount = dynamicForm.get("destination");
        double amount = Double.parseDouble(dynamicForm.get("amount"));
        String description = dynamicForm.get("description");

        Transaction tx = new Transaction(sourceAccount, destinationAccount, amount, System.currentTimeMillis());
        tx.save();

        ObjectNode result = Json.newObject();
        result.put("success", true);

        return ok(result);
    }

}
