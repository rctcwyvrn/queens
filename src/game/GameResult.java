package game;

import player.PlayerType;
import state.Move;
import state.Position;
import state.Team;

import java.util.List;

public class GameResult {

    private long runtime;
    private Team team;
    private PlayerType winner;
    private List<Move> moveLog;
    private int roundCount;

    public GameResult(Team team, PlayerType winner, List<Move> moveLog, int roundCount, long runtime) {
        this.team = team;
        this.winner = winner;
        this.moveLog = moveLog;
        this.roundCount = roundCount;
        this.runtime = runtime;
    }

    public Team getTeam() {
        return team;
    }

    public PlayerType getWinner() {
        return winner;
    }

    public List<Move> getMoveLog() {
        return moveLog;
    }

    public int getRoundCount() {
        return roundCount;
    }

    @Override
    public String toString() {
        return "GameResult{" +
                "runtime=" + runtime/1000 + "s" +
                ", team=" + team +
                ", winner=" + winner +
                ", roundCount=" + roundCount +
                '}';
    }
}
