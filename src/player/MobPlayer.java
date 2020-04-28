package player;

import exception.InvalidStateException;
import exception.PlayerFailureException;
import state.Board;
import state.BoardPiece;
import state.Position;
import state.Team;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MobPlayer extends AbstractPlayer{
    private BoardPiece currTarget = null;
    private Set<BoardPiece> oldTargets = new HashSet<>();
    private HeadshotsOnlyPlayer headshot = new HeadshotsOnlyPlayer(team);
    private int unreachableCount = 0;

    public MobPlayer(Team team) {
        super(team);
    }

    @Override
    public Board play(Board board) throws InvalidStateException, PlayerFailureException {
        if(currTarget != null && board.getValidMoves(currTarget.getPos()).isEmpty()) {
            oldTargets.add(currTarget);
            currTarget = null;
        }

        if(oldTargets.size() == 4){
            //Play as random from now on
            return playRandomMove(board);
        }

        if(currTarget == null){
            List<BoardPiece> enemyQueens = board.getPieces(team.getOther(), BoardPiece.PieceType.QUEEN);
            enemyQueens.removeAll(oldTargets);
            currTarget = randomChoice(enemyQueens);
            //System.out.println("Target: " + currTarget);
        }

        Position targetPos = currTarget.getPos();
        Map<BoardPiece, Map<Position, List<Position>>> allMoves = getAllMoves(board);

        BoardPiece queen = null;
        Position bestMove = null;
        int bestDist = 999;
        for(Map<Position, List<Position>> possibleMoves: allMoves.values()){
            for(Position move: possibleMoves.keySet()){
                if(move.dist(targetPos) < bestDist) {
                    BoardPiece potentialNextQueen = (BoardPiece) allMoves.entrySet().stream()
                            .filter(x -> x.getValue().equals(possibleMoves))
                            .map(x -> x.getKey())
                            .toArray()[0];
                    if(potentialNextQueen.getPos().dist(targetPos) > 2) { // prioritize finding good moves for pieces that are far from the target
                        bestDist = move.dist(targetPos);
                        queen = potentialNextQueen;
                        bestMove = move;
                    }
                }
            }
        }

        // all queens are probably very close to the target, pick whatever move is closest
        if(bestMove == null){
            //System.out.println("All queens close to target");
            for(Map<Position, List<Position>> possibleMoves: allMoves.values()){
                for(Position move: possibleMoves.keySet()){
                    if(move.dist(targetPos) < bestDist) {
                        bestDist = move.dist(targetPos);
                        queen = (BoardPiece) allMoves.entrySet().stream()
                                .filter(x -> x.getValue().equals(possibleMoves))
                                .map(x -> x.getKey())
                                .toArray()[0];
                        bestMove = move;
                    }
                }
            }
        }
        //System.out.println("Best dist = " + bestDist);
//        if(bestDist > 3){
//            // Seems like we can't reach that piece anymore :c
//            System.out.println("Switching targets, bestDist = " + bestDist);
//            oldTargets.add(currTarget);
//            currTarget = null;
//            return play(board);
//        }

        Position shot = randomChoice(allMoves.get(queen).get(bestMove));
        board.moveQueenAndFire(team, queen, bestMove, shot);
        return board;
    }

    @Override
    public void cleanup() {

    }
}
