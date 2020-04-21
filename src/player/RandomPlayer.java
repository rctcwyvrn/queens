package player;

import state.Board;
import state.BoardPiece;
import state.Position;
import state.Team;

import java.util.*;

public class RandomPlayer extends AbstractPlayer{

    private Random rand = new Random();

    public RandomPlayer(Team team) {
        super(team);
    }

    @Override
    public Board play(Board board) {
        List<BoardPiece> moveableQueens = board.getQueensWithValidMoves(team);
        BoardPiece chosenQueen = moveableQueens.get(Math.abs(rand.nextInt()) % moveableQueens.size());

        Map<Position,List<Position>> movesAndArrows = board.getValidMoves(chosenQueen.getPos());
        List<Position> possibleMoves = new ArrayList<>(movesAndArrows.keySet());
        Position move = possibleMoves.get(Math.abs(rand.nextInt()) % possibleMoves.size());
        List<Position> possibleArrows = movesAndArrows.get(move);
        Position arrow = possibleArrows.get(Math.abs(rand.nextInt())  % possibleArrows.size());

        board.moveQueenAndFire(team, chosenQueen, move, arrow);
        return board;
    }
}
