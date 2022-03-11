package command;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import state.ClientModel;

@Getter
@Setter
public abstract class ClientKnownExecutableCommand extends ExecutableCommand {
    @JsonIgnore
    private ClientModel client;

    public ClientKnownExecutableCommand(CommandType type) {
        super(type);
    }
}
