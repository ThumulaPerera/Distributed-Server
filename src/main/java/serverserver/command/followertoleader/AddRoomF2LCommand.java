package serverserver.command.followertoleader;

import command.Command;
import command.CommandType;
import command.S2SExecutableCommand;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serverserver.command.leadertofollower.AddRoomL2FCommand;
import state.StateManager;
import state.StateManagerImpl;

@Getter
@Setter
public class AddRoomF2LCommand extends S2SExecutableCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddRoomF2LCommand.class);
    private static final StateManager STATE_MANAGER = StateManagerImpl.getInstance();

    private String roomid;
    private String clientId;

    public AddRoomF2LCommand() {
        super(CommandType.ADD_ROOM_F2L);
    }

    public AddRoomF2LCommand(String roomid, String clientId) {
        this();
        this.roomid = roomid;
        this.clientId = clientId;
    }

    @Override
    public Command execute() {
        // executed by leader only

        LOGGER.debug("Executing Add Room F2L with roomid: {}", roomid);

        return new AddRoomL2FCommand(roomid, checkAndAddRoom());
    }

    private boolean checkAndAddRoom() {
        boolean isAdded = STATE_MANAGER.checkValidityAndAddRoom(roomid, getOrigin(), clientId);

        if (isAdded) {
//            TODO: Send newroom to all the servers except the origin server
        }
        return isAdded;
    }
}
