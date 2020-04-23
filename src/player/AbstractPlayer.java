package player;

import state.Board;
import state.BoardPiece;
import state.Position;
import state.Team;

import java.util.*;

public abstract class AbstractPlayer {
    protected Team team;
    protected Random rand = new Random();

    public AbstractPlayer(Team team){
        this.team = team;
    }

    public abstract Board play(Board board);
    public abstract void cleanup();

    public boolean hasMoves(Board board){
        for(BoardPiece queen: board.getPieces(this.team, BoardPiece.PieceType.QUEEN)){
            if(!board.getValidMoves(queen.getPos()).isEmpty()){
                return true;
            }
        }
        return false;
    }

    protected Map<BoardPiece, Map<Position, List<Position>>> getAllMoves(Board board){
        Map<BoardPiece, Map<Position, List<Position>>> allMoves = new HashMap<>();
        for(BoardPiece queen: board.getPieces(this.team, BoardPiece.PieceType.QUEEN)){
            allMoves.put(queen, board.getValidMoves(queen.getPos()));
        }
        return allMoves;
    }

    /**
     * Moved from RandomPlayer to here because most of the AIs will have an endgame of playing random moves
     * @param board
     * @return
     */
    protected Board playRandomMove(Board board){
        List<BoardPiece> moveableQueens = board.getQueensWithValidMoves(team);
        BoardPiece chosenQueen = randomChoice(moveableQueens);

        Map<Position,List<Position>> movesAndArrows = board.getValidMoves(chosenQueen.getPos());
        List<Position> possibleMoves = new ArrayList<>(movesAndArrows.keySet());
        Position move = randomChoice(possibleMoves);

        List<Position> possibleArrows = movesAndArrows.get(move);
        Position arrow = randomChoice(possibleArrows);

        board.moveQueenAndFire(team, chosenQueen, move, arrow);
        return board;
    }
    /**
     * Lots of players use randomness to make decisions (because they're bad) so I'm putting this here
     * Grumble grumble why doesn't java have this by default
     * @param options
     * @param <T>
     * @return
     */
    protected <T> T randomChoice(List<T> options){
        if(options.size() == 1){
            return options.get(0);
        }else{
            return options.get(rand.nextInt(options.size() - 1));
        }
    }
}
