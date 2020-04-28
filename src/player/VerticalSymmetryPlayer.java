package player;

import exception.InvalidStateException;
import state.Board;
import state.Position;
import state.Team;

public class VerticalSymmetryPlayer extends AbstractSymmetricPlayer{
    public VerticalSymmetryPlayer(Team team) {
        super(team);
    }

    @Override
    protected Position mirror(Position original) throws InvalidStateException {
        Position mirror = new Position(original.getX(), Math.abs(Board.BOARD_HEIGHT - original.getY() - 1));
        return mirror;
    }
}
