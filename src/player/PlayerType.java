package player;

import state.Team;

import java.lang.reflect.InvocationTargetException;

public enum PlayerType {
    Test(TestPlayer.class),
    Random(RandomPlayer.class),
    Headshot(HeadshotsOnlyPlayer.class),
    RMCQueenAI(RMCQueenAIPlayer.class);

    private Class<? extends AbstractPlayer> player;
    PlayerType(Class<? extends AbstractPlayer> p){
        this.player = p;
    }

    public AbstractPlayer createPlayer(Team team){
        try {
            return this.player.getDeclaredConstructor(Team.class).newInstance(team);
        } catch (Exception e){
            System.out.println("Failed to do my black magic reflect bullshit");
            System.exit(1);
            return null;
        }
    }
}
