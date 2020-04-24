package player;

import state.Board;
import state.BoardPiece;
import state.Position;
import state.Team;

import javax.swing.*;
import java.util.*;

public class UpClosePlayer extends AbstractPlayer{
    private Queue<BoardPiece> queenQueue = new LinkedList<BoardPiece>();
    private Map<Integer, Integer> targets = new HashMap<>();
    private boolean initialized = false;
    public UpClosePlayer(Team team) {
        super(team);
    }

    @Override
    public Board play(Board board) {
        if(!initialized){
            initialize(board);
        }
        BoardPiece myQueen = queenQueue.poll();
        Map<Position, List<Position>> moves = board.getValidMoves(myQueen.getPos());
        queenQueue.add(myQueen);
        if(moves.size() > 0) {
            BoardPiece target = board.findQueenWithID(targets.get(myQueen.getQueenID()));
            //System.out.println("Moving " + myQueen + " which is targetting " + target);
            Position bestMove = getClosest(moves.keySet(), target.getPos());
            Position arrow = getClosest(moves.get(bestMove), target.getPos());
            board.moveQueenAndFire(team, myQueen, bestMove, arrow);
            return board;
        } else {
            return play(board); //recurse and try the next queen, since one of the must have a valid move somewhere
        }
    }

    public void initialize(Board board){
        queenQueue.addAll(board.getPieces(team, BoardPiece.PieceType.QUEEN));
        for(BoardPiece enemyQueen: board.getPieces(team.getOther(), BoardPiece.PieceType.QUEEN)){
            BoardPiece myQueen = queenQueue.poll();
            targets.put(myQueen.getQueenID(), enemyQueen.getQueenID());
            queenQueue.add(myQueen);
        }
        initialized = true;
    }

    @Override
    public void cleanup() {

    }
}
