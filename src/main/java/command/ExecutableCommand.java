package command;

public abstract class ExecutableCommand extends Command {

    public ExecutableCommand(CommandType type) {
        super(type);
    }

    public abstract void execute();
}
