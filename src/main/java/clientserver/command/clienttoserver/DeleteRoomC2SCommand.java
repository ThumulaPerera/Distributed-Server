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

        // If such room is available and the owner is client:
        //      send leader to delete from all
        // Else: return false

        String clientId = getClient().getId();
        String currentOwnedRoom = STATE_MANAGER.getRoomOwnedByClient(clientId).getId();
        String currentRoom = STATE_MANAGER.getRoomOfClient(clientId).getId();
        if (roomid.equals(currentOwnedRoom) && roomid.equals(currentRoom)) {
            LOGGER.debug("==================valid to delete");
            if (deleteRoom()) {
                return new DeleteRoomS2CCommand(true, roomid);
            }
        }
        LOGGER.debug("Invalid - clientId:{}, roomId:{}, currentOwnedRoom:{}, curentRoom:{}", clientId, roomid, currentOwnedRoom, currentRoom);
        return new DeleteRoomS2CCommand(false, roomid);


    }


    private boolean deleteRoom() {

        if (STATE_MANAGER.getLocalChatRoom(roomid) == null) {
            return false;
        } else {
            ServerModel myServer = STATE_MANAGER.getSelf();
            ChatRoomModel room = STATE_MANAGER.getLocalChatRoom(roomid);
            List<LocalClientModel> roomMembers = STATE_MANAGER.getLocalChatRoomClients(roomid);
            // TODO: move all clients to mainhall
            joinMainHallRoom(roomMembers, roomid);

            if (STATE_MANAGER.isLeader()) {
                STATE_MANAGER.deleteGlobalRoom(roomid);
                // TODO: If true broadcast to all servers to remove the room
                LOGGER.info("Deleted from Leader");
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
        LOGGER.info("==========Move all to Mainhall");
        String clientId = getClient().getId();
        String mainHallId = STATE_MANAGER.getSelf().getMainHall();

        for (LocalClientModel client : roomMembers) {
            LOGGER.info("========== Moved to Mainhall - Client:{}", client.getId());
            STATE_MANAGER.moveClientToChatRoom(client.getId(), formerRoomId, mainHallId);
            RoomChangeS2CCommand roomChangeBroadcastMessage = new RoomChangeS2CCommand(client.getId(), formerRoomId, mainHallId);
            Broadcaster.broadcastToAll(mainHallId, roomChangeBroadcastMessage);

        }

    }


}
