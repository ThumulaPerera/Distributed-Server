package clientserver.command.clienttoserver;

import clientserver.command.servertoclient.DeleteRoomS2CCommand;
import command.Command;
import command.CommandType;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import state.*;

import java.util.List;

@Getter
@Setter
public class DeleteRoomC2SCommand extends RoomDeletableC2SCommand {
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

        if (!currentOwnedRoom.equals(currentRoom)) {
            LOGGER.error("Client {} is not in the owned room {}", clientId, currentOwnedRoom);
            throw new IllegalStateException("Client is not in the owned room");
        }

        if (roomid.equals(currentOwnedRoom) && roomid.equals(currentRoom)) {
            LOGGER.debug("Room {} is deletable", roomid);
            if (joinMainHallAndDeleteRoom()) {
                return new DeleteRoomS2CCommand(true, roomid);
            }
        }
        LOGGER.debug("Cannot delete room [clientId:{}, roomId:{}, currentOwnedRoom:{}, curentRoom:{}]", clientId, roomid, currentOwnedRoom, currentRoom);
        return new DeleteRoomS2CCommand(false, roomid);

    }


    private boolean joinMainHallAndDeleteRoom() {

        if (STATE_MANAGER.getLocalChatRoom(roomid) == null) {
            return false;
        } else {
            List<LocalClientModel> roomMembers = STATE_MANAGER.getLocalChatRoomClients(roomid);
            moveMembersToMainHall(roomMembers, roomid);
            deleteRoom(roomid);
            return true;
        }
    }



}
