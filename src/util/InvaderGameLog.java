package util;

import state.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static state.Team.WHITE;

/**
 * Methods for loading the game log/parsing the move made by the AI and methods for generating a new log with a new move in it
 */
public class InvaderGameLog {
    private static final String FILE_PREFIX= "<GAME-INFO-START>\n" +
            "\n" +
            "Players:\n" +
            "  Red  - Computer (~3s)\n" +
            "  Blue - Human Player\n" +
            "\n" +
            "RED-TYPE=1:1:5:3:Computer\n" +
            "BLUE-TYPE=0:1:5:25000:Human Player\n" +
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
            "\n";
    private static final String RED_AMAZONS =
            "RED-AMAZONS="; //A4, D1, G1, J4\n" +
    private static final String BLUE_AMAZONS =
            "BLUE-AMAZONS=";//A7, D10, G10, J7\n";
    private static final String ARROWS =
            "ARROWS=";

    private static final String FILE_SUFFIX= "COLUMNS=10\n" +
            "ROWS=10\n" +
            "\n" +
            "<BOARD-SETUP-END>\n" +
            "\n" +
            "<MOVE-HISTORY-START>\n" +
            "<MOVE-HISTORY-END>";
    
    public static void writeLog(Team team, File destination, List<Move> moves, List<BoardPiece> redQueens, List<BoardPiece> blueQueens, List<BoardPiece> arrows){
        //String data = team.equals(Team.WHITE) ? FILE_PREFIX_1_WHITE: FILE_PREFIX_1_BLACK;
        String data = FILE_PREFIX;
        String aiQueens;
        String otherQueens;

        if(team.equals(WHITE)){
            aiQueens = String.join(", ", translatePositions(redQueens));
            otherQueens = String.join(", ", translatePositions(blueQueens));
        }else{
            aiQueens = String.join(", ", translatePositions(blueQueens));
            otherQueens = String.join(", ", translatePositions(redQueens));
        }

        // The AI can only be tricked into playing as red, so we gotta flip the board upside down just to that the AI gets to play as red
        data    += RED_AMAZONS + aiQueens + "\n"
                + BLUE_AMAZONS + otherQueens+ "\n";

        data += ARROWS + String.join(", ", translatePositions(arrows)) + "\n";
        //data += team.equals(Team.WHITE) ? FILE_SUFFIX_WHITE: FILE_SUFFIX_BLACK_1 + translateMoves(moves) + FILE_SUFFIX_BLACK_2;
        data += FILE_SUFFIX;

        try(OutputStream os = new FileOutputStream(destination)){
            os.write(data.getBytes(), 0, data.length());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> translatePositions(List<BoardPiece> queens) {
        List<String> out = new ArrayList<>();
        for(BoardPiece piece: queens){
            out.add(columnToLetter(piece.getPos().getX()) + (10 - piece.getPos().getY()));
        }
        return out;
    }

//    private static String translateMoves(List<Move> moves) {
//        String data = "";
//        int moveCount = 1;
//        for(Move move: moves){
//            String latestMove = "";
//            String yStartTrans = "" + (10 - move.getQueenPos().getY());
//            String xStartTrans = columnToLetter(move.getQueenPos().getX());
//
//            String yMoveTrans = "" + (10 - move.getQueenMove().getY());
//            String xMoveTrans = columnToLetter(move.getQueenMove().getX());
//
//            String yArrowTrans = "" + (10 - move.getArrow().getY());
//            String xArrowTrans = columnToLetter(move.getArrow().getX());
//
//            latestMove += moveCount + ". ";
//            latestMove += xStartTrans + yStartTrans + " - ";
//            latestMove += xMoveTrans + yMoveTrans + " ";
//            latestMove += "(" + xArrowTrans + yArrowTrans + ")";
//
//            if(moveCount % 2 == 0){
//                data += latestMove + "\n";
//            }else{
//                data += "  " + latestMove + "\t";
//            }
//            moveCount+=1;
//        }
//        return data + "\n";
//    }

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

    public static Move getLastMove(File logFile) throws Exception{
        FileReader fr = new FileReader(logFile);
        BufferedReader reader = new BufferedReader(fr);
        List<String> tmp = new ArrayList<>();
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
                int startIndex = line.lastIndexOf("."); //last index so we skip the dummy move when playing as black
                String moveStr = line.substring(startIndex + 1).trim();
                //System.out.println("movestr: " + moveStr);
                StringCharacterIterator it = new StringCharacterIterator(moveStr);

                Position queen = parsePosition(it, true);

                it.next(); //space
                it.next(); //dash
                it.next(); //space

                Position move = parsePosition(it, false);

                it.next(); //space
                it.next(); //open bracket

                Position arrow = parsePosition(it, false);
                Move aiMove =  new Move(queen, move, arrow, null);
                System.out.println("Ai made the move " + aiMove);
                reader.close();
                fr.close();
                return aiMove;
            }
        }

        System.out.println("Failed to find any move lines???");
        reader.close();
        fr.close();
        //System.exit(1);2i


        throw new Exception("fuk");
    }

    private static Position parsePosition(StringCharacterIterator it, boolean start){
        int x;
        if(start){
            x = letterToColumn(it.current());
        }else{
            x = letterToColumn(it.next());
        }
        int yCheck = Integer.parseInt(Character.toString(it.next()));
        if(yCheck == 1){
            if(it.next() == '0') {
                yCheck = 10;
            }else{
                it.previous();
            }
        }
        int y = 10 - yCheck;
        return new Position(x,y);
    }

    public static void main(String[] args) throws Exception{
        //System.out.println(getLastMove(new File("./resources/tmp/x.txt")));
        Board board = new Board();
        List<BoardPiece> arrows = board.getPieces(WHITE, BoardPiece.PieceType.ARROW);
        arrows.addAll(board.getPieces(Team.BLACK, BoardPiece.PieceType.ARROW));

        InvaderGameLog.writeLog(WHITE, new File("./resources/tmp/i_hate_myself.txt"),
                board.getMoveLog(),
                board.getPieces(WHITE, BoardPiece.PieceType.QUEEN),
                board.getPieces(Team.BLACK, BoardPiece.PieceType.QUEEN),
                arrows);
    }
}
