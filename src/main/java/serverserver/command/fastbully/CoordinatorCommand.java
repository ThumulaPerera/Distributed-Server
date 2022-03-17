package serverserver.command.fastbully;

import command.Command;
import command.CommandType;
import command.S2SExecutableCommand;
import lombok.Getter;
import lombok.Setter;
import serverserver.Sender;
import serverserver.command.followertoleader.NewDataF2LCommand;
import state.ChatRoomModel;
import state.ClientModel;
import state.RefinedStateManagerImpl;
import state.StateManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CoordinatorCommand extends S2SExecutableCommand {
    private static final StateManager STATE_MANAGER = RefinedStateManagerImpl.getInstance();

    @Getter @Setter private String from;
    public CoordinatorCommand() {
        super(CommandType.COORDINATOR);
    }

    @Override
    public Command execute() {
        STATE_MANAGER.setLeader(from);
        STATE_MANAGER.setElectionAllowed(false);
        List<String> allLocalClients = STATE_MANAGER.getAllLocalClients()
                .stream()
                .map(ClientModel::getId)
                .collect(Collectors.toList());
        List<String> allLocalRooms = STATE_MANAGER.getAllLocalChatRooms()
                .stream()
                .map(ChatRoomModel::getId)
                .collect(Collectors.toList());
        NewDataF2LCommand newDataF2LCommand = new NewDataF2LCommand();
        newDataF2LCommand.setClients(allLocalClients);
        newDataF2LCommand.setChatRooms(allLocalRooms);
        Sender.sendCommandToLeader(newDataF2LCommand);
        return null;
    }
}
