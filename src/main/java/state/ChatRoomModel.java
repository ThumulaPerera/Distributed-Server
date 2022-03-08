package state;

import lombok.AccessLevel;
import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
public class ChatRoomModel {
    private final String id;
    private final ClientModel owner;
    @Getter(AccessLevel.NONE)
    private final Map<String, ClientModel> clients;

    public ChatRoomModel(String id, ClientModel owner) {
        this.id = id;
        this.owner = owner;
        clients = Collections.synchronizedMap(new HashMap<>());
    }

    public void addClient(ClientModel client) {
        clients.put(client.getId(), client);
    }

    public void removeClient(ClientModel client) {
        clients.remove(client.getId());
    }

    public ClientModel getClient(String id) {
        return clients.get(id);
    }

    public boolean containsClient(String id) {
        return clients.containsKey(id);
    }

    public Map<String, ClientModel> getClients() {
        return clients;
    }
}
