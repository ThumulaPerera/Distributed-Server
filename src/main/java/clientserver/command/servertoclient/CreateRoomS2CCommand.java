package clientserver.command.servertoclient;

import command.Command;
import command.CommandType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRoomS2CCommand extends Command {
    private boolean approved;
    private String roomid;

    public CreateRoomS2CCommand() {
        super(CommandType.CREATE_ROOM);
    }

    public CreateRoomS2CCommand(boolean approved, String roomId) {
        this();
        this.approved = approved;
        this.roomid = roomId;
    }
}
