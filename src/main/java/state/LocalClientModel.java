package state;

import clientserver.ClientSender;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.JsonParser;


@Getter
public class LocalClientModel extends ClientModel {
    private static final Logger LOGGER = LoggerFactory.getLogger(LocalClientModel.class);
    private static final ObjectMapper MAPPER = JsonParser.getMapper();

    private final ClientSender sender;

    public LocalClientModel(String id, ClientSender sender) {
        super(id);
        this.sender = sender;
    }

}
