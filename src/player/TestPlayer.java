package player;

import state.Board;

public class TestPlayer extends AbstractPlayer{
    @Override
    public Board play(Board b) {
        return b;
    }
}
