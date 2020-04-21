package player;

import state.Board;
import state.Team;

public abstract class AbstractPlayer {
    protected Team team;
    public abstract Board play(Board b );

    public void setTeam(Team team){
        this.team = team;
    }
}
