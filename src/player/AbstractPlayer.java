package player;

import exception.InvalidStateException;
import exception.PlayerFailureException;
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

    public abstract Board play(Board board) throws InvalidStateException, PlayerFailureException;
    public abstract void cleanup();

    public int movesLeft(Board board) {
        int moves = 0;
        for (BoardPiece queen : board.getPieces(this.team, BoardPiece.PieceType.QUEEN)) {
            moves+= board.getValidMoves(queen.getPos()).size();
        }
        return moves;
    }

    public boolean hasMoves(Board board){
        return movesLeft(board) != 0;
    }

    protected Map<BoardPiece, Map<Position, List<Position>>> getAllMoves(Board board){
        Map<BoardPiece, Map<Position, List<Position>>> allMoves = new HashMap<>();
        for(BoardPiece queen: board.getPieces(this.team, BoardPiece.PieceType.QUEEN)){
            allMoves.put(queen, board.getValidMoves(queen.getPos()));
        }
        return allMoves;
    }

    /**
     * Moved from RandomPlayer to here because most of the AIs will eventually play random moves once their strategy fails
     * @param board
     * @return
     */
    protected Board playRandomMove(Board board) throws InvalidStateException {
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

    protected Position getClosest(Collection<Position> poss, Position target){
        int bestDist = 9999;
        Position best = null;
        for(Position p: poss){
            if(target.dist(p) < bestDist){
                best = p;
                bestDist = target.dist(p);
            }
        }
        return best;
    }

    public Team getTeam() {
        return team;
    }
}
