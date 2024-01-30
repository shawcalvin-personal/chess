package chess.ChessRuleBook;

import chess.*;
import java.util.Collection;
import java.util.HashSet;

public class ChessRuleBook {
    MovementRule kingMovementRule;
    MovementRule queenMovementRule;
    MovementRule rookMovementRule;
    MovementRule bishopMovementRule;
    MovementRule knightMovementRule;
    MovementRule pawnMovementRule;
    public ChessRuleBook() {
        this.kingMovementRule = new KingMovementRule();
        this.queenMovementRule = new QueenMovementRule();
        this.rookMovementRule = new RookMovementRule();
        this.bishopMovementRule = new BishopMovementRule();
        this.knightMovementRule = new KnightMovementRule();
        this.pawnMovementRule = new PawnMovementRule();
    }

    public Collection<ChessMove> validMoves(ChessBoard board, ChessPosition position, ChessGame.TeamColor teamTurn) {
        Collection<ChessMove> validMovesIgnoringCheck = validMovesIgnoringCheck(board, position, teamTurn);
        Collection<ChessMove> validMoves = new HashSet<>();
        for (var move : validMovesIgnoringCheck) {
            board.movePiece(move.getStartPosition(), move.getEndPosition(), move.getPromotionPiece());
            if (!isInCheck(board, teamTurn)) {
                validMoves.add(move);
            }
            board.undoLastMove();
        }
        return validMoves;
    }

    private Collection<ChessMove> validMovesIgnoringCheck(ChessBoard board, ChessPosition position, ChessGame.TeamColor teamTurn) {
        if (teamTurn != board.getPiece(position).getTeamColor()) {
            return new HashSet<>();
        }
        return switch (board.getPiece(position).getPieceType()) {
            case KING -> this.kingMovementRule.getValidMoves(board, position);
            case QUEEN -> this.queenMovementRule.getValidMoves(board,position);
            case ROOK -> this.rookMovementRule.getValidMoves(board,position);
            case BISHOP -> this.bishopMovementRule.getValidMoves(board,position);
            case KNIGHT -> this.knightMovementRule.getValidMoves(board,position);
            case PAWN -> this.pawnMovementRule.getValidMoves(board,position);
        };
    }

    private Collection<ChessPosition> getAttackedPositions(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> validMoves = validMovesIgnoringCheck(board, position, board.getPiece(position).getTeamColor());
        Collection<ChessPosition> attackedPositions = new HashSet<>();
        if (validMoves != null) {
            for (var move : validMoves) {
                attackedPositions.add(move.getEndPosition());
            }
        }
        return attackedPositions;
    }

    public boolean isInCheck(ChessBoard board, ChessGame.TeamColor teamTurn) {
        ChessGame.TeamColor nextTeamTurn = teamTurn.equals(ChessGame.TeamColor.WHITE) ? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;
        Collection<ChessPosition> attackedPositions = new HashSet<>();
        ChessPosition kingPosition = null;
        ChessPiece piece;
        for (int i = 0; i < board.getSquares().length; i++) {
            piece = board.getSquares()[i];
            if (piece == null) {
                continue;
            }
            else if (piece.getTeamColor().equals(nextTeamTurn)) {
                attackedPositions.addAll(getAttackedPositions(board, new ChessPosition(board.getBoardRow(i), board.getBoardColumn(i))));
            }
            else if (piece.getTeamColor().equals(teamTurn) && piece.getPieceType().equals(ChessPiece.PieceType.KING)) {
                kingPosition = new ChessPosition(board.getBoardRow(i), board.getBoardColumn(i));
            }
        }

        return attackedPositions.contains(kingPosition);
    }

    public boolean isInCheckMake(ChessBoard board, ChessGame.TeamColor teamTurn) {
        return true;
    }

    public boolean isInStalemate(ChessBoard board, ChessGame.TeamColor teamTurn) {
        return true;
    }
}
