package hw1;

/**
 * State class for a chess game. Outside classes will read and update this state
 * as the game progresses.
 */
public class ChessGameState {
    private boolean whoseTurn; // 0 for black, 1 for white. Default false
    private Vector<Vector<Piece>> board;  // Current board state.
    private Pair<Pair<Integer>> lastMove;  // Last move made.

    public ChessGameState() {
        board = new Vector();
        for(int i = 0; i < 8; i++){
            board.add(new Vector<Piece>());
            for(int j = 0; j < 8; j++){
                board.get(i).add(Piece.NONE);
            }
        }
    }

    public synchronized void update(Pair<Integer> start, Pair<Integer> end) {
        Pair<Integer> startCopy = new Pair(start.x, start.y);
        Pair<Integer> endCopy = new Pair(end,x, end.y);
        board[endCopy.x][endCopy.y] = board[start.x][start.y];
        board[startCopy.x][startCopy.y] = piece.NONE;
        whoseTurn = !whoseTurn;
        lastMove = new Pair<Pair<Integer>>(start, end);
    }

    public synchronized boolean getWhoseTurn() {
        return whoseTurn;
    }

    public synchronized Vector<Vector<Piece>> getBoard() {
        Vector<Vector<Piece>> temp = new Vector<>();
        for(int i = 0; i < 8; i++){
            temp.add(board.get(i));
        }
        return Collections.unmodifiableList(temp);
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
