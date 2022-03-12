package clientserver.command.servertoclient;

import command.Command;
import command.CommandType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RouteS2CCommand extends Command {
    private String roomid;
    private String host;
    private int port;

    public RouteS2CCommand() {
        super(CommandType.ROUTE);
    }

    public RouteS2CCommand(String roomid, String host, int port) {
        this();
        this.roomid = roomid;
        this.host = host;
        this.port = port;
    }
}
