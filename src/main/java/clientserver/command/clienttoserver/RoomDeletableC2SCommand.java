package clientserver.command.clienttoserver;

import clientserver.Broadcaster;
import clientserver.command.servertoclient.RoomChangeS2CCommand;
import command.ClientAndSenderKnownExecutableCommand;
import command.Command;
import command.CommandType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serverserver.Sender;
import serverserver.command.followertoleader.DeleteRoomF2LCommand;
import serverserver.command.leadertofollower.DeleteRoomL2FCommand;
import serverserver.command.leadertofollower.RemoveRoomL2FCommand;
import state.LocalClientModel;
import state.RefinedStateManagerImpl;
import state.StateManager;

import java.util.List;

public abstract class RoomDeletableC2SCommand extends ClientAndSenderKnownExecutableCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(NewIdentityC2SCommand.class);
    private static final StateManager STATE_MANAGER = RefinedStateManagerImpl.getInstance();

    public RoomDeletableC2SCommand(CommandType type) {
        super(type);
    }

    protected void moveMembersToMainHall(List<LocalClientModel> roomMembers, String formerRoomId) {
        String mainHallId = STATE_MANAGER.getSelf().getMainHall();
        LOGGER.debug("Moving all the clients in the room to {}", mainHallId);

        for (LocalClientModel client : roomMembers) {
            RoomChangeS2CCommand roomChangeBroadcastMessage = new RoomChangeS2CCommand(client.getId(), formerRoomId, mainHallId);
            Broadcaster.broadcastToAll(mainHallId, roomChangeBroadcastMessage);
            Broadcaster.broadcastToOthers(formerRoomId, roomChangeBroadcastMessage, client.getId());
        }

        for (LocalClientModel client : roomMembers) {
            LOGGER.debug("Room Member {} Moved to {}", client.getId(), mainHallId);
            STATE_MANAGER.moveClientToChatRoom(client.getId(), formerRoomId, mainHallId);
            client.getSender().send(new RoomChangeS2CCommand(client.getId(), formerRoomId, mainHallId));
        }
    }

    protected void deleteRoom(String roomId) {
        if (STATE_MANAGER.isLeader()) {
            STATE_MANAGER.deleteLocalRoom(roomId);
            RemoveRoomL2FCommand removeRoomL2FCommand = new RemoveRoomL2FCommand(roomId);
            Sender.broadcastCommandToAllFollowers(removeRoomL2FCommand);
            LOGGER.debug("Deleted from Leader");
        } else {
            // notify leader
            DeleteRoomF2LCommand deleteRoomF2LCommand = new DeleteRoomF2LCommand(roomId);
            LOGGER.debug(deleteRoomF2LCommand.toString());
            Command response = Sender.sendCommandToLeaderAndReceive(deleteRoomF2LCommand);
            if (response instanceof DeleteRoomL2FCommand) {
                ((DeleteRoomL2FCommand) response).execute();
            }
        }
    }
}
