package dev.vality.dominator.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.vality.geck.serializer.kit.json.JsonHandler;
import dev.vality.geck.serializer.kit.tbase.TBaseProcessor;
import org.apache.thrift.TBase;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JsonUtil {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String thriftBaseToJsonString(TBase thriftBase) {
        if (thriftBase == null) {
            return null;
        }
        try {
            return new TBaseProcessor().process(thriftBase, new JsonHandler()).toString();
        } catch (IOException e) {
            throw new RuntimeException("Couldn't convert to json string: " + thriftBase, e);
        }
    }

    public static JsonNode thriftBaseToJsonNode(TBase thriftBase) {
        if (thriftBase == null) {
            return null;
        }
        try {
            return new TBaseProcessor().process(thriftBase, new JsonHandler());
        } catch (IOException e) {
            throw new RuntimeException("Couldn't convert to json node: " + thriftBase, e);
        }
    }

    public static String objectToJsonString(Object o) {
        if (o == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Couldn't convert object to json string: " + o, e);
        }
    }

    public static <T> T stringToObject(byte[] stringObject, Class<T> type) {
        if (stringObject == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(stringObject, type);
        } catch (IOException e) {
            throw new IllegalStateException("Couldn't convert json string to object: ", e);
        }
    }

    public static <T> List<T> listOfStringsToObjectsList(List<String> source, Class<T> type) {
        return source == null ? new ArrayList<>() : source.stream()
                .map(sourceString -> stringToObject(sourceString.getBytes(StandardCharsets.UTF_8), type))
                .toList();
    }

    public static <T> byte[] toBytes(T object) {
        try {
            if (Objects.isNull(object)) {
                return new byte[0];
            }
            return OBJECT_MAPPER.writeValueAsBytes(object);
        } catch (IOException e) {
            throw new IllegalStateException("Couldn't convert object to byte array: ", e);
        }
    }
}
