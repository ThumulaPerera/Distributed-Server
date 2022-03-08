package command;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CommandType {
    // client-server commands
    NEW_IDENTITY("newidentity"),
    MESSAGE("message"),
    LIST("list"),
    ROOM_LIST("roomlist"),
    // server-server commands
    CHECK_IDENTITY_F2L("checkidentityf2l"),
    CHECK_IDENTITY_L2F("checkidentityl2f"),
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

