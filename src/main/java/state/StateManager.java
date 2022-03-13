package state;

import java.net.Socket;
import java.util.List;

public interface StateManager {
    boolean isLeader();
    ServerModel getLeader();
    void setLeader(String leaderId);
    ServerModel getSelf();
    void setSelf(String selfId);
    void addNewLocalClient(String clientId, ClientSender sender);
    void addMoveJoinLocalClient(String clientId, ClientSender sender, String chatRoomId);
    ClientModel removeLocalClientFromRoom(String clientId, String chatRoomId);
    boolean checkAvailabilityAndAddNewLocalClient(String clientId, ClientSender sender);
    boolean checkAvailabilityAndAddGlobalClient(String clientId, String serverId);
    List<LocalClientModel> getLocalChatRoomClients(String chatRoomId);
    LocalClientModel getLocalClient(String clientId);
    ChatRoomModel getLocalChatRoom(String chatRoomId);
    boolean isIdLocallyAvailable(String clientId);
    ServerModel getServer(String serverId);
    ChatRoomModel getRoomOfClient(String clientId);
    ServerModel getServerIfGlobalChatRoomExists(String chatRoomId);
    void moveClientToChatRoom(String clientId, String fromRoomID, String toRoomId);
    List<ChatRoomModel> getAllChatRooms();
    boolean checkValidityAndAddRoom(String roomId, String serverId, String clientId);
    boolean deleteRoom(String roomId);

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
