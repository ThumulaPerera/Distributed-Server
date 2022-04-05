package state;

import lombok.AccessLevel;
import lombok.Getter;

import java.util.*;

@Getter
public class LocalChatRoomModel extends ChatRoomModel {
    private final LocalClientModel owner;
    @Getter(AccessLevel.NONE)
    private final Map<String, LocalClientModel> clients;

    public LocalChatRoomModel(String id, LocalClientModel owner) {
        super(id);
        this.owner = owner;
        clients = Collections.synchronizedMap(new HashMap<>());
    }

    protected void addClient(LocalClientModel client) {
        clients.put(client.getId(), client);
    }

    protected LocalClientModel removeClient(String clientId) {
        return clients.remove(clientId);
    }

    protected LocalClientModel getClient(String id) {
        return clients.get(id);
    }

    protected boolean containsClient(String id) {
        return clients.containsKey(id);
    }

    protected List<LocalClientModel> getClients() {
        return new ArrayList<LocalClientModel>(clients.values());
    }
}
