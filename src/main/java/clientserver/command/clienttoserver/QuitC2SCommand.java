package clientserver.command.clienttoserver;

import clientserver.Broadcaster;
import clientserver.command.servertoclient.RoomChangeS2CCommand;
import command.ClientAndSenderKnownExecutableCommand;
import command.Command;
import command.CommandType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serverserver.Sender;
import serverserver.command.followertoleader.QuitNotificationF2LCommand;
import state.LocalChatRoomModel;
import state.RefinedStateManagerImpl;
import state.StateManager;

public class QuitC2SCommand extends ClientAndSenderKnownExecutableCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(QuitC2SCommand.class);
    private static final StateManager STATE_MANAGER = RefinedStateManagerImpl.getInstance();

    public QuitC2SCommand() {
        super(CommandType.QUIT);
    }

    @Override
    public Command execute() {
        String clientId = getClient().getId();

        LocalChatRoomModel ownedChatRoom = STATE_MANAGER.getRoomOwnedByClient(clientId);
        if (ownedChatRoom != null) {
            // TODO: follow delete room protocol
        } else {
            String currentChatRoomId = STATE_MANAGER.getRoomOfClient(clientId).getId();
            STATE_MANAGER.removeLocalClientFromRoom(clientId, currentChatRoomId);
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

        // remove client from global list
        if (STATE_MANAGER.isLeader()){
            STATE_MANAGER.removeClientFromAllClients(clientId);
        } else {
            Sender.sendCommandToLeader(new QuitNotificationF2LCommand(clientId));
        }

        return null;
    }
}
