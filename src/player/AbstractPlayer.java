package player;

import state.Board;
import state.BoardPiece;
import state.Position;
import state.Team;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    protected Map<Position, Map<Position, List<Position>>> getAllMoves(Board board){
        Map<Position, Map<Position, List<Position>>> allMoves = new HashMap<>();
        for(BoardPiece queen: board.getPieces(this.team, BoardPiece.PieceType.QUEEN)){
            allMoves.put(queen.getPos(), board.getValidMoves(queen.getPos()));
        }
        return allMoves;
    }
}
