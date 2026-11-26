package edu.mci.labyrinth.protocol;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.mci.labyrinth.protocol.codec.MessageCodec;
import edu.mci.labyrinth.protocol.message.command.ConnectMessage;
import edu.mci.labyrinth.protocol.message.command.StartGameMessage;
import edu.mci.labyrinth.protocol.message.command.PushTileMessage;
import edu.mci.labyrinth.protocol.model.Direction;
import edu.mci.labyrinth.protocol.model.Push;
import edu.mci.labyrinth.protocol.message.command.MoveFigureMessage;
import edu.mci.labyrinth.protocol.model.Coordinates;
import edu.mci.labyrinth.protocol.message.command.PassMoveMessage;
import edu.mci.labyrinth.protocol.message.command.DisconnectMessage;
import edu.mci.labyrinth.protocol.message.command.SetAiModeMessage;
import edu.mci.labyrinth.protocol.message.command.RematchRequestMessage;
import edu.mci.labyrinth.protocol.message.command.UseBonusMessage;
import edu.mci.labyrinth.protocol.codec.InvalidMessageException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import edu.mci.labyrinth.protocol.model.Tile;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageCodecTest {

    private final MessageCodec codec = new MessageCodec();
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void decodesConnectMessage() throws Exception {
        String json = """
            {
              "type": "CONNECT",
              "data": {
                "username": "Alice"
              }
            }
            """;

        ConnectMessage message = assertInstanceOf(
            ConnectMessage.class,
            codec.decode(json)
        );

        assertEquals("Alice", message.data().username());
        assertNull(message.data().identifierToken());
    }

    @Test
    void encodesConnectMessage() throws Exception {
        ConnectMessage message = new ConnectMessage(
            new ConnectMessage.Data("Alice", null)
        );

        JsonNode json = mapper.readTree(codec.encode(message));

        assertEquals("CONNECT", json.get("type").asText());
        assertEquals("Alice", json.get("data").get("username").asText());
        assertFalse(json.get("data").has("identifierToken"));
        assertEquals(2, json.size());
        assertEquals(1, json.get("data").size());
    }

    @Test
    void rejectsConnectWithoutUsername() {
        String json = """
        {
          "type": "CONNECT",
          "data": {}
        }
        """;

        assertThrows(
            InvalidMessageException.class,
            () -> codec.decode(json)
        );
    }
    @Test
    void preservesReconnectMessageDuringRoundTrip() throws Exception {
        ConnectMessage original = new ConnectMessage(
            new ConnectMessage.Data(
                "Alice",
                "token-98f2-4bc1-912a"
            )
        );

        String json = codec.encode(original);

        assertEquals(original, codec.decode(json));
    }

    @Test
    @Tag("A-46")
    void ignoresUnknownFields() throws Exception {
        String json = """
            {
              "type": "CONNECT",
              "extraEnvelopeField": true,
              "data": {
                "username": "Alice",
                "clientVersion": "1.0"
              }
            }
            """;

        ConnectMessage message = assertInstanceOf(
            ConnectMessage.class,
            codec.decode(json)
        );

        assertEquals("Alice", message.data().username());
        assertNull(message.data().identifierToken());
    }

    @Test
    @Tag("A-46")
    void reportsUnknownMessageType() {
        String json = """
            {
              "type": "FLY",
              "data": {}
            }
            """;

        MessageCodec.UnknownMessageTypeException exception =
            assertThrows(
                MessageCodec.UnknownMessageTypeException.class,
                () -> codec.decode(json)
            );

        assertEquals("FLY", exception.getType());
    }

    @Test
    void convertsStartGameMessageInBothDirections() throws Exception {
        String json = """
        {
          "type": "START_GAME",
          "data": {
            "boardSize": {
              "rows": 7,
              "cols": 7
            },
            "maxPlayers": 4,
            "treasureCardCount": 6,
            "gameDurationInSeconds": 1800,
            "turnTimeoutInSeconds": 30,
            "bonusSpawnInterval": 3
          }
        }
        """;

        StartGameMessage expected = new StartGameMessage(
            new StartGameMessage.Data(
                new StartGameMessage.BoardSize(7, 7),
                4,
                6,
                1800,
                30,
                3
            )
        );

        // Verify JSON to Java.
        assertEquals(expected, codec.decode(json));

        // Verify Java to JSON, independently of formatting and field order.
        assertEquals(
            mapper.readTree(json),
            mapper.readTree(codec.encode(expected))
        );
    }

    @Test
    @Tag("V-01")
    void rejectsPushTileWithoutRotation() {
        String json = """
        {
          "type": "PUSH_TILE",
          "data": {
            "rowIndex": 1,
            "columnIndex": 0,
            "direction": "RIGHT"
          }
        }
        """;

        assertThrows(
            InvalidMessageException.class,
            () -> codec.decode(json)
        );
    }

    @Test
    void convertsPushTileMessageInBothDirections() throws Exception {
        String json = """
        {
          "type": "PUSH_TILE",
          "data": {
            "rowIndex": 1,
            "columnIndex": 0,
            "rotation": 1,
            "direction": "RIGHT"
          }
        }
        """;

        PushTileMessage expected = new PushTileMessage(
            new Push(
                1,
                0,
                1,
                Direction.RIGHT
            )
        );

        // Verify JSON to Java.
        assertEquals(expected, codec.decode(json));

        // Verify Java to JSON.
        assertEquals(
            mapper.readTree(json),
            mapper.readTree(codec.encode(expected))
        );
    }

    @Test
    void convertsMoveFigureMessageInBothDirections() throws Exception {
        String json = """
        {
          "type": "MOVE_FIGURE",
          "data": {
            "targetCoordinates": {
              "row": 3,
              "column": 4
            }
          }
        }
        """;

        MoveFigureMessage expected = new MoveFigureMessage(
            new MoveFigureMessage.Data(
                new Coordinates(3, 4)
            )
        );

        // Verify JSON to Java.
        assertEquals(expected, codec.decode(json));

        // Verify Java to JSON.
        assertEquals(
            mapper.readTree(json),
            mapper.readTree(codec.encode(expected))
        );
    }

    @Test
    void convertsPassMoveMessageInBothDirections() throws Exception {
        String json = """
        {
          "type": "PASS_MOVE",
          "data": {}
        }
        """;

        PassMoveMessage expected = new PassMoveMessage(
            new PassMoveMessage.Data()
        );

        // Verify JSON to Java.
        assertEquals(expected, codec.decode(json));

        // Verify Java to JSON.
        assertEquals(
            mapper.readTree(json),
            mapper.readTree(codec.encode(expected))
        );
    }


    @Test
    void convertsDisconnectMessageInBothDirections() throws Exception {
        String json = """
        {
          "type": "DISCONNECT",
          "data": {}
        }
        """;

        DisconnectMessage expected = new DisconnectMessage(
            new DisconnectMessage.Data()
        );

        // Verify JSON to Java.
        assertEquals(expected, codec.decode(json));

        // Verify Java to JSON.
        assertEquals(
            mapper.readTree(json),
            mapper.readTree(codec.encode(expected))
        );
    }

    @Test
    void convertsSetAiModeMessageInBothDirections() throws Exception {
        String json = """
        {
          "type": "SET_AI_MODE",
          "data": {
            "enabled": true
          }
        }
        """;

        SetAiModeMessage expected = new SetAiModeMessage(
            new SetAiModeMessage.Data(true)
        );

        // Verify JSON to Java.
        assertEquals(expected, codec.decode(json));

        // Verify Java to JSON.
        assertEquals(
            mapper.readTree(json),
            mapper.readTree(codec.encode(expected))
        );
    }

    @Test
    void convertsRematchRequestMessageInBothDirections() throws Exception {
        String json = """
        {
          "type": "REMATCH_REQUEST",
          "data": {
            "ready": true
          }
        }
        """;

        RematchRequestMessage expected = new RematchRequestMessage(
            new RematchRequestMessage.Data(true)
        );

        // Verify JSON to Java.
        assertEquals(expected, codec.decode(json));

        // Verify Java to JSON.
        assertEquals(
            mapper.readTree(json),
            mapper.readTree(codec.encode(expected))
        );
    }

    @Test
    void convertsBeamBonusMessageInBothDirections() throws Exception {
        String json = """
        {
          "type": "USE_BONUS",
          "data": {
            "bonusType": "BEAM",
            "params": {
              "targetCoordinates": {
                "row": 5,
                "column": 2
              }
            }
          }
        }
        """;

        UseBonusMessage expected = new UseBonusMessage(
            new UseBonusMessage.BeamData(
                new UseBonusMessage.BeamParams(
                    new Coordinates(5, 2)
                )
            )
        );

        // Verify JSON to Java.
        assertEquals(expected, codec.decode(json));

        // Verify Java to JSON.
        assertEquals(
            mapper.readTree(json),
            mapper.readTree(codec.encode(expected))
        );
    }

    @Test
    void convertsSwapBonusMessageInBothDirections() throws Exception {
        String json = """
        {
          "type": "USE_BONUS",
          "data": {
            "bonusType": "SWAP",
            "params": {
              "targetPlayerId": "p-102"
            }
          }
        }
        """;

        UseBonusMessage expected = new UseBonusMessage(
            new UseBonusMessage.SwapData(
                new UseBonusMessage.SwapParams("p-102")
            )
        );

        // Verify JSON to Java.
        assertEquals(expected, codec.decode(json));

        // Verify Java to JSON.
        assertEquals(
            mapper.readTree(json),
            mapper.readTree(codec.encode(expected))
        );
    }

    @Test
    void convertsPushFixedBonusMessageInBothDirections() throws Exception {
        String json = """
        {
          "type": "USE_BONUS",
          "data": {
            "bonusType": "PUSH_FIXED",
            "params": {
              "rowIndex": 0,
              "columnIndex": 2,
              "rotation": 2,
              "direction": "DOWN"
            }
          }
        }
        """;

        UseBonusMessage expected = new UseBonusMessage(
            new UseBonusMessage.PushFixedData(
                new Push(
                    0,
                    2,
                    2,
                    Direction.DOWN
                )
            )
        );

        // Verify JSON to Java.
        assertEquals(expected, codec.decode(json));

        // Verify Java to JSON.
        assertEquals(
            mapper.readTree(json),
            mapper.readTree(codec.encode(expected))
        );
    }

    @Test
    void convertsPushTwiceBonusMessageInBothDirections() throws Exception {
        String json = """
        {
          "type": "USE_BONUS",
          "data": {
            "bonusType": "PUSH_TWICE",
            "params": {}
          }
        }
        """;

        UseBonusMessage expected = new UseBonusMessage(
            new UseBonusMessage.PushTwiceData(
                new UseBonusMessage.PushTwiceParams()
            )
        );

        // Verify JSON to Java.
        assertEquals(expected, codec.decode(json));

        // Verify Java to JSON.
        assertEquals(
            mapper.readTree(json),
            mapper.readTree(codec.encode(expected))
        );
    }

    /** test datatypes **/
    @Test
    void rejectsMissingType() {
        String json = """
        {
          "data": {}
        }
        """;

        assertThrows(
            InvalidMessageException.class,
            () -> codec.decode(json)
        );
    }

    @Test
    void rejectsMissingData() {
        String json = """
        {
          "type": "CONNECT"
        }
        """;

        assertThrows(
            InvalidMessageException.class,
            () -> codec.decode(json)
        );
    }

    @Test
    void rejectsNonStringType() {
        String json = """
        {
          "type": 123,
          "data": {}
        }
        """;

        assertThrows(
            InvalidMessageException.class,
            () -> codec.decode(json)
        );
    }

    @Test
    void rejectsNonObjectData() {
        String json = """
        {
          "type": "CONNECT",
          "data": "Text"
        }
        """;

        assertThrows(
            InvalidMessageException.class,
            () -> codec.decode(json)
        );
    }

    @Test
    void rejectsAdditionalFieldsInPassMoveData() {
        String json = """
        {
          "type": "PASS_MOVE",
          "data": {
            "username": "Alice"
          }
        }
        """;

        assertThrows(
            InvalidMessageException.class,
            () -> codec.decode(json)
        );
    }

    @Test
    void rejectsAdditionalFieldsInDisconnectData() {
        String json = """
        {
          "type": "DISCONNECT",
          "data": {
            "username": "Alice"
          }
        }
        """;

        assertThrows(
            InvalidMessageException.class,
            () -> codec.decode(json)
        );
    }

    @Test
    void rejectsAdditionalFieldsInPushTwiceParams() {
        String json = """
        {
          "type": "USE_BONUS",
          "data": {
            "bonusType": "PUSH_TWICE",
            "params": {
              "direction": "UP"
            }
          }
        }
        """;

        assertThrows(
            InvalidMessageException.class,
            () -> codec.decode(json)
        );
    }

    @Test
    void rejectsUnknownBonusType() {
        String json = """
        {
          "type": "USE_BONUS",
          "data": {
            "bonusType": "FLY",
            "params": {}
          }
        }
        """;

        assertThrows(
            InvalidMessageException.class,
            () -> codec.decode(json)
        );
    }

    @Test
    @Tag("V-01")
    void rejectsMoveFigureWithoutTargetCoordinates() {
        String json = """
        {
          "type": "MOVE_FIGURE",
          "data": {}
        }
        """;

        assertThrows(
            InvalidMessageException.class,
            () -> codec.decode(json)
        );
    }

    @Test
    @Tag("V-01")
    void rejectsMoveFigureWithNullTargetCoordinates() {
        String json = """
        {
          "type": "MOVE_FIGURE",
          "data": {
            "targetCoordinates": null
          }
        }
        """;

        assertThrows(
            InvalidMessageException.class,
            () -> codec.decode(json)
        );
    }

    @Test
    @Tag("V-01")
    void rejectsSetAiModeWithoutEnabled() {
        String json = """
        {
          "type": "SET_AI_MODE",
          "data": {}
        }
        """;

        assertThrows(
            InvalidMessageException.class,
            () -> codec.decode(json)
        );
    }

    @Test
    @Tag("V-01")
    void rejectsSetAiModeWithNullEnabled() {
        String json = """
        {
          "type": "SET_AI_MODE",
          "data": {
            "enabled": null
          }
        }
        """;

        assertThrows(
            InvalidMessageException.class,
            () -> codec.decode(json)
        );
    }

    @ParameterizedTest
    @Tag("V-01")
    @ValueSource(strings = {
        // Missing or null readiness.
        """
        {"type":"REMATCH_REQUEST","data":{}}
        """,
        """
        {"type":"REMATCH_REQUEST","data":{"ready":null}}
        """,

        // Missing or null board size.
        """
        {"type":"START_GAME","data":{}}
        """,
        """
        {"type":"START_GAME","data":{"boardSize":null}}
        """,

        // Missing board dimensions.
        """
        {"type":"START_GAME","data":{"boardSize":{"cols":7}}}
        """,
        """
        {"type":"START_GAME","data":{"boardSize":{"rows":7}}}
        """,

        // Null board dimensions.
        """
        {"type":"START_GAME","data":{"boardSize":{"rows":null,"cols":7}}}
        """,
        """
        {"type":"START_GAME","data":{"boardSize":{"rows":7,"cols":null}}}
        """
    })
    void rejectsMissingOrNullConfigurationFields(String json) {
        assertThrows(
            InvalidMessageException.class,
            () -> codec.decode(json)
        );
    }

    @ParameterizedTest
    @Tag("V-01")
    @ValueSource(strings = {
        // Missing row.
        """
        {"type":"MOVE_FIGURE","data":{"targetCoordinates":{"column":2}}}
        """,

        // Missing column.
        """
        {"type":"MOVE_FIGURE","data":{"targetCoordinates":{"row":3}}}
        """,

        // Null row.
        """
        {"type":"MOVE_FIGURE","data":{"targetCoordinates":{"row":null,"column":2}}}
        """,

        // Null column.
        """
        {"type":"MOVE_FIGURE","data":{"targetCoordinates":{"row":3,"column":null}}}
        """
    })
    void rejectsMissingOrNullCoordinateFields(String json) {
        assertThrows(
            InvalidMessageException.class,
            () -> codec.decode(json)
        );
    }

    @ParameterizedTest
    @Tag("V-01")
    @ValueSource(strings = {
        // Missing bonus type.
        """
        {"type":"USE_BONUS","data":{"params":{}}}
        """,

        // Missing parameters for each bonus type.
        """
        {"type":"USE_BONUS","data":{"bonusType":"BEAM"}}
        """,
        """
        {"type":"USE_BONUS","data":{"bonusType":"SWAP"}}
        """,
        """
        {"type":"USE_BONUS","data":{"bonusType":"PUSH_FIXED"}}
        """,
        """
        {"type":"USE_BONUS","data":{"bonusType":"PUSH_TWICE"}}
        """,

        // Missing bonus targets.
        """
        {"type":"USE_BONUS","data":{"bonusType":"BEAM","params":{}}}
        """,
        """
        {"type":"USE_BONUS","data":{"bonusType":"SWAP","params":{}}}
        """,

        // Missing individual push parameters.
        """
        {"type":"USE_BONUS","data":{"bonusType":"PUSH_FIXED","params":{"columnIndex":2,"rotation":0,"direction":"DOWN"}}}
        """,
        """
        {"type":"USE_BONUS","data":{"bonusType":"PUSH_FIXED","params":{"rowIndex":0,"rotation":0,"direction":"DOWN"}}}
        """,
        """
        {"type":"USE_BONUS","data":{"bonusType":"PUSH_FIXED","params":{"rowIndex":0,"columnIndex":2,"direction":"DOWN"}}}
        """,
        """
        {"type":"USE_BONUS","data":{"bonusType":"PUSH_FIXED","params":{"rowIndex":0,"columnIndex":2,"rotation":0}}}
        """,

        // Null parameter objects.
        """
        {"type":"USE_BONUS","data":{"bonusType":"BEAM","params":null}}
        """,
        """
        {"type":"USE_BONUS","data":{"bonusType":"SWAP","params":null}}
        """,
        """
        {"type":"USE_BONUS","data":{"bonusType":"PUSH_FIXED","params":null}}
        """,
        """
        {"type":"USE_BONUS","data":{"bonusType":"PUSH_TWICE","params":null}}
        """,

        // Null targets and direction.
        """
        {"type":"USE_BONUS","data":{"bonusType":"BEAM","params":{"targetCoordinates":null}}}
        """,
        """
        {"type":"USE_BONUS","data":{"bonusType":"SWAP","params":{"targetPlayerId":null}}}
        """,
        """
        {"type":"USE_BONUS","data":{"bonusType":"PUSH_FIXED","params":{"rowIndex":0,"columnIndex":2,"rotation":0,"direction":null}}}
        """
    })
    void rejectsMissingOrNullBonusFields(String json) {
        assertThrows(
            InvalidMessageException.class,
            () -> codec.decode(json)
        );
    }

    @Test
    @Tag("F-314")
    void convertsTileWithNullTreasureAndBonusInBothDirections()
        throws Exception {

        String json = """
        {
          "entrances": ["UP", "RIGHT"],
          "rotation": 1,
          "treasure": null,
          "bonus": null,
          "isFixed": false
        }
        """;

        Tile expected = new Tile(
            List.of(Direction.UP, Direction.RIGHT),
            1,
            null,
            null,
            false
        );

        assertEquals(expected, mapper.readValue(json, Tile.class));

        assertEquals(
            mapper.readTree(json),
            mapper.readTree(mapper.writeValueAsString(expected))
        );
    }

    @ParameterizedTest
    @Tag("F-314")
    @ValueSource(strings = {
        // Missing treasure.
        """
        {
          "entrances": ["UP", "RIGHT"],
          "rotation": 1,
          "bonus": null,
          "isFixed": false
        }
        """,

        // Missing bonus.
        """
        {
          "entrances": ["UP", "RIGHT"],
          "rotation": 1,
          "treasure": null,
          "isFixed": false
        }
        """
    })
    void rejectsTileWithoutRequiredNullableFields(String json) {
        assertThrows(
            com.fasterxml.jackson.core.JsonProcessingException.class,
            () -> mapper.readValue(json, Tile.class)
        );
    }
}
