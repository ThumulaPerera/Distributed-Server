package state;

import java.net.Socket;
import java.util.List;

public interface StateManager {
    boolean isLeader();

    ServerModel getLeader();

    void setLeader(String leaderId);

    ServerModel getSelf();

    void setSelf(String selfId);

//    boolean checkValidityAndAddClient(String clientId, String serverId);
    void addLocalClient(String clientId, Socket socket);
    boolean checkValidityAndAddLocalClient(String clientId, Socket socket);
    boolean checkValidityAndAddGlobalClient(String clientId, String serverId);
    List<LocalClientModel> getLocalChatRoomClients(String chatRoomId);
    LocalClientModel getLocalClient(String clientId);

    boolean checkValidityAndAddRoom(String roomId, String serverId, String clientId);
    boolean deleteRoom(String roomId);

    ServerModel getServer(String serverId);
//    void addLocalChatRoom(String chatRoomId);
//    void removeLocalChatRoom(String chatRoomId);
//    Map<String, ChatRoomModel> getLocalChatRooms();
//    void addRemoteChatRoom(String chatRoomId, String serverId);
//    void removeRemoteChatRoom(String chatRoomId, String serverId);
//    Map<String, Map<String, ChatRoomModel>> getRemoteChatRooms();
//    Map<String, Map<String, ChatRoomModel>> getAllChatRooms();
//    void addLocalClient(String clientId, String chatRoomId);
//    void removeLocalClient(String clientId, String chatRoomId);
//    Map<String, Map<String, ClientModel>> getLocalClients();

}
