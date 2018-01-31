package hw1.one;

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
    lastMove = new Pair<>(start, end);
  }

  public synchronized boolean getWhoseTurn() {
  return whoseTurn;
  }
  
  public synchronized Piece[][] getBoard() {
    return board;
  }
  
  public synchronized Pair<Pair<Integer>> getLastMove() {
    return lastMove;
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
