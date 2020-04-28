import state.Board;
import state.BoardPiece;
import state.Team;

public class BoardMovesTest {
    public static void main(String[] args){
        Board state = BoardPiece.createBoard();
        System.out.println(state.displayBoard());

        BoardPiece test = state.getPieces(Team.WHITE, BoardPiece.PieceType.QUEEN).get(0);
        System.out.println("Piece " + test + " has moves " + state.getValidMoves(test.getPos()));
    }
}
