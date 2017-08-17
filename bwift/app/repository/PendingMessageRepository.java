package repository;

import models.Message;
import play.Logger;

import java.util.HashMap;
import java.util.Map;

public class PendingMessageRepository {
    /*
    This class keeps track of pending individual messages.
    Messages are identified by their message ID which is generated randomly.
     */
    private Map<String, Message> pendingMessages = new HashMap<>();
    private static PendingMessageRepository instance = null;

    protected PendingMessageRepository() {
        // Exists only to defeat instantiation.
    }

    public void addPendingMessage(Message msg) {
        Logger.debug("Adding pending message: " + msg.getId());
        pendingMessages.put(msg.getId(), msg);
    }

    public void removePendingMessage(String id) {
        Logger.debug("Removing pending message: " + id);
        pendingMessages.remove(id);
    }

    public Message getPendingMessage(String id) {
        Logger.debug("Looking for pending message: " + id);
        if(!pendingMessages.containsKey(id)) {
            return null;
        }
        return pendingMessages.get(id);
    }

    public static PendingMessageRepository getInstance() {
        if(instance == null) {
            instance = new PendingMessageRepository();
        }
        return instance;
    }
}
