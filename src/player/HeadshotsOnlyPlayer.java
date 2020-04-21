package player;

import state.Board;
import state.BoardPiece;
import state.Position;
import state.Team;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HeadshotsOnlyPlayer extends AbstractPlayer {
    public HeadshotsOnlyPlayer(Team team) {
        super(team);
    }

    @Override
    public Board play(Board board) {
        List<Position> enemyQueens = board.getPieces(team.getOther(), BoardPiece.PieceType.QUEEN).stream().map(x -> x.getPos()).collect(Collectors.toList());
        Map<Position, Map<Position, List<Position>>> allMoves = getAllMoves(board);
        int shortestDist = 1000;
        Position queen = null;
        Position move = null;
        Position shot = null;
        for(Map<Position, List<Position>> possibleMoves: allMoves.values()){
            for(List<Position> arrows: possibleMoves.values()){
                for(Position arrow: arrows){
                    for(Position enemy: enemyQueens){
                        if(arrow.dist(enemy) < shortestDist){
                            queen = (Position) allMoves.entrySet().stream().filter(x -> x.getValue().equals(possibleMoves)).map(x -> x.getKey()).toArray()[0];
                            move = (Position) possibleMoves.entrySet().stream().filter(x-> x.getValue().contains(arrow)).map(x -> x.getKey()).toArray()[0];
                            shot = arrow;
                            if(arrow.dist(enemy) == 1){
                                board.moveQueenAndFire(team, board.getQueenAt(queen), move, shot);
                                return board;
                            }
                        }
                    }
                }
            }
        }
        board.moveQueenAndFire(team, board.getQueenAt(queen), move, shot);
        return board;
    }
}
