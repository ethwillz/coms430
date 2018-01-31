package hw1.one;

/**
 * State class for a chess game. Outside classes will read and update this state
 * as the game progresses.
 *
 * CHANGE LIST:
 * 1/28 Getting last move makes deep copy of pair object to return
 *      Update makes copy of start and end pairs instead of copying ref
 *
 * NOTES:
 * Do we actually need to make deep copies of Pair objects?
 * Does an AtomicBoolean actually increase performance?
 * Could check for valid piece at start on update call
 */
public class ChessGameStateCorrected {
    private boolean whoseTurn; // 0 for black, 1 for white. Default false
    private Piece board[][];  // Current board state.
    private volatile Pair<Pair<Integer>> lastMove;  // Last move made.

    public ChessGameStateCorrected() {
        board = new Piece[8][8];
    }

    public synchronized void update(Pair<Integer> start, Pair<Integer> end) {
        board[end.x][end.y] = board[start.x][start.y];
        board[start.x][start.y] = Piece.NONE;
        whoseTurn = !whoseTurn;
        Pair<Integer> startCopy = new Pair<>(start.x, start.y);
        Pair<Integer> endCopy = new Pair<>(end.x, end.y);
        lastMove = new Pair<>(startCopy, endCopy);
    }

    public synchronized boolean getWhoseTurn() {
        return whoseTurn;
    }

    public synchronized Piece[][] getBoard() {
        return board;
    }

    public Pair<Pair<Integer>> getLastMove() {
        Pair<Integer> lastMoveStart = new Pair<>(lastMove.x.x, lastMove.x.y);
        Pair<Integer> lastMoveEnd = new Pair<>(lastMove.y.x, lastMove.y.y);
        return new Pair<>(lastMoveStart, lastMoveEnd);
    }

    public enum Piece {
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
