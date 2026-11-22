package edu.mci.labyrinth.protocol.message.command;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import edu.mci.labyrinth.protocol.message.Message;
import edu.mci.labyrinth.protocol.model.Coordinates;
import edu.mci.labyrinth.protocol.model.Direction;

/**
 * A client command for using a bonus.
 * @param data bonus-specific payload
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record UseBonusMessage(Data data) implements Message {

    /**
     * A bonus payload identified by its bonusType field.
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
    public record BeamData(BeamParams params) implements Data {
    }

    /**
     * The teleport parameters.
     * @param targetCoordinates target board position
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record BeamParams(Coordinates targetCoordinates) {
    }

    /**
     * The payload for swapping player positions.
     * @param params swap parameters
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record SwapData(SwapParams params) implements Data {
    }

    /**
     * The swap parameters.
     * @param targetPlayerId identifier of the other player
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record SwapParams(String targetPlayerId) {
    }

    /**
     * The payload for pushing a normally fixed row or column.
     * @param params push parameters
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PushFixedData(PushFixedParams params) implements Data {
    }

    /**
     * The parameters for pushing a normally fixed row or column.
     * @param rowIndex row of the insertion position
     * @param columnIndex column of the insertion position
     * @param rotation absolute clockwise orientation from zero to three
     * @param direction direction of movement into the board
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PushFixedParams(
        int rowIndex,
        int columnIndex,
        int rotation,
        Direction direction
    ) {
    }

    /**
     * The payload for enabling an additional push.
     *
     * @param params empty parameters
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PushTwiceData(PushTwiceParams params) implements Data {
    }

    /**
     * The explicitly empty parameters for an additional push.
     */
    public record PushTwiceParams() {

        /**
         * Rejects fields because these parameters must be empty.
         * @param name unexpected field name
         * @param value unexpected field value
         */
        @JsonAnySetter
        public void rejectField(String name, Object value) {
            throw new IllegalArgumentException(
                "PUSH_TWICE params must be empty"
            );
        }
    }
}
