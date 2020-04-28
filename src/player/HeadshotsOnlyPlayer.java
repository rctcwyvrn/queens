package player;

import exception.InvalidStateException;
import exception.PlayerFailureException;
import state.Board;
import state.BoardPiece;
import state.Position;
import state.Team;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Attempts to shoot arrows as close as possible to any enemy queens
 */
public class HeadshotsOnlyPlayer extends AbstractPlayer {
    public HeadshotsOnlyPlayer(Team team) {
        super(team);
    }

    @Override
    public Board play(Board board) throws InvalidStateException, PlayerFailureException {
        List<Position> enemyQueens = board.getPieces(team.getOther(), BoardPiece.PieceType.QUEEN).stream().map(x -> x.getPos()).collect(Collectors.toList());
        Collections.shuffle(enemyQueens);
        Map<BoardPiece, Map<Position, List<Position>>> allMoves = getAllMoves(board);
        int shortestDist = 1000;
        BoardPiece queen = null;
        Position move = null;
        Position shot = null;
        for(Map<Position, List<Position>> possibleMoves: allMoves.values()){
            for(List<Position> arrows: possibleMoves.values()){
                for(Position arrow: arrows){
                    for(Position enemy: enemyQueens){
                        if(arrow.dist(enemy) < shortestDist){
                            queen = (BoardPiece) allMoves.entrySet().stream().filter(x -> x.getValue().equals(possibleMoves)).map(x -> x.getKey()).toArray()[0];
                            move = (Position) possibleMoves.entrySet().stream().filter(x-> x.getValue().contains(arrow)).map(x -> x.getKey()).toArray()[0];
                            shot = arrow;
                            if(arrow.dist(enemy) == 1){
                                board.moveQueenAndFire(team, queen, move, shot);
                                return board;
                            }
                        }
                    }
                }
            }
        }
        board.moveQueenAndFire(team, queen, move, shot);
        return board;
    }

    @Override
    public void cleanup() {

    }
}
