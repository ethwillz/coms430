public class Main{
    public static void main(String[] args){
        ChessGameState gs = new ChessGameState();
        Vector<Vector<Piece>> board = gs.getBoard();
        for(int i = 0; i < 8; i++){
            if(board.get(0).get(i) == Piece.NONE)
                System.out.println("Expected);
        }
    }
}