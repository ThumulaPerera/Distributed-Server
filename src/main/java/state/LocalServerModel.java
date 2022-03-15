package state;

import java.util.List;

public class LocalServerModel extends ServerModel {
    public LocalServerModel(String serverId, String serverAddress, int clientsPort, int coordinationPort) {
        super(serverId, serverAddress, clientsPort, coordinationPort);
        chatRooms.remove(getMainHall());
        chatRooms.put(getMainHall(), new LocalChatRoomModel(getMainHall(), new LocalClientModel("", null)));
    }

    protected LocalChatRoomModel getChatRoomByOwner(String owner) {
        for (ChatRoomModel chatRoom : chatRooms.values()) {
            LocalChatRoomModel localChatRoom = (LocalChatRoomModel) chatRoom;
            if (localChatRoom.getOwner().getId().equals(owner)) {
                return localChatRoom;
            }
        }
        return null;
    }

    protected LocalClientModel getClient(String clientId) {
        synchronized (chatRooms) {
            for (ChatRoomModel chatRoom : chatRooms.values()) {
                LocalChatRoomModel localChatRoom = (LocalChatRoomModel) chatRoom;
                LocalClientModel client = localChatRoom.getClient(clientId);
                if (client != null) {
                    return client;
                }
            }
        }
        return null;
    }

    protected LocalChatRoomModel getRoomOfClient(String clientId) {
        synchronized (chatRooms) {
            for (ChatRoomModel chatRoom : chatRooms.values()) {
                LocalChatRoomModel localChatRoom = (LocalChatRoomModel) chatRoom;
                if (localChatRoom.containsClient(clientId)) {
                    return localChatRoom;
                }
            }
        }
        return null;
    }

    protected void moveClientToChatRoom(String clientId, String fromRoomID, String toRoomId){
        LocalChatRoomModel fromRoom = (LocalChatRoomModel) chatRooms.get(fromRoomID);
        LocalChatRoomModel toRoom = (LocalChatRoomModel) chatRooms.get(toRoomId);
        // TODO: synchronize?
        LocalClientModel client = fromRoom.removeClient(clientId);
        toRoom.addClient(client);
    }

    protected void addClientToChatRoom(LocalClientModel client, String chatRoomId) {
        ((LocalChatRoomModel) chatRooms.get(chatRoomId)).addClient(client);
    }

    protected LocalClientModel removeClientFromChatRoom(String clientId, String chatRoomId) {
        return ((LocalChatRoomModel) chatRooms.get(chatRoomId)).removeClient(clientId);
    }

    protected void addClientToMainHall(LocalClientModel client) {
        addClientToChatRoom(client, getMainHall());
    }

    protected List<LocalClientModel> getClientsOfChatRoom(String chatRoomId) {
        return ((LocalChatRoomModel) chatRooms.get(chatRoomId)).getClients();
    }
}
