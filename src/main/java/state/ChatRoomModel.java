package state;

import lombok.AccessLevel;
import lombok.Getter;

import java.util.*;

@Getter
public class ChatRoomModel {
    private final String id;
    private final ClientModel owner;
    private final ServerModel server;
    @Getter(AccessLevel.NONE)
    private final Map<String, ClientModel> clients;

    public ChatRoomModel(String id, ClientModel owner, ServerModel server) {
        this.id = id;
        this.owner = owner;
        this.server = server;
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

    public List<ClientModel> getClients() {
        return new ArrayList<ClientModel>(clients.values());
    }
}
