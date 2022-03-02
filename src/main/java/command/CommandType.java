package command;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CommandType {
    // client-server commands
    NEW_IDENTITY("newidentity"),
    MESSAGE("message"),
    // server-server commands
    CHECK_IDENTITY("checkidentity"),
    HEARTBEAT("heartbeat"),
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

