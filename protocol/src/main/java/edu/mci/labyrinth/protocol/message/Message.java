package edu.mci.labyrinth.protocol.message;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import edu.mci.labyrinth.protocol.message.command.ConnectMessage;
import edu.mci.labyrinth.protocol.message.command.DisconnectMessage;
import edu.mci.labyrinth.protocol.message.command.MoveFigureMessage;
import edu.mci.labyrinth.protocol.message.command.PassMoveMessage;
import edu.mci.labyrinth.protocol.message.command.PushTileMessage;
import edu.mci.labyrinth.protocol.message.command.RematchRequestMessage;
import edu.mci.labyrinth.protocol.message.command.SetAiModeMessage;
import edu.mci.labyrinth.protocol.message.command.StartGameMessage;
import edu.mci.labyrinth.protocol.message.command.UseBonusMessage;

/**
 * A WebSocket message identified by its JSON type field.
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = ConnectMessage.class, name = "CONNECT"),
    @JsonSubTypes.Type(value = DisconnectMessage.class, name = "DISCONNECT"),
    @JsonSubTypes.Type(value = StartGameMessage.class, name = "START_GAME"),
    @JsonSubTypes.Type(value = PushTileMessage.class, name = "PUSH_TILE"),
    @JsonSubTypes.Type(value = MoveFigureMessage.class, name = "MOVE_FIGURE"),
    @JsonSubTypes.Type(value = PassMoveMessage.class, name = "PASS_MOVE"),
    @JsonSubTypes.Type(value = UseBonusMessage.class, name = "USE_BONUS"),
    @JsonSubTypes.Type(value = SetAiModeMessage.class, name = "SET_AI_MODE"),
    @JsonSubTypes.Type(value = RematchRequestMessage.class, name = "REMATCH_REQUEST")
})
public interface Message {
}
