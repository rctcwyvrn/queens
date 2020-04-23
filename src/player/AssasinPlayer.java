package player;


import state.Board;
import state.BoardPiece;
import state.Position;
import state.Team;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Fires arrows at a specific queen until it is out of moves or cannot be reached, before moving on to the next queen target
 */
public class AssasinPlayer extends AbstractPlayer{
    private BoardPiece currTarget = null;
    private Set<BoardPiece> oldTargets = new HashSet<>();
    private HeadshotsOnlyPlayer headshot = new HeadshotsOnlyPlayer(team);
    private int unreachableCount = 0;

    public AssasinPlayer(Team team) {
        super(team);
    }

    @Override
    public Board play(Board board) {
        if(oldTargets.size() == 4){
            //Play as headshot from now on
            return headshot.play(board);
        }

        if(currTarget == null){
            List<BoardPiece> enemyQueens = board.getPieces(team.getOther(), BoardPiece.PieceType.QUEEN);
            enemyQueens.removeAll(oldTargets);
            currTarget = randomChoice(enemyQueens);
            unreachableCount = 0;
            //System.out.print("Target: " + currTarget);
        }

        Position targetPos = currTarget.getPos();
        Map<BoardPiece, Map<Position, List<Position>>> allMoves = getAllMoves(board);
        for(Map<Position, List<Position>> possibleMoves: allMoves.values()){
            for(List<Position> arrows: possibleMoves.values()){
                for(Position arrow: arrows){
                    if(arrow.dist(targetPos) == 1){
                        BoardPiece queen = (BoardPiece) allMoves.entrySet().stream().filter(x -> x.getValue().equals(possibleMoves)).map(x -> x.getKey()).toArray()[0];
                        Position move = (Position) possibleMoves.entrySet().stream().filter(x-> x.getValue().contains(arrow)).map(x -> x.getKey()).toArray()[0];
                        Position shot = arrow;
                        board.moveQueenAndFire(team, queen, move, shot);
                        return board;
                    }
                }
            }
        }

        unreachableCount+=1;
        if(unreachableCount >= 1) { //This gives the best results for some reason
            oldTargets.add(currTarget);
            currTarget = null;
            return play(board);
        }else{
            return playRandomMove(board);
        }
    }
}
