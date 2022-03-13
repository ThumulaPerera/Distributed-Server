package clientserver.command.clienttoserver;

import clientserver.command.servertoclient.CreateRoomS2CCommand;
import clientserver.command.servertoclient.DeleteRoomS2CCommand;
import command.ClientKnownExecutableCommand;
import command.Command;
import command.CommandType;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serverserver.Sender;
import serverserver.command.followertoleader.AddRoomF2LCommand;
import serverserver.command.followertoleader.DeleteRoomF2LCommand;
import serverserver.command.leadertofollower.AddRoomL2FCommand;
import serverserver.command.leadertofollower.DeleteRoomL2FCommand;
import state.StateManager;
import state.StateManagerImpl;

@Getter
@Setter
public class DeleteRoomC2SCommand extends ClientKnownExecutableCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(NewIdentityC2SCommand.class);
    private static final StateManager STATE_MANAGER = StateManagerImpl.getInstance();

    private String roomid;

    public DeleteRoomC2SCommand() {
        super(CommandType.DELETE_ROOM);
    }

    @Override
    public Command execute() {
        LOGGER.debug("Executing client request for delete room with identity: {}", roomid);

        // If such room is available and the owner is client:
        //      send leader to delete from all
        // Else: return false

        String clientId = getClient().getId();
        String currentOwnedRoom = STATE_MANAGER.getSelf().getChatRoomByOwner(clientId);
        boolean isApproved = false;
        if (currentOwnedRoom != null) {
            if (currentOwnedRoom.equals(roomid)) {
                isApproved = deleteRoom();
                if (isApproved) {
                    joinMainHallRoom();
                }
            }
        }

        return new DeleteRoomS2CCommand(isApproved, roomid);
    }

    private void joinMainHallRoom() {
        //TODO: Join MainHall
    }


    private boolean deleteRoom() {

        if (!STATE_MANAGER.getSelf().containsChatRoom(roomid)) {
            return false;
        } else {
            if (STATE_MANAGER.isLeader()) {
                return STATE_MANAGER.deleteRoom(roomid);

            } else {
                DeleteRoomF2LCommand deleteRoomF2LCommand = new DeleteRoomF2LCommand(roomid);
                LOGGER.debug(deleteRoomF2LCommand.toString());
                Command response = Sender.sendCommandToLeaderAndReceive(deleteRoomF2LCommand);
                if (response instanceof DeleteRoomL2FCommand) {
                    return ((DeleteRoomL2FCommand) response).isApproved();
                }
            }
            return false;
        }


    }
}
