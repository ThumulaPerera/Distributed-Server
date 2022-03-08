package utils;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class JsonParser {
    private static ObjectReader reader = new ObjectMapper().readerFor(Map.class);
    private static ObjectMapper mapper;

    // configure object mapper
    static {
        mapper = new ObjectMapper();

        SimpleModule simpleModule = new SimpleModule("BooleanAsString", new Version(1, 0, 0, null, null, null));
        simpleModule.addSerializer(Boolean.class,new BooleanSerializer());
        simpleModule.addSerializer(boolean.class,new BooleanSerializer());

        mapper.registerModule(simpleModule);
    }

    public static Map<String, Object> parse(String json) {
        try {
            return reader.readValue(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ObjectMapper getMapper() {
        return mapper;
    }
}

class BooleanSerializer extends JsonSerializer<Boolean> {
    private final static Logger LOGGER = LoggerFactory.getLogger(BooleanSerializer.class);

    @Override
    public void serialize(Boolean value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonGenerationException {
        jgen.writeString(value.toString());
    }

}
