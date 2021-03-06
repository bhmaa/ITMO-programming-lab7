package com.bhma.server.commands;

import com.bhma.common.exceptions.IllegalKeyException;
import com.bhma.common.exceptions.InvalidCommandArguments;
import com.bhma.common.util.CommandObjectRequirement;
import com.bhma.common.util.ExecuteCode;
import com.bhma.common.util.ServerResponse;
import com.bhma.server.collectionmanagers.CollectionManager;

/**
 * remove_key command
 */
public class RemoveKeyCommand extends Command {
    private final CollectionManager collectionManager;

    public RemoveKeyCommand(CollectionManager collectionManager) {
        super("remove_key", "удалить элемент из коллекции по его ключу", CommandObjectRequirement.NONE,
                true);
        this.collectionManager = collectionManager;
    }

    /**
     * removes element from collection by key
     * @param argument must be a number
     * @throws InvalidCommandArguments if argument is empty
     * @throws NumberFormatException if argument is not a number
     * @throws IllegalKeyException if there's no element with entered key
     */
    public ServerResponse execute(String argument, Object object, String username) throws InvalidCommandArguments,
            NumberFormatException, IllegalKeyException {
        if (argument.isEmpty() || object != null) {
            throw new InvalidCommandArguments();
        }
        try {
            long key = Long.parseLong(argument);
            if (!collectionManager.containsKey(key)) {
                throw new IllegalKeyException("There's no object with that key.");
            }
            if (!collectionManager.getByKey(key).getOwnerUsername().equals(username)) {
                throw new IllegalKeyException("Object with that key belong to the another user");
            }
            if (!collectionManager.remove(key)) {
                return new ServerResponse("Cannot remove object", ExecuteCode.SERVER_ERROR);
            }
            return new ServerResponse(ExecuteCode.SUCCESS);
        } catch (NumberFormatException e) {
            return new ServerResponse("the argument must be a long number", ExecuteCode.ERROR);
        }
    }
}
