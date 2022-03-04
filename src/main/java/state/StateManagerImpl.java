package state;

import clientserver.ServerThread;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


public class StateManagerImpl implements StateManager, StateInitializer {
    private static Logger LOGGER = LoggerFactory.getLogger(StateManagerImpl.class);
    private static StateManagerImpl instance = new StateManagerImpl();
    @Getter private Map<String, ServerModel> servers;
    @Getter private final Set<String> availableServers = Collections.synchronizedSet(new HashSet<>());
    private ServerModel leader;
    private ServerModel self;

    private StateManagerImpl() {
        servers = Collections.synchronizedMap(new HashMap<>());
    }

    public static StateManagerImpl getInstance() {
        return instance;
    }

    @Override
    public boolean isLeader() {
        boolean isLeader = self == leader;
//        LOGGER.debug("Is leader: {}", isLeader);
        return isLeader;
    }

    @Override
    public ServerModel getLeader() {
        return leader;
    }

    @Override
    public void setLeader(String leaderId) {
        leader = servers.get(leaderId);
//        leader.getHbdThread().start();
    }

    @Override
    public ServerModel getSelf() {
        return self;
    }

    @Override
    public void setSelf(String selfId) {
        self = servers.get(selfId);
    }

    @Override
    public void addServer(String serverId, String serverAddress, int clientPort, int coordinationPort) {
        servers.put(serverId, new ServerModel(serverId, serverAddress, clientPort, coordinationPort));
    }

    public void addAvailableServer(String serverId){
        this.availableServers.add(serverId);
    }

    public void removeAvailableServer(String serverId){
        this.availableServers.remove(serverId);
    }
}
