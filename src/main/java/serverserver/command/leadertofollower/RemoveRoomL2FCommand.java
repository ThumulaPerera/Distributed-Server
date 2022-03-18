package serverserver.command.leadertofollower;

import command.Command;
import command.CommandType;
import command.S2SExecutableCommand;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import state.RefinedStateManagerImpl;
import state.StateManager;

@Getter
@Setter
public class RemoveRoomL2FCommand extends S2SExecutableCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(RemoveRoomL2FCommand.class);
    private static final StateManager STATE_MANAGER = RefinedStateManagerImpl.getInstance();

    private String roomid;

    public RemoveRoomL2FCommand() {
        super(CommandType.REMOVE_ROOM_L2F);
    }

    public RemoveRoomL2FCommand(String roomid) {
        this();
        this.roomid = roomid;

    }

    @Override
    public Command execute() {
        LOGGER.debug("Executing Remove Room L2F with roomid: {}", roomid);

        STATE_MANAGER.deleteGlobalRoom(roomid);

        return this;
    }
}
