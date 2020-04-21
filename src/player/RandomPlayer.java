package player;

import state.Board;
import state.BoardPiece;
import state.Position;

import java.util.List;
import java.util.Random;

public class RandomPlayer extends AbstractPlayer{

    private Random rand = new Random();

    @Override
    public Board play(Board board) {
        List<BoardPiece> moveableQueens = board.getPieces(this.team, BoardPiece.PieceType.QUEEN);
        BoardPiece chosenQueen = moveableQueens.get(Math.abs(rand.nextInt()) % moveableQueens.size());

        List<Position> possibleMoves = BoardPiece.getValidMoves(chosenQueen.getPos());
        Position move = possibleMoves.get(Math.abs(rand.nextInt()) % possibleMoves.size());

        possibleMoves = BoardPiece.getValidMoves(move);
        Position arrow = possibleMoves.get(Math.abs(rand.nextInt())  % possibleMoves.size());
        System.out.println("Random player has chosen the queen at " + chosenQueen.getPos() + " and plans to move it to " + move + ", and to fire an arrow to " + arrow);

        board.moveQueenAndFire(team, chosenQueen, move, arrow);
        return board;
    }
}
