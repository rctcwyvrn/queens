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
    private PlayerType loser;
    private List<Move> moveLog;
    private int roundCount;
    private int winningScore;

    public GameResult(Team team, PlayerType winner, PlayerType loser, List<Move> moveLog, int winningScore, int roundCount, long runtime) {
        this.team = team;
        this.winner = winner;
        this.loser = loser;
        this.moveLog = moveLog;
        this.winningScore = winningScore;
        this.roundCount = roundCount;
        this.runtime = runtime;
    }

    public Team getTeam() {
        return team;
    }

    public PlayerType getWinner() {
        return winner;
    }

    public PlayerType getLoser() {
        return loser;
    }

    public List<Move> getMoveLog() {
        return moveLog;
    }

    public int getRoundCount() {
        return roundCount;
    }

    public String convertToCSV(){
        return winner + "," + loser + "," + winningScore + "," + roundCount + "," + runtime;
    }
    @Override
    public String toString() {
        return "GameResult{" +
                "runtime=" + runtime +
                ", team=" + team +
                ", winner=" + winner +
                ", loser=" + loser +
                ", roundCount=" + roundCount +
                ", winningScore=" + winningScore +
                '}';
    }
}
