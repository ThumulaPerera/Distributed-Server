package state;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.net.Socket;

@Getter
public class LocalClientModel extends ClientModel {
    private final Socket socket;

    public LocalClientModel(String id, Socket socket) {
        super(id);
        this.socket = socket;
    }
}
