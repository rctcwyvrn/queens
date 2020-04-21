package state;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Board {
    private List<BoardPiece> pieces = new ArrayList<>();
    
    public Board(){
        pieces.add(new BoardPiece(BoardPiece.PieceType.QUEEN, Team.WHITE, Position.START_WHITE_1));
        pieces.add(new BoardPiece(BoardPiece.PieceType.QUEEN, Team.WHITE, Position.START_WHITE_2));
        pieces.add(new BoardPiece(BoardPiece.PieceType.QUEEN, Team.WHITE, Position.START_WHITE_3));
        pieces.add(new BoardPiece(BoardPiece.PieceType.QUEEN, Team.WHITE, Position.START_WHITE_4));

        pieces.add(new BoardPiece(BoardPiece.PieceType.QUEEN, Team.BLACK, Position.START_BLACK_1));
        pieces.add(new BoardPiece(BoardPiece.PieceType.QUEEN, Team.BLACK, Position.START_BLACK_2));
        pieces.add(new BoardPiece(BoardPiece.PieceType.QUEEN, Team.BLACK, Position.START_BLACK_3));
        pieces.add(new BoardPiece(BoardPiece.PieceType.QUEEN, Team.BLACK, Position.START_BLACK_4));
    }


    public List<BoardPiece> getPieces(Team team, BoardPiece.PieceType type){
        return pieces.stream().filter(x -> x.getType() == type && x.getTeam().equals(team)).collect(Collectors.toList());
    }

    public void moveQueenAndFire(Team team, BoardPiece queen, Position move, Position arrow){
        if(!pieces.contains(queen)){
            System.out.println("Board does not contain the given queen");
            System.exit(1);
        }

        if(!queen.getType().equals(BoardPiece.PieceType.QUEEN) && queen.getTeam().equals(team)){
            System.out.println("Given piece is an arrow not a queen or is on the wrong team");
            System.exit(1);
        }

        //TODO: verify that no piece is in either position
        queen.moveTo(move);
        pieces.add(new BoardPiece(BoardPiece.PieceType.ARROW, team, arrow));
    }

    public String displayBoard(){
        //List<String> display = new ArrayList<>();
        String[] display = new String[]{"","","","","","","","","",""};
        for(int y = 0; y <= 9; y++){
            display[y] = ("oooooooooo");
        }

        for(BoardPiece piece: pieces){
            String pieceStr = piece.getType() == BoardPiece.PieceType.QUEEN ? "q" : "x";
            Position pos = piece.getPos();
            String row = display[pos.getY()];
            String newRow = row.substring(0, pos.getX()) + pieceStr + row.substring(pos.getX()+1);
            display[pos.getY()] = newRow;
        }

        // I am still mad that java doesn't have a clean native fold
        String output = "";
        for(String row: display){
            output += row + "\n";
        }
        return output;
    }
}
