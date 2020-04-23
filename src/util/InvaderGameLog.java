package util;

import state.Move;
import state.Position;
import state.Team;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Methods for loading the game log/parsing the move made by the AI and methods for generating a new log with a new move in it
 */
public class InvaderGameLog {
    private static final String FILE_PREFIX = "<GAME-INFO-START>\n" +
            "\n" +
            "Players:\n" +
            "  Red  - Computer (~3s)\n" +
            "  Blue - Computer (~3s)\n" +
            "\n" +
            "RED-TYPE=1:1:5:3:Computer\n" +
            "BLUE-TYPE=1:1:5:3:Computer\n" +
            "\n" +
            "Game Time:\n" +
            "  Red  - 00:00:00\n" +
            "  Blue - 00:00:00\n" +
            "\n" +
            "RED-TIME=0\n" +
            "BLUE-TIME=0\n" +
            "\n" +
            "Red Amazons:\n" +
            "  A4 D1 G1 J4 \n" +
            "\n" +
            "Blue Amazons:\n" +
            "  A7 D10 G10 J7 \n" +
            "\n" +
            "Arrows:\n" +
            "\n" +
            "<GAME-INFO-END>\n" +
            "\n" +
            "<BOARD-SETUP-START>\n" +
            "\n" +
            "RED-AMAZONS=A4, D1, G1, J4\n" +
            "BLUE-AMAZONS=A7, D10, G10, J7\n" +
            "ARROWS=\n" +
            "COLUMNS=10\n" +
            "ROWS=10\n" +
            "\n" +
            "<BOARD-SETUP-END>\n" +
            "\n" +
            "<MOVE-HISTORY-START>\n" +
            "\n" +
            "Red/Blue\n";

    private static final String FILE_SUFFIX = "\n" +
            "<MOVE-HISTORY-END>";
    
    public static void writeLog(File destination, List<Move> moves){
        String data = FILE_PREFIX + translateMoves(moves) + FILE_SUFFIX;
        try(OutputStream os = new FileOutputStream(destination)){
            os.write(data.getBytes(), 0, data.length());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String translateMoves(List<Move> moves) {
        String data = "";
        int moveCount = 1;
        for(Move move: moves){
            String latestMove = "  ";
            String yStartTrans = "" + (10 - move.getQueenPos().getY());
            String xStartTrans = columnToLetter(move.getQueenPos().getY());

            String yMoveTrans = "" + (10 - move.getQueenMove().getY());
            String xMoveTrans = columnToLetter(move.getQueenMove().getY());

            String yArrowTrans = "" + (10 - move.getArrow().getY());
            String xArrowTrans = columnToLetter(move.getArrow().getY());

            latestMove += moveCount + ". ";
            latestMove += xStartTrans + yStartTrans + " - ";
            latestMove += xMoveTrans + yMoveTrans + " ";
            latestMove += "(" + xArrowTrans + yArrowTrans + ")";

            if(moveCount % 2 == 0){
                data += latestMove + "\n";
            }else{
                data += latestMove + "\t";
            }
            moveCount+=1;
        }
        return data;
    }

    private static String columnToLetter(int y) {
        String[] letters = new String[]{"A","B","C","D","E","F","G","H","I","J"};
        return letters[y];
    }

    private static int letterToColumn(char yChar) {
        if(yChar == 'A'){
            return 0;
        } else if(yChar == 'B'){
            return 1;
        } else if(yChar == 'C'){
            return 2;
        } else if(yChar == 'D'){
            return 3;
        } else if(yChar == 'E'){
            return 4;
        } else if(yChar == 'F'){
            return 5;
        } else if(yChar == 'G'){
            return 6;
        } else if(yChar == 'H'){
            return 7;
        } else if(yChar == 'I'){
            return 8;
        } else if(yChar == 'J') {
            return 9;
        } else{
            System.out.println("What the fuck is this: " + yChar);
            System.exit(1);
            return -1;
        }
    }

    public static Move getLastMove(File logFile) {
        try {
            FileReader fr = new FileReader(logFile);
            BufferedReader reader = new BufferedReader(fr);
            List<String> tmp = new ArrayList<String>();
            String ch = "";
            do {
                ch = reader.readLine();
                tmp.add(ch);
            } while (ch != null);
            for(int i=tmp.size()-1;i>=0;i--) {
                String line = tmp.get(i);
                if(line == null){
                    continue;
                }
                line = line.trim();
                if(line.contains(".")){
                    System.out.println("FOUND THE GOOD LINE: " + line);
                    int startIndex = line.lastIndexOf(".");
                    String moveStr = line.substring(startIndex + 1).trim();
                    System.out.println("movestr: " + moveStr);
                    StringCharacterIterator it = new StringCharacterIterator(moveStr);

                    int xMove = letterToColumn(it.current());
                    int yMove = Integer.parseInt(Character.toString(it.next()));

                    it.next(); //space
                    it.next(); //dash
                    it.next(); //space

                    int xQueen = letterToColumn(it.next());
                    int yQueen = Integer.parseInt(Character.toString(it.next()));

                    it.next(); //space
                    it.next(); //open bracket

                    int xArrow = letterToColumn(it.next());
                    int yArrow = Integer.parseInt(Character.toString(it.next()));

                    Move aiMove =  new Move(new Position(xQueen, yQueen), new Position(xMove, yMove), new Position(xArrow, yArrow), Team.BLACK);
                    System.out.println("Ai made the move " + aiMove);
                    return aiMove;
                }
            }
            System.out.println("Failed to find any move lines???");
            System.exit(1);
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }

    public static void main(String[] args){
        System.out.println(getLastMove(new File("./resources/tmp/x.txt")));
//        List<Move> test = new ArrayList<>();
//        test.add(new Move(new Position(0, 0), new Position(1, 1), new Position(5, 5), Team.WHITE));
//        test.add(new Move(new Position(0, 0), new Position(1, 1), new Position(5, 5), Team.WHITE));
//        test.add(new Move(new Position(0, 0), new Position(1, 1), new Position(5, 5), Team.WHITE));
//        test.add(new Move(new Position(0, 0), new Position(1, 1), new Position(5, 5), Team.WHITE));
//        System.out.println(FILE_PREFIX + translateMoves(test) + FILE_SUFFIX);
    }
}
