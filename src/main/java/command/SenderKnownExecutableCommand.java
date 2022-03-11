package command;

import clientserver.ClientSender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class SenderKnownExecutableCommand extends ExecutableCommand {
    @JsonIgnore
    private ClientSender sender;

    public SenderKnownExecutableCommand(CommandType type) {
        super(type);
    }
}
