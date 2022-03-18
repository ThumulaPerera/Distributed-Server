package state;

import clientserver.ClientSender;

import java.util.List;

public interface StateManager {
    boolean isLeader();
    ServerModel getLeader();
    void setLeader(String leaderId);
    ServerModel getSelf();
    void addNewLocalClient(String clientId, ClientSender sender);
    void addMoveJoinLocalClient(String clientId, ClientSender sender, String chatRoomId);
    LocalClientModel removeLocalClientFromRoom(String clientId, String chatRoomId);
    boolean checkAvailabilityAndAddNewLocalClient(String clientId, ClientSender sender);
    boolean checkAvailabilityAndAddGlobalClient(String clientId, String serverId);
    List<LocalClientModel> getLocalChatRoomClients(String chatRoomId);
    LocalClientModel getLocalClient(String clientId);
    LocalChatRoomModel getLocalChatRoom(String chatRoomId);
    boolean isIdLocallyAvailable(String clientId);
    ServerModel getServer(String serverId);
    LocalChatRoomModel getRoomOfClient(String clientId);
    LocalChatRoomModel getRoomOwnedByClient(String clientId);
    ServerModel getServerIfGlobalChatRoomExists(String chatRoomId);
    void moveClientToChatRoom(String clientId, String fromRoomID, String toRoomId);
    List<ChatRoomModel> getAllChatRooms();
    void addLocalRoom(String roomId, LocalClientModel owner);
    boolean checkValidityAndAddLocalRoom(String roomId, LocalClientModel owner);
    boolean checkValidityAndAddRemoteRoom(String roomId, String managingServerId);
    boolean deleteLocalRoom(String roomId);
    void deleteGlobalRoom(String roomId);
    List<ServerModel> getAllRemoteServers();
    void addRemoteChatRoom(String chatRoomId, String managingServerId);
    List<LocalChatRoomModel> getAllLocalChatRooms();
    List<LocalClientModel> getAllLocalClients();
    boolean isRoomIdAvailable(String roomId);
    void removeClientFromAllClients(String clientId);

    // for fast bully
    void addAvailableServerId(String serverId);
    void setLeaderOnStartup();
    List<String> getAvailableServerIds();
    void setElectionAllowed(boolean electionAllowed);
    boolean isElectionAllowed();
    void addClientData(List<String> clientIds);
    void addChatroomData(List<String> chatroomIds, String serverId);



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
