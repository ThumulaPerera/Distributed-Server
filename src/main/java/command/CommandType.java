package command;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CommandType {
    // client-server commands
    NEW_IDENTITY("newidentity"),
    MESSAGE("message"),
    LIST("list"),
    ROOM_LIST("roomlist"),
    MOVE_JOIN("movejoin"),
    CREATE_ROOM("createroom"),
    DELETE_ROOM("deleteroom"),
    // server-server commands
    CHECK_IDENTITY_F2L("checkidentityf2l"),
    CHECK_IDENTITY_L2F("checkidentityl2f"),
    ADD_ROOM_F2L("addroomf2l"),
    ADD_ROOM_L2F("addrooml2f"),
    NEW_ROOM_L2F("newrooml2f"),
    DELETE_ROOM_F2L("deleteroomf2l"),
    DELETE_ROOM_L2F("deleterooml2f"),
    REMOVE_ROOM_L2F("removerooml2f"),
    HEARTBEAT("heartbeat"),
    HEARTBEAT_STATUS_CHECK("statuscheck"),
    HEARTBEAT_STATUS_REPLY("statuscheckreply"),

    //fast bully commands
    IAMUP("iamup"),
    VIEW("view"),
    COORDINATOR("coordinator"),
    ELECTION("election"),
    ANSWER("answer"),
    NOMINATION("nomination")
    ;

    public final String label;

    CommandType(String label) {
        this.label = label;
    }

    public static CommandType getCommandType(String label) throws IllegalArgumentException {
        for (CommandType type : values()) {
            if (type.label.equals(label)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No such command type: " + label);
    }

    @JsonValue
    public String getLabel() {
        return label;
    }
}

