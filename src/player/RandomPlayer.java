package player;

import exception.InvalidStateException;
import exception.PlayerFailureException;
import state.Board;
import state.BoardPiece;
import state.Position;
import state.Team;

import java.util.*;

/**
 * Plays random moves
 */
public class RandomPlayer extends AbstractPlayer{
    public RandomPlayer(Team team) {
        super(team);
    }

    @Override
    public Board play(Board board) throws InvalidStateException, PlayerFailureException {
        return playRandomMove(board);
    }

    @Override
    public void cleanup() {

    }
}
