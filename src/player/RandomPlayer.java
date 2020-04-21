package player;

import state.Board;
import state.BoardPiece;
import state.Position;
import state.Team;

import java.util.AbstractMap;
import java.util.List;
import java.util.Random;

public class RandomPlayer extends AbstractPlayer{

    private Random rand = new Random();

    public RandomPlayer(Team team) {
        super(team);
    }

    @Override
    public Board play(Board board) {
        List<BoardPiece> moveableQueens = board.getQueensWithValidMoves(team);
        BoardPiece chosenQueen = moveableQueens.get(Math.abs(rand.nextInt()) % moveableQueens.size());

        List<AbstractMap.SimpleEntry<Position,List<Position>>> possibleMoves = board.getValidMoves(chosenQueen.getPos());
        AbstractMap.SimpleEntry<Position,List<Position>> pair = possibleMoves.get(Math.abs(rand.nextInt()) % possibleMoves.size());
        Position move = pair.getKey();
        List<Position> possibleArrows = pair.getValue();
        Position arrow = possibleArrows.get(Math.abs(rand.nextInt())  % possibleArrows.size());

        board.moveQueenAndFire(team, chosenQueen, move, arrow);
        return board;
    }
}
