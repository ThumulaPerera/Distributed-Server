package clientserver.command.clienttoserver;

import clientserver.Broadcaster;
import clientserver.command.servertoclient.DeleteRoomS2CCommand;
import clientserver.command.servertoclient.RoomChangeS2CCommand;
import command.ClientAndSenderKnownExecutableCommand;
import command.ClientKnownExecutableCommand;
import command.Command;
import command.CommandType;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serverserver.Sender;
import serverserver.command.followertoleader.DeleteRoomF2LCommand;
import serverserver.command.leadertofollower.DeleteRoomL2FCommand;
import serverserver.command.leadertofollower.NewRoomL2FCommand;
import serverserver.command.leadertofollower.RemoveRoomL2FCommand;
import state.*;

import java.util.List;

@Getter
@Setter
public class DeleteRoomC2SCommand extends ClientAndSenderKnownExecutableCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(NewIdentityC2SCommand.class);
    private static final StateManager STATE_MANAGER = RefinedStateManagerImpl.getInstance();

    private String roomid;

    public DeleteRoomC2SCommand() {
        super(CommandType.DELETE_ROOM);
    }

    @Override
    public Command execute() {
        LOGGER.debug("Executing client request for delete room with identity: {}", roomid);

        String clientId = getClient().getId();
        if (STATE_MANAGER.getRoomOwnedByClient(clientId) == null) return new DeleteRoomS2CCommand(false, roomid);

        String currentOwnedRoom = STATE_MANAGER.getRoomOwnedByClient(clientId).getId();
        String currentRoom = STATE_MANAGER.getRoomOfClient(clientId).getId();

        if (roomid.equals(currentOwnedRoom) && roomid.equals(currentRoom)) {
            LOGGER.debug("Room {} is deletable", roomid);
            if (deleteRoom()) {
                return new DeleteRoomS2CCommand(true, roomid);
            }
        }
        LOGGER.debug("Cannot delete room [clientId:{}, roomId:{}, currentOwnedRoom:{}, curentRoom:{}]", clientId, roomid, currentOwnedRoom, currentRoom);
        return new DeleteRoomS2CCommand(false, roomid);

    }


    private boolean deleteRoom() {

        if (STATE_MANAGER.getLocalChatRoom(roomid) == null) {
            return false;
        } else {
            List<LocalClientModel> roomMembers = STATE_MANAGER.getLocalChatRoomClients(roomid);
            joinMainHallRoom(roomMembers, roomid);

            if (STATE_MANAGER.isLeader()) {
                STATE_MANAGER.deleteLocalRoom(roomid);
                RemoveRoomL2FCommand removeRoomL2FCommand = new RemoveRoomL2FCommand(roomid);
                Sender.broadcastCommandToAllFollowers(removeRoomL2FCommand);
                LOGGER.debug("Deleted from Leader");
            } else {
                // notify leader
                DeleteRoomF2LCommand deleteRoomF2LCommand = new DeleteRoomF2LCommand(roomid);
                LOGGER.debug(deleteRoomF2LCommand.toString());
                Sender.sendCommandToLeader(deleteRoomF2LCommand);
                STATE_MANAGER.deleteLocalRoom(roomid);
            }
            return true;
        }
    }

    private void joinMainHallRoom(List<LocalClientModel> roomMembers, String formerRoomId) {
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


}
