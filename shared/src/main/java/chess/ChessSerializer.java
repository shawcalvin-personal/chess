package chess;

import chess.ChessRuleBook.*;
import com.google.gson.*;

public class ChessSerializer {
    public static Gson createSerializer() {
        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.registerTypeAdapter(MovementRule.class,
                (JsonDeserializer<MovementRule>) (el, type, ctx) -> {
                    MovementRule movementRule = null;
                    if (el.isJsonObject()) {
                        String pieceType = el.getAsJsonObject().get("pieceType").getAsString();
                        switch (ChessPiece.PieceType.valueOf(pieceType)) {
                            case PAWN -> movementRule = ctx.deserialize(el, PawnMovementRule.class);
                            case ROOK -> movementRule = ctx.deserialize(el, RookMovementRule.class);
                            case KNIGHT -> movementRule = ctx.deserialize(el, KnightMovementRule.class);
                            case BISHOP -> movementRule = ctx.deserialize(el, BishopMovementRule.class);
                            case QUEEN -> movementRule = ctx.deserialize(el, QueenMovementRule.class);
                            case KING -> movementRule = ctx.deserialize(el, KingMovementRule.class);
                        }
                    }
                    return movementRule;
                });

        return gsonBuilder.create();
    }

    public static String serializeChessGame(ChessGame game) {
        return ChessSerializer.createSerializer().toJson(game);
    }

    public static ChessGame deserializeChessGame(String serializedGame) {
        return ChessSerializer.createSerializer().fromJson(serializedGame, ChessGame.class);
    }
}
