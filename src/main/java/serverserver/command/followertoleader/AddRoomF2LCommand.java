package serverserver.command.followertoleader;

import command.Command;
import command.CommandType;
import command.S2SExecutableCommand;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serverserver.Sender;
import serverserver.command.leadertofollower.AddRoomL2FCommand;
import serverserver.command.leadertofollower.NewRoomL2FCommand;
import state.RefinedStateManagerImpl;
import state.StateManager;

@Getter
@Setter
public class AddRoomF2LCommand extends S2SExecutableCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddRoomF2LCommand.class);
    private static final StateManager STATE_MANAGER = RefinedStateManagerImpl.getInstance();

    private String roomid;

    public AddRoomF2LCommand() {
        super(CommandType.ADD_ROOM_F2L);
    }

    public AddRoomF2LCommand(String roomid) {
        this();
        this.roomid = roomid;
    }

    @Override
    public Command execute() {
        // executed by leader only

        LOGGER.debug("Executing Add Room F2L with roomid: {}", roomid);

        boolean isAdded = checkAndAddRoom();
        if (isAdded) {
            NewRoomL2FCommand newRoomL2FCommand = new NewRoomL2FCommand(
                    roomid,
                    getOrigin()
            );
            Sender.broadcastCommandToOtherFollowers(newRoomL2FCommand, getOrigin());
        }

        return new AddRoomL2FCommand(roomid, isAdded);
    }

    private boolean checkAndAddRoom() {
        return STATE_MANAGER.checkValidityAndAddRemoteRoom(roomid, getOrigin());
    }
}
