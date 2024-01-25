package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    ChessBoard board;
    TeamColor teamTurn;
    ChessPosition whiteKingPosition;
    ChessPosition blackKingPosition;
    public ChessGame() {
        this.board = null;
        this.teamTurn = null;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = this.board.getPiece(startPosition);
        if (piece != null) {
            return piece.pieceMoves(this.board, startPosition);
        }
        return null;
    }

    private boolean isValidMove(ChessMove move) {
        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
        ChessPiece movePiece = this.board.getPiece(move.getStartPosition());
        boolean isValidIgnoringChecks = validMoves != null && validMoves.contains(move) && movePiece.getTeamColor().equals(this.teamTurn);
        if (!isValidIgnoringChecks) {
            return false;
        }
        ChessPiece capturedPiece = doMove(move);
        boolean isValid = !isInCheck(this.teamTurn);
        undoMove(move, capturedPiece);
        return isValid;
    }

    private void updateTeamColor() {
        this.setTeamTurn(this.teamTurn == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE);
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = this.board.getPiece(move.getStartPosition());
        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
         if (!isValidMove(move)) {
            throw new InvalidMoveException();
        }
        doMove(move);
        updateTeamColor();
    }

    private ChessPiece doMove(ChessMove move) {
        ChessPiece movedPiece = this.board.getPiece(move.getStartPosition());
        ChessPiece capturedPiece = this.board.getPiece(move.getEndPosition());
        this.board.addPiece(move.getEndPosition(), movedPiece);
        this.board.removePiece(move.getStartPosition());
        return capturedPiece;
    }

    private void undoMove(ChessMove move, ChessPiece capturedPiece) {
        ChessPiece movedPiece = board.getPiece(move.getEndPosition());
        this.board.addPiece(move.getStartPosition(), movedPiece);
        this.board.removePiece(move.getEndPosition());
        if (capturedPiece != null) {
            this.board.addPiece(move.getEndPosition(), capturedPiece);
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPiece piece;
        ChessPosition kingPosition = null;
        Collection<ChessPosition> attackedPositions = new HashSet<>();
        Collection<ChessMove> attackMoves;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                piece = this.board.squares[i][j];
                if (piece == null) {
                    continue;
                }
                if (!piece.getTeamColor().equals(teamColor)) {
                    attackedPositions.addAll(getAttackedPositions(new ChessPosition(i + 1, j + 1)));
                }
                if (piece.getTeamColor().equals(teamColor) && piece.getPieceType().equals(ChessPiece.PieceType.KING)) {
                    kingPosition = new ChessPosition(i + 1, j + 1);
                }
            }
        }
        return attackedPositions.contains(kingPosition);
    }

    private Collection<ChessPosition> getAttackedPositions(ChessPosition startPosition) {
        Collection<ChessMove> validMoves = validMoves(startPosition);
        Collection<ChessPosition> attackedPositions = new HashSet<>();
        if (validMoves != null) {
            for (var move : validMoves) {
                attackedPositions.add(move.getEndPosition());
            }
        }
        return attackedPositions;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
        this.setKingPositions();
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }

    private void setKingPositions() {
        ChessPiece piece;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                piece = this.board.squares[i][j];
                if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING)
                    if (piece.getTeamColor() == TeamColor.WHITE) {
                    this.whiteKingPosition = new ChessPosition(i + 1, j + 1);
                    } else if (piece.getTeamColor() == TeamColor.BLACK) {
                    this.blackKingPosition = new ChessPosition(i + 1, j + 1);
                }
            }
        }
    }
}
