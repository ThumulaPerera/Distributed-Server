package clientserver.command.servertoclient;

import command.Command;
import command.CommandType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageS2CCommand extends Command {
    private String identity;
    private String content;

    public MessageS2CCommand() {
        super(CommandType.MESSAGE);
    }

    public MessageS2CCommand(String identity, String content) {
        this();
        this.identity = identity;
        this.content = content;
    }
}
