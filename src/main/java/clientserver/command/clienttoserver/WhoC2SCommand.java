package clientserver.command.clienttoserver;

import clientserver.command.servertoclient.RoomContentsS2CCommand;
import command.ClientKnownExecutableCommand;
import command.Command;
import command.CommandType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import state.*;

import java.util.List;

public class WhoC2SCommand extends ClientKnownExecutableCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(WhoC2SCommand.class);
    private static final StateManager STATE_MANAGER = RefinedStateManagerImpl.getInstance();

    public WhoC2SCommand() {
        super(CommandType.WHO);
    }

    @Override
    public Command execute() {
        LocalChatRoomModel room = STATE_MANAGER.getRoomOfClient(getClient().getId());

        String roomId = room.getId();
        String ownerId = room.getOwner().getId();

        List<LocalClientModel> clients = STATE_MANAGER.getLocalChatRoomClients(roomId);
        List<String> clientIds = clients.stream().map(LocalClientModel::getId).toList();

        return new RoomContentsS2CCommand(roomId, ownerId, clientIds);
    }
}
