package com.bhma.server.commands;

import com.bhma.common.data.SpaceMarine;
import com.bhma.common.exceptions.IllegalKeyException;
import com.bhma.common.exceptions.InvalidCommandArguments;
import com.bhma.common.util.CommandObjectRequirement;
import com.bhma.common.util.ExecuteCode;
import com.bhma.common.util.ServerResponse;
import com.bhma.server.collectionmanagers.CollectionManager;

/**
 * insert command
 */
public class InsertCommand extends Command {
    private final CollectionManager collectionManager;

    public InsertCommand(CollectionManager collectionManager) {
        super("insert", "добавить новый элемент с заданным ключом", CommandObjectRequirement.SPACE_MARINE,
                true);
        this.collectionManager = collectionManager;
    }

    /**
     * add to collection element with entered key
     * @param argument must be a number (long)
     * @throws InvalidCommandArguments if argument is empty
     * @throws NumberFormatException if argument is not a number
     * @throws IllegalKeyException if there is an element with equal key in collection
     */
    public ServerResponse execute(String argument, Object spaceMarine, String username) throws InvalidCommandArguments,
            NumberFormatException, IllegalKeyException {
        if (argument.isEmpty() || spaceMarine == null || spaceMarine.getClass() != SpaceMarine.class) {
            throw new InvalidCommandArguments();
        }
        try {
            long key = Long.parseLong(argument);
            if (collectionManager.getCollection().containsKey(key)) {
                throw new IllegalKeyException("Element with this key is already exists");
            }
            collectionManager.addToCollection(key, (SpaceMarine) spaceMarine);
            return new ServerResponse(ExecuteCode.SUCCESS);
        } catch (NumberFormatException e) {
            return new ServerResponse("the argument must be a long number", ExecuteCode.ERROR);
        }
    }
}
