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
        chatRooms.put(mainHall, new ChatRoomModel(mainHall, null, this));
    }

    public void addChatRoom(ChatRoomModel chatRoom) {
        chatRooms.put(chatRoom.getId(), chatRoom);
    }

    public void removeChatRoom(String roomId) {
        chatRooms.remove(roomId);
    }

    public ChatRoomModel getChatRoom(String chatRoomId) {
        return chatRooms.get(chatRoomId);
    }

    public String getChatRoomByOwner(String owner) {
        for (Map.Entry<String, ChatRoomModel> room : chatRooms.entrySet()) {
            if (room.getValue().getOwner() != null) {
                if (room.getValue().getOwner().getId().equals(owner)) return room.getValue().getId();
            }
        }
        return null;
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

    public void addClientToMainHall(ClientModel client) {
        chatRooms.get(mainHall).addClient(client);
    }
}
