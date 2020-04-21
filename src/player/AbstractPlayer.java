package player;

import state.Board;
import state.BoardPiece;
import state.Team;

public abstract class AbstractPlayer {
    protected Team team;
    public abstract Board play(Board board);

    public AbstractPlayer(Team team){
        this.team = team;
    }

    public boolean hasMoves(Board board){
        for(BoardPiece queen: board.getPieces(this.team, BoardPiece.PieceType.QUEEN)){
            if(!board.getValidMoves(queen.getPos()).isEmpty()){
                return true;
            }
        }
        return false;
    }
}
