package command.inputcommand;

import command.Command;
import command.CommandType;

public abstract class InputCommand extends Command {

    public InputCommand(CommandType type) {
        super(type);
    }

    public abstract void execute();
}
