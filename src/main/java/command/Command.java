package command;

public abstract class Command {
    private CommandType type;

    public Command(CommandType type) {
        this.type = type;
    }

    public CommandType getType() {
        return type;
    }
}
