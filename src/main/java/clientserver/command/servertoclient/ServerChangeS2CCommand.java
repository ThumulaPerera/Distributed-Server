package clientserver.command.servertoclient;

import command.Command;
import command.CommandType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServerChangeS2CCommand extends Command {
    private boolean approved;
    private String serverid;

    public ServerChangeS2CCommand() {
        super(CommandType.SERVER_CHANGE);
    }

    public ServerChangeS2CCommand(boolean approved, String serverId) {
        this();
        this.approved = approved;
        this.serverid = serverId;
    }
}
