package game;

import player.PlayerType;
import util.EloRating;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.toMap;

/**
 *  Main runner for the project: Runs all the players against each other, outputs results in a csv, and calculates elo ratings for everyone
 */
public class EloRunner {
    private static final int GAMES_PER_MATCHUP = 2;
    private static final int INVADER_GAMES_PER_MATCHUP = 1;
    private static final int THREAD_COUNT = 8;
    private static ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);

    public static void main(String[] args) throws Exception{
        Map<PlayerType, EloRating> ratings = new EnumMap<PlayerType, EloRating>(PlayerType.class);
        List<GameResult> results = new ArrayList<>();

        //PlayerType[] allPlayers = PlayerType.values();
        //PlayerType[] allPlayers = PlayerType.nonAIPlayers;
        //PlayerType[] allPlayers = PlayerType.nonInvaderPlayers;

        System.out.println("Running tournament with " + GAMES_PER_MATCHUP + " iterations. ExecutorService nThreads = " + THREAD_COUNT);
        for(PlayerType p: PlayerType.values()){
            ratings.put(p, new EloRating());
        }

        //Play the Invader games
        for(int i = 0; i < INVADER_GAMES_PER_MATCHUP; i++) {
            for(PlayerType invader: PlayerType.invaderPlayers) {
                for (PlayerType other : PlayerType.nonInvaderPlayers) {
                    System.out.println("Playing " + invader + " vs " + other);
                    results.add(GameRunner.playGame(invader, other));

                    System.out.println("Playing " + other + " vs " + invader);
                    results.add(GameRunner.playGame(other, invader));
                }
            }
        }

        AtomicInteger completed = new AtomicInteger();
        int totalGames = 0;
        for(int i = 0; i < GAMES_PER_MATCHUP; i++) {
            //System.out.println("Playing tournament #" + i);
            for (PlayerType p1 : PlayerType.nonInvaderPlayers) {
                for (PlayerType p2 : PlayerType.nonInvaderPlayers) {
                    if(p1 != p2) {
                        //System.out.println("Playing " + p1 + " vs " + p2);
                        Thread task = new Thread(() -> {
                            results.add(GameRunner.playGame(p1, p2));
                            completed.addAndGet(1);

                        });
                        executorService.submit(task);
                        totalGames +=1;
                    }
                }
            }
        }

        System.out.println("Done submitting all tasks: Awaiting termination");
        executorService.shutdown();
        int last = 0;
        while(!executorService.awaitTermination(1, TimeUnit.SECONDS)) {
            if(last != completed.get()) {
                System.out.println("Games completed = " + completed + "/" + totalGames);
                last = completed.get();
            }
        }

        Collections.shuffle(results);
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

        File csvResultsFile = new File("results/results.csv");
        try(PrintWriter pw = new PrintWriter(csvResultsFile)){
            pw.println("Winner,Loser,Score,Rounds,Runtime");
            results.stream()
                    .map(x -> x.convertToCSV())
                    .forEach(pw::println);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
