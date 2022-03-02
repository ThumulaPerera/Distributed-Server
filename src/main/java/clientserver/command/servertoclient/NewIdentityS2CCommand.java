package clientserver.command.servertoclient;

import command.Command;
import command.CommandType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewIdentityS2CCommand extends Command {
    private boolean approved;

    public NewIdentityS2CCommand() {
        super(CommandType.NEW_IDENTITY);
    }

    public NewIdentityS2CCommand(boolean approved) {
        this();
        this.approved = approved;
    }

}
