package edu.mci.labyrinth.protocol.message.command;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import edu.mci.labyrinth.protocol.message.Message;
import edu.mci.labyrinth.protocol.model.Coordinates;
import edu.mci.labyrinth.protocol.model.Push;

/**
 * A client command for using a bonus.
 * @param data bonus-specific payload
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record UseBonusMessage(
    @JsonProperty(value = "data", required = true)
    Data data
) implements Message {

    /**
     * Validates the message payload.
     * @throws IllegalArgumentException if data is null
     */
    public UseBonusMessage {
        if (data == null) {
            throw new IllegalArgumentException("data must not be null");
        }
    }

    /**
     * A bonus payload identified by its required bonusType field.
     */
    @JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "bonusType"
    )
    @JsonSubTypes({
        @JsonSubTypes.Type(value = BeamData.class, name = "BEAM"),
        @JsonSubTypes.Type(value = SwapData.class, name = "SWAP"),
        @JsonSubTypes.Type(value = PushFixedData.class, name = "PUSH_FIXED"),
        @JsonSubTypes.Type(value = PushTwiceData.class, name = "PUSH_TWICE")
    })
    public sealed interface Data
        permits BeamData, SwapData, PushFixedData, PushTwiceData {
    }

    /**
     * The payload for teleporting a figure.
     * @param params teleport parameters
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record BeamData(
        @JsonProperty(value = "params", required = true)
        BeamParams params
    ) implements Data {

        /**
         * Validates the teleport payload.
         * @throws IllegalArgumentException if params is null
         */
        public BeamData {
            if (params == null) {
                throw new IllegalArgumentException("params must not be null");
            }
        }
    }

    /**
     * The teleport parameters.
     * @param targetCoordinates target board position
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record BeamParams(
        @JsonProperty(value = "targetCoordinates", required = true)
        Coordinates targetCoordinates
    ) {

        /**
         * Validates the teleport parameters.
         * @throws IllegalArgumentException if targetCoordinates is null
         */
        public BeamParams {
            if (targetCoordinates == null) {
                throw new IllegalArgumentException(
                    "targetCoordinates must not be null"
                );
            }
        }
    }

    /**
     * The payload for swapping player positions.
     * @param params swap parameters
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record SwapData(
        @JsonProperty(value = "params", required = true)
        SwapParams params
    ) implements Data {

        /**
         * Validates the swap payload.
         * @throws IllegalArgumentException if params is null
         */
        public SwapData {
            if (params == null) {
                throw new IllegalArgumentException("params must not be null");
            }
        }
    }

    /**
     * The swap parameters.
     * @param targetPlayerId identifier of the other player
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record SwapParams(
        @JsonProperty(value = "targetPlayerId", required = true)
        String targetPlayerId
    ) {

        /**
         * Validates the swap parameters.
         * @throws IllegalArgumentException if targetPlayerId is null
         */
        public SwapParams {
            if (targetPlayerId == null) {
                throw new IllegalArgumentException(
                    "targetPlayerId must not be null"
                );
            }
        }
    }

    /**
     * The payload for pushing a normally fixed row or column.
     * @param params push parameters
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PushFixedData(
        @JsonProperty(value = "params", required = true)
        Push params
    ) implements Data {

        /**
         * Validates the push payload.
         * @throws IllegalArgumentException if params is null
         */
        public PushFixedData {
            if (params == null) {
                throw new IllegalArgumentException("params must not be null");
            }
        }
    }

    /**
     * The payload for enabling an additional push.
     * @param params empty parameters
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PushTwiceData(
        @JsonProperty(value = "params", required = true)
        PushTwiceParams params
    ) implements Data {

        /**
         * Validates the additional-push payload.
         * @throws IllegalArgumentException if params is null
         */
        public PushTwiceData {
            if (params == null) {
                throw new IllegalArgumentException("params must not be null");
            }
        }
    }

    /**
     * The explicitly empty parameters for an additional push.
     */
    public record PushTwiceParams() {

        /**
         * Rejects fields because these parameters must be empty.
         * @param name unexpected field name
         * @param value unexpected field value
         * @throws IllegalArgumentException if a field is provided
         */
        @JsonAnySetter
        public void rejectField(String name, Object value) {
            throw new IllegalArgumentException(
                "PUSH_TWICE params must be empty"
            );
        }
    }
}
