package clientserver.command.servertoclient;

import command.Command;
import command.CommandType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoomContentsS2CCommand extends Command {
    private String roomid;
    private String owner;
    private List<String> identities;


    public RoomContentsS2CCommand() {
        super(CommandType.ROOM_CONTENTS);
    }

    public RoomContentsS2CCommand(String roomId, String owner, List<String> identities) {
        this();
        this.roomid = roomId;
        this.owner = owner;
        this.identities = identities;
    }
}
