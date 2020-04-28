package player;

import exception.InvalidStateException;
import exception.PlayerFailureException;
import state.Board;
import state.BoardPiece;
import state.Position;
import state.Team;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Everyone for themselves player
 * Moves each queen to their own corner and tries to stay there
 * Fires shots close to the center
 */
public class CornerPlayer extends AbstractPlayer{
    protected Map<Integer, Position> corners = new HashMap<>();
    private boolean initalized = false;

    public CornerPlayer(Team team) {
        super(team);
    }

    @Override
    public Board play(Board board) throws InvalidStateException, PlayerFailureException {
        if(!initalized){
            chooseCorners(board);
            initalized = true;
        }
        Map<Integer, Position> bestMoves = new HashMap<>();
        for(Map.Entry<Integer, Position> corner: corners.entrySet()){
            BoardPiece queen = board.findQueenWithID(corner.getKey());
            Map<Position, List<Position>> moves = board.getValidMoves(queen.getPos());
            if(!moves.keySet().isEmpty()) {
                Position bestMove = getClosest(moves.keySet(), corner.getValue()); // best move for this queen to its corner
                bestMoves.put(corner.getKey(), bestMove);
            }
        }

        int bestDistImprovement = -99;
        Position bestMove = null;
        BoardPiece queen = null;
        for(Map.Entry<Integer, Position> goodMove: bestMoves.entrySet()){
            int dist = goodMove.getValue().dist(corners.get(goodMove.getKey())); // dist between the chosen move and the corner
            int currDist = board.findQueenWithID(goodMove.getKey()).getPos().dist(corners.get(goodMove.getKey())); //dist between the current position and the corner
            int improvement = currDist - dist;
            if(improvement > bestDistImprovement){
                bestDistImprovement = improvement;
                bestMove = goodMove.getValue();
                queen = board.findQueenWithID(goodMove.getKey());
            }
        }

        Position arrow = getClosest(board.getValidMoves(queen.getPos()).get(bestMove), Board.CENTER);
        board.moveQueenAndFire(team, queen, bestMove, arrow);
        return board;
    }

    protected void chooseCorners(Board board) throws InvalidStateException {
        List<BoardPiece> queens = board.getPieces(team, BoardPiece.PieceType.QUEEN);
        Collections.shuffle(queens);
        corners.put(queens.get(0).getQueenID(), new Position(0,0));
        corners.put(queens.get(1).getQueenID(), new Position(0,Board.BOARD_HEIGHT - 1));
        corners.put(queens.get(2).getQueenID(), new Position(Board.BOARD_WIDTH - 1,0));
        corners.put(queens.get(3).getQueenID(), new Position(Board.BOARD_WIDTH - 1,Board.BOARD_HEIGHT - 1));
    }

    @Override
    public void cleanup() {

    }
}
