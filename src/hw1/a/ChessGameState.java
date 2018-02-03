package hw1.a;

/**
 * State class for a chess game. Outside classes will read and update this state
 * as the game progresses.
 */
public class ChessGameState {
    private boolean whoseTurn; // 0 for black, 1 for white.
    private Piece[][] board;  // Current board state.
    private Pair<Pair<Integer>> lastMove;  // Last move made.

    public ChessGameState() {
        board = new Piece[8][8];
    }

    public synchronized void update(Pair<Integer> start, Pair<Integer> end) {
        board[end.x][end.y] = board[start.x][start.y];
        board[start.x][start.y] = Piece.NONE;
        whoseTurn = !whoseTurn;
        lastMove = new Pair<Pair<Integer>>(start, end);
    }

    public synchronized boolean getWhoseTurn() {
        return whoseTurn;
    }

    public synchronized Piece[][] getBoard() {
        Piece[][] copy = new Piece[8][8];
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                copy[i][j] = board[i][j];
            }
        }
        return copy;
    }

    public synchronized Pair<Pair<Integer>> getLastMove() {
        return lastMove;
    }

    public static enum Piece {
        NONE, ROOK, KNIGHT, BISHOP, KING, QUEEN, PAWN;
    }

    public class Pair<T> {
        final T x, y;
        public Pair(T x, T y) {
            this.x = x;
            this.y = y;
        }
    }
}
