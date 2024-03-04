package dataAccess;

import chess.*;
import chess.ChessRuleBook.*;
import com.google.gson.*;

public class ChessGameSerializer {
    public static Gson createSerializer() {
        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.registerTypeAdapter(MovementRule.class,
                (JsonDeserializer<MovementRule>) (el, type, ctx) -> {
                    System.out.println("ELEMENT: " + type);
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
}
