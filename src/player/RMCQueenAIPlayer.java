package player;

import state.*;
import ygraphs.ai.smart_fox.games.GameClient;

import java.util.List;

/**
 * Uses the amazons ai from https://github.com/rctcwyvrn/game-of-the-amazons-ai
 * Original author: https://github.com/rmcqueen
 */
public class RMCQueenAIPlayer extends AbstractPlayer{
    private GameClient client = GameClient.CLIENT_INSTANCE;

    public RMCQueenAIPlayer(Team team) {
        super(team);
        client.reset();
    }

    @Override
    public Board play(Board board) {
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
        client.reset();
    }
}
