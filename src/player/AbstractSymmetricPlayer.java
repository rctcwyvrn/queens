package player;

import state.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Attempts to mirror the last move along the mirror defined in the mirror function
 * Finds the move and then the arrow
 * Mirror is defined in terms of where the move and arrow end up, but not where the queen started. Because most moves would be possible to mirror that closely I think
 */
public abstract class AbstractSymmetricPlayer extends AbstractPlayer{
    public AbstractSymmetricPlayer(Team team) {
        super(team);
    }

    @Override
    public Board play(Board board) {
        Move lastMove = board.getLastMove();
        if(lastMove == null){ // If we go first
            return playRandomMove(board);
        }
        Map<BoardPiece, Map<Position, List<Position>>> allMoves = getAllMoves(board);
        List<Position> allMovePositions = allMoves.values().stream().map(x -> x.keySet()).flatMap(x -> x.stream()).collect(Collectors.toList());
        Position mirroredMove = mirror(lastMove.getQueenMove());
        Position bestMove = getClosest(allMovePositions, mirroredMove);

        // For sure we're playing this move, so just see if we can also match the arrow
        BoardPiece queen = (BoardPiece) allMoves.keySet().stream().filter(q -> allMoves.get(q).keySet().contains(bestMove)).toArray()[0];
        Position mirroredArrow = mirror(lastMove.getArrow());
        List<Position> validArrows = allMoves.get(queen).get(bestMove);
        Position bestArrow = getClosest(validArrows, mirroredArrow);
        // Regardless of this is the perfect mirror or not we make the shot anyway
        board.moveQueenAndFire(team, queen, bestMove, bestArrow);
        return board;
    }

    protected abstract Position mirror(Position original);

    @Override
    public void cleanup() {

    }
}
