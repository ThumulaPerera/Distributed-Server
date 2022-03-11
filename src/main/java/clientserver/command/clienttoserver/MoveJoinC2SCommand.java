package clientserver.command.clienttoserver;

import command.Command;
import command.CommandType;
import command.ExecutableCommand;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Setter
public class MoveJoinC2SCommand extends ExecutableCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(MoveJoinC2SCommand.class);

    private String former;
    private String roomid;
    private String identity;

    public MoveJoinC2SCommand() {
        super(CommandType.MOVE_JOIN);
    }

    @Override
    public Command execute() {
        return null;
    }
}
