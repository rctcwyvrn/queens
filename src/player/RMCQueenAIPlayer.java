package player;

import exception.InvalidStateException;
import exception.PlayerFailureException;
import state.*;
import ygraphs.ai.smart_fox.games.GameClient;

import java.util.List;

/**
 * Uses the amazons ai from https://github.com/rctcwyvrn/game-of-the-amazons-ai
 * Original author: https://github.com/rmcqueen
 * Can't play anything other than 10x10 boards!
 */
public class RMCQueenAIPlayer extends AbstractPlayer{
    private GameClient client = new GameClient();

    public RMCQueenAIPlayer(Team team) {
        super(team);
    }

    @Override
    public Board play(Board board) throws InvalidStateException, PlayerFailureException  {
        Move lastMove = board.getLastMove();
        List<Position> AIPlay;
        if(lastMove == null){
            AIPlay = client.askAIForMove(team);
        }else{
            AIPlay = client.receiveMove(team, lastMove.getQueenPos().toListForRMC(), lastMove.getQueenMove().toListForRMC(), lastMove.getArrow().toListForRMC());

        }
        BoardPiece target = board.getQueenAt(AIPlay.get(0));
        board.moveQueenAndFire(team, target, AIPlay.get(1), AIPlay.get(2));
        return board;
    }

    @Override
    public void cleanup() {
    }
}
