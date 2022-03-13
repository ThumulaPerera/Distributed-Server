package state;

import lombok.AccessLevel;
import lombok.Getter;
import serverserver.HeartbeatDetector;
import java.util.*;

@Getter
public class ServerModel {
    private final String id;
    private final String address;
    private final int clientsPort;
    private final int coordinationPort;
    private final HeartbeatDetector heartbeatDetector = new HeartbeatDetector();
    private final String mainHall;
    @Getter(AccessLevel.NONE)
    private final Map<String, ChatRoomModel> chatRooms;

    public ServerModel(String serverId, String serverAddress, int clientsPort, int coordinationPort) {
        this.id = serverId;
        this.address = serverAddress;
        this.clientsPort = clientsPort;
        this.coordinationPort = coordinationPort;
        chatRooms = Collections.synchronizedMap(new HashMap<>());
        mainHall = "MainHall-" + id;
        chatRooms.put(mainHall, new ChatRoomModel(mainHall, new ClientModel("")));
    }

    public void addChatRoom(ChatRoomModel chatRoom) {
        chatRooms.put(chatRoom.getId(), chatRoom);
    }

    public void removeChatRoom(ChatRoomModel chatRoom) {
        chatRooms.remove(chatRoom.getId());
    }

    public ChatRoomModel getChatRoom(String chatRoomId) {
        return chatRooms.get(chatRoomId);
    }

    public boolean containsChatRoom(String chatRoomId) {
        return chatRooms.containsKey(chatRoomId);
    }

    public Map<String, ChatRoomModel> getChatRooms() {
        return chatRooms;
    }

    protected ClientModel getClient(String clientId) {
        synchronized (chatRooms) {
            for (ChatRoomModel chatRoom : chatRooms.values()) {
                ClientModel client = chatRoom.getClient(clientId);
                if (client != null) {
                    return client;
                }
            }
        }
        return null;
    }

    protected ChatRoomModel getRoomOfClient(String clientId) {
        synchronized (chatRooms) {
            for (ChatRoomModel chatRoom : chatRooms.values()) {
                if (chatRoom.containsClient(clientId)) {
                    return chatRoom;
                }
            }
        }
        return null;
    }

    protected void moveClientToChatRoom(String clientId, String fromRoomID, String toRoomId){
        ChatRoomModel fromRoom = chatRooms.get(fromRoomID);
        ChatRoomModel toRoom = chatRooms.get(toRoomId);
        // TODO: synchronize?
        ClientModel client = fromRoom.removeClient(clientId);
        toRoom.addClient(client);
    }

    protected void addClientToChatRoom(ClientModel client, String chatRoomId) {
        chatRooms.get(chatRoomId).addClient(client);
    }

    protected void addClientToMainHall(ClientModel client) {
        addClientToChatRoom(client, mainHall);
    }
}
