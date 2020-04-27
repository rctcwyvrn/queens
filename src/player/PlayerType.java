package player;

import player.decorator.RandomMovesDecorator;
import state.Team;

import java.lang.reflect.InvocationTargetException;

public enum PlayerType {
    Test(TestPlayer.class),
    Random(RandomPlayer.class),
    Headshot(HeadshotsOnlyPlayer.class),
    Assasin(AssasinPlayer.class),
    Invader(InvaderPlayer.class),
    Invader_50(RandomMovesDecorator.class),
    Invader_10(RandomMovesDecorator.class),
    Straight(StraightPlayer.class),
    UpClose(UpClosePlayer.class),
    Mob(MobPlayer.class),
    VerticalSym(VerticalSymmetryPlayer.class),
    HorizontalSym(HorizontalSymmetryPlayer.class),
    DiagonalSym(DiagonalSymmetricPlayer.class),
    Corner(CornerPlayer.class),
    AlternateCorner(AlternateCornerPlayer.class),
    RMCQueenAI(RMCQueenAIPlayer.class);

    private Class<? extends AbstractPlayer> player;
    PlayerType(Class<? extends AbstractPlayer> p){
        this.player = p;
    }

    public AbstractPlayer createPlayer(Team team){
        if(this.equals(Invader_50)){
            return new RandomMovesDecorator(new InvaderPlayer(team), 50);
        } else if(this.equals(Invader_10)){
            return new RandomMovesDecorator(new InvaderPlayer(team), 10);
        }

        try {
            return this.player.getDeclaredConstructor(Team.class).newInstance(team);
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("Failed to do my black magic reflect bullshit");
            System.exit(1);
            return null;
        }
    }

    public static PlayerType[] nonAIPlayers = new PlayerType[]{
            Random, Headshot, Assasin, Straight, UpClose, Mob, VerticalSym, HorizontalSym, DiagonalSym, Corner, AlternateCorner
    };

    public static PlayerType[] nonInvaderPlayers = new PlayerType[]{
            Random, Headshot, Assasin, Straight, UpClose, Mob, VerticalSym, HorizontalSym, DiagonalSym, Corner, AlternateCorner, RMCQueenAI,
    };
}
