package serverserver.command.followertoleader;

import clientserver.command.clienttoserver.QuitC2SCommand;
import command.Command;
import command.CommandType;
import command.S2SExecutableCommand;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import state.RefinedStateManagerImpl;
import state.StateManager;

@Getter
@Setter
public class QuitNotificationF2LCommand extends S2SExecutableCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(QuitNotificationF2LCommand.class);
    private static final StateManager STATE_MANAGER = RefinedStateManagerImpl.getInstance();

    private String identity;

    public QuitNotificationF2LCommand() {
        super(CommandType.QUIT_NOTIFICATION);
    }

    public QuitNotificationF2LCommand(String identity) {
        this();
        this.identity = identity;
    }

    @Override
    public Command execute() {
        STATE_MANAGER.removeClientFromAllClients(identity);
        return null;
    }
}
