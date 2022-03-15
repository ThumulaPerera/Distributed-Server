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
public class NewRoomL2FCommand extends S2SExecutableCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(NewRoomL2FCommand.class);
    private static final StateManager STATE_MANAGER = RefinedStateManagerImpl.getInstance();

    private String roomid;
    private String managingserver;

    public NewRoomL2FCommand() {
        super(CommandType.NEW_ROOM_L2F);
    }

    public NewRoomL2FCommand(String roomid, String managingserver) {
        this();
        this.roomid = roomid;
        this.managingserver = managingserver;
    }

    @Override
    public Command execute() {
        LOGGER.debug("Executing New Room L2F with roomid: {}, ownerserver: {}", roomid, managingserver);

        STATE_MANAGER.addRemoteChatRoom(roomid, managingserver);

        return null;
    }
}
