package game;

import player.AbstractPlayer;
import player.PlayerType;
import state.Board;
import state.Team;

public class GameRunner {
    private static final boolean PRINT_BOARDS = false;

    public static void main(String[] args){
        PlayerType p1Type = PlayerType.valueOf(args[0]);
        PlayerType p2Type = PlayerType.valueOf(args[1]);
        int gamesToPlay = Integer.valueOf(args[2]);
        int blackWins = 0;
        int whiteWins = 0;
        for(int i = 0; i < gamesToPlay; i++) {
            GameResult result = playGame(p1Type, p2Type);
            System.out.println(result);

            if(result.getTeam().equals(Team.WHITE)){
                whiteWins+=1;
            }else{
                blackWins+=1;
            }
        }
        System.out.println("White: " + whiteWins + " | Black: " + blackWins);
    }

    public static GameResult playGame(PlayerType p1Type, PlayerType p2Type){
        AbstractPlayer player1 = p1Type.createPlayer(Team.WHITE);
        System.out.println("Player #1 (WHITE): " + p1Type + " | " + player1);

        AbstractPlayer player2 = p2Type.createPlayer(Team.BLACK);
        System.out.println("Player #2 (BLACK): " + p2Type + " | " + player2);

        Board state = new Board();
        if(PRINT_BOARDS) System.out.println(state.displayBoard());
        int totalRounds = 0;
        long startTime = System.currentTimeMillis();
        while(true){
            if(!player1.hasMoves(state)){
                if(PRINT_BOARDS) System.out.println(state.displayBoard());
                System.out.println("Game over! Player 2 (BLACK) wins");
                return new GameResult(Team.BLACK, p2Type, state.getMoveLog(), totalRounds, System.currentTimeMillis() - startTime);
            }

            state = player1.play(state);
            if(PRINT_BOARDS) System.out.println(state.displayBoard());
            totalRounds+=1;

            if(!player2.hasMoves(state)){
                if(PRINT_BOARDS) System.out.println(state.displayBoard());
                System.out.println("Game over! Player 1 (WHITE) wins");
                return new GameResult(Team.WHITE, p1Type, state.getMoveLog(), totalRounds, System.currentTimeMillis() - startTime);
            }

            state = player2.play(state);
            if(PRINT_BOARDS) System.out.println(state.displayBoard());
            totalRounds+=1;
        }
    }
}
