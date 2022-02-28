package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import java.util.Map;

public class JsonParser {
    private static ObjectReader reader = new ObjectMapper().readerFor(Map.class);

    public static Map<String, Object> parse(String json) {
        try {
            return reader.readValue(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
