package game;

import player.PlayerType;
import util.EloRating;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

import static java.util.stream.Collectors.toMap;

/**
 *  Main runner for the project: Runs all the players against each other, outputs results in a csv, and calculates elo ratings for everyone
 */
public class EloRunner {
    private static final int GAMES_PER_MATCHUP = 10;

    public static void main(String[] args){
        Map<PlayerType, EloRating> ratings = new EnumMap<PlayerType, EloRating>(PlayerType.class);
        List<GameResult> results = new ArrayList<>();

        //PlayerType[] allPlayers = PlayerType.values();
        PlayerType[] allPlayers = PlayerType.nonAIPlayers;
        //PlayerType[] allPlayers = PlayerType.nonInvaderPlayers;

        for(PlayerType p: allPlayers){
            ratings.put(p, new EloRating());
        }

        for(int i = 0; i < GAMES_PER_MATCHUP; i++) {
            System.out.println("Playing tournament #" + i);
            for (PlayerType p1 : allPlayers) {
                for (PlayerType p2 : allPlayers) {
                    if(p1 != p2) {
                        //System.out.println("Playing " + p1 + " vs " + p2);
                        results.add(GameRunner.playGame(p1, p2));
                    }
                }
            }
        }

        for(GameResult result: results){
            PlayerType winner = result.getWinner();
            PlayerType loser = result.getLoser();
            EloRating.updateRatings(ratings.get(winner), ratings.get(loser));
        }

        Comparator<EloRating> eloRatingComparator = new Comparator<EloRating>() {
            @Override
            public int compare(EloRating o1, EloRating o2) {
                return (int) (o1.getRating() - o2.getRating());
            }
        };

        Map<PlayerType, EloRating> sortedRatings = ratings.entrySet().stream().sorted(Map.Entry.comparingByValue(eloRatingComparator))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        File eloOutputFile = new File("results/elo.txt");
        try(PrintWriter pw = new PrintWriter(eloOutputFile)){
            sortedRatings.entrySet().stream()
                    .map(x -> x.getKey() + ": " + (int) x.getValue().getRating())
                    .forEach(pw::println);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
