package command;

import lombok.Getter;

@Getter
public abstract class Command {
    private CommandType type;

    public Command(CommandType type) {
        this.type = type;
    }

}
