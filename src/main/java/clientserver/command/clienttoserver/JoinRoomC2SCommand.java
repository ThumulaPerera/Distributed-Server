package clientserver.command.clienttoserver;

import clientserver.Broadcaster;
import clientserver.command.servertoclient.RoomChangeS2CCommand;
import clientserver.command.servertoclient.RouteS2CCommand;
import command.ClientKnownExecutableCommand;
import command.Command;
import command.CommandType;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import state.ChatRoomModel;
import state.ServerModel;
import state.StateManager;
import state.StateManagerImpl;

@Getter
@Setter
public class JoinRoomC2SCommand extends ClientKnownExecutableCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(JoinRoomC2SCommand.class);
    private static final StateManager STATE_MANAGER = StateManagerImpl.getInstance();

    private String roomid;

    public JoinRoomC2SCommand() {
        super(CommandType.JOIN_ROOM);
    }

    @Override
    public Command execute() {
        String clientId = getClient().getId();

        ChatRoomModel currentRoom = STATE_MANAGER.getRoomOfClient(clientId);
        String currentRoomId = currentRoom.getId();

        // if client is the owner of current room
        if (isRoomOwner(clientId, currentRoom)) {
            return new RoomChangeS2CCommand(clientId, currentRoomId, currentRoomId);
        }

        // if requested chatroom exists locally
        if (STATE_MANAGER.getLocalChatRoom(roomid) != null) {
            STATE_MANAGER.moveClientToChatRoom(clientId, currentRoomId, roomid);

            RoomChangeS2CCommand roomChangeBroadcastMessage = new RoomChangeS2CCommand(clientId, currentRoomId, roomid);
            Broadcaster.broadcastToAll(currentRoomId, roomChangeBroadcastMessage);
            Broadcaster.broadcastToAll(roomid, roomChangeBroadcastMessage);
            return null;
        }

        // if requested chatroom exists remotely
        ServerModel remoteServer = STATE_MANAGER.getServerIfGlobalChatRoomExists(roomid);
        if (remoteServer != null) {
            STATE_MANAGER.removeLocalClientFromRoom(clientId, currentRoomId);
            RoomChangeS2CCommand roomChangeBroadcastMessage = new RoomChangeS2CCommand(clientId, currentRoomId, roomid);
            Broadcaster.broadcastToAll(currentRoomId, roomChangeBroadcastMessage);
            return new RouteS2CCommand(roomid, remoteServer.getAddress(), remoteServer.getClientsPort());
        }

        // if requested chatroom does not exist
        return new RoomChangeS2CCommand(clientId, currentRoomId, currentRoomId);
    }

    private boolean isRoomOwner(String clientId, ChatRoomModel room) {
        String roomOwnerId = room.getOwner().getId();
        return clientId.equals(roomOwnerId);
    }
}
