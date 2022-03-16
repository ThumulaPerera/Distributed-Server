package command;

import clientserver.ClientSender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ClientAndSenderKnownExecutableCommand extends ClientKnownExecutableCommand {
    @JsonIgnore
    private ClientSender sender;

    public ClientAndSenderKnownExecutableCommand(CommandType type) {
        super(type);
    }
}
