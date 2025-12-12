package command;

import java.util.ArrayList;
import java.util.List;

public class CommandInvoker {
    private List<Command> commandHistory;

    public CommandInvoker() {
        this.commandHistory = new ArrayList<>();
    }

    public void executeCommand(Command command) {
        command.execute();
        commandHistory.add(command);
    }

    public void undoLastCommand() {
        if (!commandHistory.isEmpty()) {
            Command lastCommand = commandHistory.remove(commandHistory.size() - 1);
            lastCommand.undo();
        }
    }

    public List<Command> getCommandHistory() {
        return new ArrayList<>(commandHistory);
    }

    public void clearHistory() {
        commandHistory.clear();
    }
}
