package controllers;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.mvc.*;
import repository.BankRepository;

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

}
