package serverserver.command.leadertofollower;

import command.Command;
import command.CommandType;
import command.S2SExecutableCommand;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Setter
public class DeleteRoomL2FCommand extends S2SExecutableCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteRoomL2FCommand.class);

    private String roomid;
    private boolean approved;

    public DeleteRoomL2FCommand() {
        super(CommandType.DELETE_ROOM_L2F);
    }

    public DeleteRoomL2FCommand(String roomid, boolean approved) {
        this();
        this.roomid = roomid;
        this.approved = approved;
    }

    @Override
    public Command execute() {
        LOGGER.debug("Executing Delete Room L2F with identity: {}", roomid);
        return this;
    }
}
