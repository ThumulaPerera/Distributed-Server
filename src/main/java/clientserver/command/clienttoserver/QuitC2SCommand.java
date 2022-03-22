package clientserver.command.clienttoserver;

import clientserver.Broadcaster;
import clientserver.command.servertoclient.RoomChangeS2CCommand;
import command.Command;
import command.CommandType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serverserver.FastBully;
import serverserver.Sender;
import serverserver.command.followertoleader.QuitNotificationF2LCommand;
import state.LocalChatRoomModel;
import state.LocalClientModel;
import state.RefinedStateManagerImpl;
import state.StateManager;

import java.util.List;

public class QuitC2SCommand extends RoomDeletableC2SCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(QuitC2SCommand.class);
    private static final StateManager STATE_MANAGER = RefinedStateManagerImpl.getInstance();

    public QuitC2SCommand() {
        super(CommandType.QUIT);
    }

    @Override
    public Command execute() {
        String clientId = getClient().getId();

        LocalChatRoomModel ownedChatRoom = STATE_MANAGER.getRoomOwnedByClient(clientId);
        String currentChatRoomId = STATE_MANAGER.getRoomOfClient(clientId).getId();

        STATE_MANAGER.removeLocalClientFromRoom(clientId, currentChatRoomId);
        sendDisconnectMessage(clientId, currentChatRoomId);

        if (ownedChatRoom != null) {
            // follow delete room protocol
            String ownedChatRoomId = ownedChatRoom.getId();

            if (!ownedChatRoomId.equals(currentChatRoomId)) {
                LOGGER.error("Client {} is not in the owned room {}", clientId, ownedChatRoomId);
                throw new IllegalStateException("Client is not in the owned room");
            }

            List<LocalClientModel> roomMembers = STATE_MANAGER.getLocalChatRoomClients(ownedChatRoomId);

            moveMembersToMainHall(roomMembers, ownedChatRoomId);
            deleteRoom(ownedChatRoomId);

        }

        // remove client from global list
        if (STATE_MANAGER.isLeader()){
            STATE_MANAGER.removeClientFromAllClients(clientId);
        } else {
            try {
                Sender.sendCommandToLeader(new QuitNotificationF2LCommand(clientId));
            }catch (Exception e){
                FastBully.startElection();
            }
        }

        return null;
    }

    private void sendDisconnectMessage(String clientId, String currentChatRoomId) {
        RoomChangeS2CCommand roomChangeS2CCommand = new RoomChangeS2CCommand(
                clientId,
                currentChatRoomId,
                ""
        );
        Broadcaster.broadcastToAll(currentChatRoomId, roomChangeS2CCommand);
        if (getSender() != null) {
            getSender().send(roomChangeS2CCommand);
        }
    }

}
