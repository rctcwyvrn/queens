package player.decorator;

import exception.InvalidStateException;
import exception.PlayerFailureException;
import player.AbstractPlayer;
import state.Board;
import state.Team;

/**
 * Decorates a player to play the proper player move playPercent % of the time
 */
public class RandomMovesDecorator extends AbstractPlayer {

    private AbstractPlayer player;
    private int playPercent;

    public RandomMovesDecorator(AbstractPlayer player, int playPercent) {
        super(player.getTeam());
        this.player = player;
        this.playPercent = playPercent;
    }

    @Override
    public Board play(Board board) throws PlayerFailureException, InvalidStateException {
        if(rand.nextInt(99) + 1 <= playPercent){
            return player.play(board);
        }else{
            return playRandomMove(board);
        }
    }

    @Override
    public void cleanup() {
        player.cleanup();
    }
}
