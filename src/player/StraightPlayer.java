package player;

import state.Board;
import state.BoardPiece;
import state.Position;
import state.Team;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Only moves and shoots in straight lines, choosing randomly
 * This one sucks real bad
 */
public class StraightPlayer extends AbstractPlayer{
    public StraightPlayer(Team team) {
        super(team);
    }

    @Override
    public Board play(Board board) {
        List<BoardPiece> moveableQueens = board.getQueensWithValidMoves(team);
        Collections.shuffle(moveableQueens);
        for(BoardPiece chosenQueen: moveableQueens) {

            Map<Position, List<Position>> movesAndArrows = board.getValidMoves(chosenQueen.getPos());
            List<Position> possibleMoves = new ArrayList<>(movesAndArrows.keySet());
            List<Position> straightMoves = possibleMoves.stream().filter(pos -> pos.getX() == chosenQueen.getPos().getX() ||
                    pos.getY() == chosenQueen.getPos().getY()).collect(Collectors.toList());

            if (straightMoves.size() > 0) {
                Position move = randomChoice(straightMoves);
                List<Position> possibleArrows = movesAndArrows.get(move);
                List<Position> straightArrows = possibleArrows.stream().filter(pos -> pos.getX() == move.getX() ||
                        pos.getY() == move.getY()).collect(Collectors.toList());

                if(straightArrows.size() > 0) {
                    Position arrow = randomChoice(straightArrows);
                    board.moveQueenAndFire(team, chosenQueen, move, arrow);
                    return board;
                }
            }
        }
        // If we don't find any straight moves to play
        return playRandomMove(board);
    }

    @Override
    public void cleanup() {

    }
}
