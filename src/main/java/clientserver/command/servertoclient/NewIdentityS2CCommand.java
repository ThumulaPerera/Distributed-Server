package clientserver.command.servertoclient;

import command.Command;
import command.CommandType;

public class NewIdentityS2CCommand extends Command {
    private boolean approved;

    public NewIdentityS2CCommand() {
        super(CommandType.NEW_IDENTITY);
    }

    public NewIdentityS2CCommand(boolean approved) {
        this();
        this.approved = approved;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
