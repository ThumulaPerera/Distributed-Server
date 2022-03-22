package serverserver.command.leadertofollower;

import command.Command;
import command.CommandType;
import command.S2SExecutableCommand;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServerChangeNotedL2FCommand extends S2SExecutableCommand {
    private boolean noted;

    public ServerChangeNotedL2FCommand() {
        super(CommandType.SERVER_CHANGE_NOTED);
    }

    public ServerChangeNotedL2FCommand(boolean noted) {
        this();
        this.noted = noted;
    }

    @Override
    public Command execute() {
        return null;
    }
}
