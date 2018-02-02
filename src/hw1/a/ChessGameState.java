package hw1.a;

/**
 * State class for a chess game. Outside classes will read and update this state
 * as the game progresses.
 *
 * I changed the API to return one deep copy of ChessGameStateObject instead of having an access method for each
 * state variable. This prevents a thread from getting an inconsistent game state constructed out of several sequential
 * API calls to get state which could be interrupted by correctly-timed context switches.
 */
public class ChessGameState {
    private boolean whoseTurn; // 0 for black, 1 for white. Default false
    private Piece board[][];  // Current board state.
    private Pair<Pair<Integer>> lastMove;  // Last move made.

    public ChessGameState() {
        board = new Piece[8][8];
    }

    public synchronized void update(Pair<Integer> start, Pair<Integer> end) {
        board[end.x][end.y] = board[start.x][start.y];
        board[start.x][start.y] = Piece.NONE;
        whoseTurn = !whoseTurn;
        lastMove = new Pair<>(start, end);
    }

    private Piece[][] getBoard() {
        Piece[][] copy = new Piece[8][8];
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                copy[i][j] = board[i][j];
            }
        }
        return copy;
    }

    public synchronized ChessGameState getGameState(){
        ChessGameState gs = new ChessGameState();
        gs.whoseTurn = whoseTurn;
        gs.board = getBoard();
        gs.lastMove = lastMove;
        return gs;
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
