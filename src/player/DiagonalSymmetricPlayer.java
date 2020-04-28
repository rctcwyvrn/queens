package player;

import exception.InvalidStateException;
import state.Board;
import state.Position;
import state.Team;

public class DiagonalSymmetricPlayer extends AbstractSymmetricPlayer {
    public DiagonalSymmetricPlayer(Team team) {
        super(team);
    }

    @Override
    protected Position mirror(Position original) throws InvalidStateException {
        Position mirror = new Position(Math.abs(Board.BOARD_WIDTH - original.getX() - 1), Math.abs(Board.BOARD_HEIGHT - original.getY() - 1));
        return mirror;
    }
}
