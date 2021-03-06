package com.bhma.server.commands;

import com.bhma.common.data.SpaceMarine;
import com.bhma.common.exceptions.IllegalKeyException;
import com.bhma.common.exceptions.InvalidCommandArguments;
import com.bhma.common.util.CommandObjectRequirement;
import com.bhma.common.util.ExecuteCode;
import com.bhma.common.util.ServerResponse;
import com.bhma.server.collectionmanagers.CollectionManager;

/**
 * update command
 */
public class UpdateCommand extends Command {
    private final CollectionManager collectionManager;

    public UpdateCommand(CollectionManager collectionManager) {
        super("update", "обновить значение элемента коллекции, id которого равен заданному",
                CommandObjectRequirement.SPACE_MARINE, true);
        this.collectionManager = collectionManager;
    }

    /**
     * updates element of collection whose id equal entered one
     *
     * @param argument must be a number
     * @throws NumberFormatException   if argument is not a number
     * @throws InvalidCommandArguments if argument is empty
     * @throws IllegalKeyException     if there's no element with entered id
     */
    public ServerResponse execute(String argument, Object spaceMarine, String username) throws NumberFormatException,
            InvalidCommandArguments, IllegalKeyException {
        if (argument.isEmpty() || spaceMarine == null || spaceMarine.getClass() != SpaceMarine.class) {
            throw new InvalidCommandArguments();
        }
        try {
            long id = Long.parseLong(argument);
            if (!collectionManager.containsId(id)) {
                throw new IllegalKeyException("There's no value with that id.");
            }
            if (!collectionManager.getById(id).getOwnerUsername().equals(username)) {
                throw new IllegalKeyException("Object with that key belong to the another user");
            }
            if (!collectionManager.updateID(id, (SpaceMarine) spaceMarine)) {
                return new ServerResponse("Cannot update element", ExecuteCode.SERVER_ERROR);
            }
            return new ServerResponse(ExecuteCode.SUCCESS);
        } catch (NumberFormatException e) {
            return new ServerResponse("the argument must be a long number", ExecuteCode.ERROR);
        }
    }
}
