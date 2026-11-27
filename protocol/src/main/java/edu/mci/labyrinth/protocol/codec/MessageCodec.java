package edu.mci.labyrinth.protocol.codec;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.CoercionAction;
import com.fasterxml.jackson.databind.cfg.CoercionInputShape;
import com.fasterxml.jackson.databind.exc.InvalidTypeIdException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.type.LogicalType;
import edu.mci.labyrinth.protocol.message.Message;

import java.util.Objects;

/**
 * Converts protocol messages between Java objects and JSON text.
 */
public final class MessageCodec {

    private final ObjectMapper mapper;

    /**
     * Creates a codec that ignores unknown fields and requires JSON integers
     * for integer properties.
     */
    public MessageCodec() {
        mapper = JsonMapper.builder()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .disable(DeserializationFeature.ACCEPT_FLOAT_AS_INT)
            .withCoercionConfig(LogicalType.Integer, config -> config
                .setCoercion(CoercionInputShape.String, CoercionAction.Fail))
            .enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS)
            .enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
            .build();
    }

    /**
     * Converts a Java message to JSON text.
     * @param message message to serialize
     * @return serialized JSON text
     * @throws NullPointerException if the message is null
     * @throws InvalidMessageException if serialization fails
     */
    public String encode(Message message) throws InvalidMessageException {
        Objects.requireNonNull(message, "message must not be null");

        try {
            return mapper.writerFor(Message.class).writeValueAsString(message);
        }
        catch (JsonProcessingException exception)
        {
            throw new InvalidMessageException("Message could not be serialized", exception);
        }
    }

    /**
     * Converts JSON text to the matching Java message.
     * @param json JSON text to deserialize
     * @return deserialized message
     * @throws NullPointerException if the JSON text is null
     * @throws InvalidMessageException if the JSON, envelope or payload is invalid
     * @throws UnknownMessageTypeException if the message type is unknown
     */
    public Message decode(String json)
        throws InvalidMessageException, UnknownMessageTypeException {
        Objects.requireNonNull(json, "json must not be null");

        try {
            JsonNode root = mapper.readTree(json);

            if (root == null || !root.isObject()) {
                throw new InvalidMessageException(
                    "Message must be a JSON object"
                );
            }

            JsonNode type = root.get("type");
            JsonNode data = root.get("data");

            if (type == null || !type.isTextual() || type.asText().isBlank()) {
                throw new InvalidMessageException(
                    "Message must contain a non-blank string field 'type'"
                );
            }

            if (data == null || !data.isObject()) {
                throw new InvalidMessageException(
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
        } catch (JsonProcessingException exception) {
            throw new InvalidMessageException(
                "Message could not be deserialized",
                exception
            );
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
