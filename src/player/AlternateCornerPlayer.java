package player;

import exception.InvalidStateException;
import state.Board;
import state.BoardPiece;
import state.Position;
import state.Team;

import java.util.Collections;
import java.util.List;

/**
 * Play to the spot 2 blocks away from the corner instead of literally inside the corner
 */
public class AlternateCornerPlayer extends CornerPlayer{
    public AlternateCornerPlayer(Team team) {
        super(team);
    }

    protected void chooseCorners(Board board) throws InvalidStateException {
        List<BoardPiece> queens = board.getPieces(team, BoardPiece.PieceType.QUEEN);
        //Collections.shuffle(queens);
        corners.put(queens.get(0).getQueenID(), new Position(2,2));
        corners.put(queens.get(1).getQueenID(), new Position(2,Board.BOARD_HEIGHT - 3));
        corners.put(queens.get(3).getQueenID(), new Position(Board.BOARD_WIDTH - 3,2));
        corners.put(queens.get(2).getQueenID(), new Position(Board.BOARD_WIDTH - 3,Board.BOARD_HEIGHT - 3));
    }
}
