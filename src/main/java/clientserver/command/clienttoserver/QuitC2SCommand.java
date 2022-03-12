package clientserver.command.clienttoserver;

import command.ClientKnownExecutableCommand;
import command.Command;
import command.CommandType;

public class QuitC2SCommand extends ClientKnownExecutableCommand {
    public QuitC2SCommand() {
        super(CommandType.QUIT);
    }

    @Override
    public Command execute() {
        // remove client from local list
        // remove client from global list
        // if client owns a chat room
        // follow delete room protocol
        return null;
    }
}
