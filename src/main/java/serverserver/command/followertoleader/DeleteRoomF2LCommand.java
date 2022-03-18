package serverserver.command.followertoleader;

import command.Command;
import command.CommandType;
import command.S2SExecutableCommand;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serverserver.Sender;
import serverserver.command.leadertofollower.DeleteRoomL2FCommand;
import serverserver.command.leadertofollower.RemoveRoomL2FCommand;
import state.RefinedStateManagerImpl;
import state.StateManager;

@Getter
@Setter
public class DeleteRoomF2LCommand extends S2SExecutableCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteRoomF2LCommand.class);
    private static final StateManager STATE_MANAGER = RefinedStateManagerImpl.getInstance();

    private String roomid;

    public DeleteRoomF2LCommand() {
        super(CommandType.DELETE_ROOM_F2L);
    }

    public DeleteRoomF2LCommand(String roomid) {
        this();
        this.roomid = roomid;
    }

    @Override
    public String toString() {
        return "DeleteRoomF2LCommand [roomid=" + roomid + "]";
    }

    @Override
    public Command execute() {
        // executed by leader only
        LOGGER.debug("Executing Delete Room F2L with roomid: {}", roomid);

        return new DeleteRoomL2FCommand(roomid, deleteRoom());
    }

    private boolean deleteRoom() {
        STATE_MANAGER.deleteGlobalRoom(roomid);
        RemoveRoomL2FCommand removeRoomL2FCommand = new RemoveRoomL2FCommand(roomid);
        Sender.broadcastCommandToOtherFollowers(removeRoomL2FCommand, getOrigin());
        return true;
    }
}
