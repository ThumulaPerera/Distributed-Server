package command;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.net.Socket;

@Getter
@Setter
public abstract class SocketExecutableCommand extends ExecutableCommand {
    @JsonIgnore
    private Socket socket;

    public SocketExecutableCommand(CommandType type) {
        super(type);
    }
}
