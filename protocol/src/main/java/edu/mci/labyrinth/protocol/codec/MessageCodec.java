package edu.mci.labyrinth.protocol.codec;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidTypeIdException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import edu.mci.labyrinth.protocol.message.Message;

import java.util.Objects;

/**
 * Converts protocol messages between Java objects and JSON text.
 */
public final class MessageCodec {

    private final ObjectMapper mapper;

    /**
     * Creates a codec that ignores unknown object fields.
     */
    public MessageCodec() {
        mapper = JsonMapper.builder()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS)
            .build();
    }

    /**
     * Converts a Java message to JSON text.
     * @param message message to serialize
     * @return serialized JSON text
     * @throws NullPointerException if the message is null
     * @throws JsonProcessingException if serialization fails
     */
    public String encode(Message message) throws JsonProcessingException {
        Objects.requireNonNull(message, "message must not be null");

        return mapper.writerFor(Message.class)
            .writeValueAsString(message);
    }

    /**
     * Converts JSON text to the matching Java message.
     * @param json JSON text to deserialize
     * @return deserialized message
     * @throws NullPointerException if the JSON text is null
     * @throws JsonProcessingException if the JSON, envelope or payload is invalid
     * @throws UnknownMessageTypeException if the message type is unknown
     */
    public Message decode(String json)
        throws JsonProcessingException, UnknownMessageTypeException {

        Objects.requireNonNull(json, "json must not be null");

        JsonNode root = mapper.readTree(json);

        if (root == null || !root.isObject()) {
            throw new JsonParseException(
                null,
                "Message must be a JSON object"
            );
        }

        JsonNode type = root.get("type");
        JsonNode data = root.get("data");

        if (type == null
            || !type.isTextual()
            || type.asText().isBlank()) {
            throw new JsonParseException(
                null,
                "Message must contain a non-blank string field 'type'"
            );
        }

        if (data == null || !data.isObject()) {
            throw new JsonParseException(
                null,
                "Message must contain an object field 'data'"
            );
        }

        try {
            return mapper.treeToValue(root, Message.class);
        } catch (InvalidTypeIdException exception) {
            if (exception.getBaseType().getRawClass() == Message.class) {
                throw new UnknownMessageTypeException(
                    type.asText(),
                    exception
                );
            }

            throw exception;
        }
    }

    /**
     * Indicates an unknown top-level message type.
     */
    public static final class UnknownMessageTypeException extends Exception {
        /**
         * The unknown message type received from the sender.
         * @serial
         */
        private final String type;

        /**
         * Creates an exception for an unknown message type.
         * @param type unknown type received in the JSON message
         * @param cause original Jackson exception
         */
        public UnknownMessageTypeException(String type, Throwable cause) {
            super("Unknown message type: " + type, cause);
            this.type = type;
        }

        /**
         * Returns the unknown message type.
         * @return received message type
         */
        public String getType() {
            return type;
        }
    }
}
