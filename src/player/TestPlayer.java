package player;

import state.Board;
import state.Team;

/**
 * Testing purposes only
 */
public class TestPlayer extends AbstractPlayer{
    public TestPlayer(Team team) {
        super(team);
    }

    @Override
    public Board play(Board b) {
        return b;
    }

    @Override
    public void cleanup() {

    }
}
