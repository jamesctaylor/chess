package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor teamColor;
    private final PieceType pieceType;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.teamColor = pieceColor;
        this.pieceType = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> moves = new ArrayList<>();
        switch (pieceType) {
            case ROOK -> addSimpleMoves(board, myPosition, moves,
                    new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}});
            case BISHOP -> addSimpleMoves(board, myPosition, moves,
                    new int[][]{{1, 1}, {1, -1}, {-1, 1}, {-1, -1}});
            case QUEEN -> addSimpleMoves(board, myPosition, moves,
                    new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1},
                            {1, 1}, {1, -1}, {-1, 1}, {-1, -1}});
            case KING -> kingMoves(board, myPosition, moves,
                    new int[][]{{1, 0}, {1, 1}, {0, 1}, {-1, 1},
                            {-1, 0}, {-1, -1}, {0, -1}, {1, -1}});
            case KNIGHT -> knightMoves(board, myPosition, moves,
                    new int[][]{{2, 1}, {1, 2}, {-1, 2}, {-2, 1},
                            {-2, -1}, {-1, -2}, {1, -2}, {2, -1}});
            // Need to do the pawn moves
        }
        return moves;
    };
    private void addSimpleMoves(ChessBoard board, ChessPosition start, List<ChessMove> moves, int[][] directions) {
        for (int[] direction : directions) {
            int row = start.getRow() + direction[0];
            int col = start.getColumn() + direction[1];
            while (onBoard(row, col)) {
                ChessPosition end = new ChessPosition(row, col);
                ChessPiece occupant = board.getPiece(end);
                if (occupant == null) {
                    moves.add(new ChessMove(start, end, null));
                } else {
                    if (occupant.getTeamColor() != teamColor) {
                        moves.add(new ChessMove(start, end, null));
                    }
                    break;
                }
                row += direction[0];
                col += direction[1];
            }
        }
    }

    private void kingMoves(ChessBoard board, ChessPosition start,
                           List<ChessMove> moves, int[][] directions) {
        for (int[] direction : directions) {
            int newRow = start.getRow() + direction[0];
            int newColumn = start.getColumn() + direction[1];

            // Ignore positions outside the board
            if (newRow < 1 || newRow > 8 || newColumn < 1 || newColumn > 8) {
                continue;
            }

            ChessPosition end = new ChessPosition(newRow, newColumn);

            ChessPiece pieceAtEnd = board.getPiece(end);

            // Add an empty square or a square occupied by an enemy
            if (pieceAtEnd == null || pieceAtEnd.getTeamColor() != this.getTeamColor()) {
                moves.add(new ChessMove(start, end, null));
            }
        }
    }

    private void knightMoves(ChessBoard board, ChessPosition start,
                             List<ChessMove> moves, int[][] offsets) {
        for (int[] offset : offsets) {
            int row = start.getRow() + offset[0];
            int col = start.getColumn() + offset[1];
            if (!onBoard(row, col)) continue;
            ChessPosition end = new ChessPosition(row, col);
            ChessPiece occupant = board.getPiece(end);
            if (occupant == null || occupant.getTeamColor() != teamColor) {
                moves.add(new ChessMove(start, end, null));
            }
        }
    }

    private boolean onBoard(int row, int col) {
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }
}
