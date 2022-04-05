package command;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import state.LocalClientModel;

@Getter
@Setter
public abstract class ClientKnownExecutableCommand extends ExecutableCommand {
    @JsonIgnore
    private LocalClientModel client;

    public ClientKnownExecutableCommand(CommandType type) {
        super(type);
    }
}
