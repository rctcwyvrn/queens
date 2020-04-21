import player.PlayerType;
import state.Board;
import state.Team;

public class GameRunner {
    public static void main(String[] args){
        PlayerType p1Type = PlayerType.valueOf(args[0]);
        System.out.println("Player #1 : " + p1Type + " | " + p1Type.getPlayer());
        p1Type.getPlayer().setTeam(Team.WHITE);

        PlayerType p2Type = PlayerType.valueOf(args[0]);
        System.out.println("Player #2 : " + p2Type + " | " + p2Type.getPlayer());
        p1Type.getPlayer().setTeam(Team.BLACK);

        Board state = new Board();
        System.out.println(state.displayBoard());
        
        Board next = p1Type.getPlayer().play(state);
        System.out.println(next.displayBoard());

    }
}
