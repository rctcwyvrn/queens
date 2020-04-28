package state;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BoardPiece {
    private static int queensMade = 0;
    private int queenID = -1;
    private final PieceType type;
    private final Team team;
    private Position pos;

    public enum PieceType {
        ARROW, QUEEN
    }

    public static Board createBoard(){
        List<BoardPiece> pieces = new ArrayList<>();
        pieces.add(new BoardPiece(BoardPiece.PieceType.QUEEN, Team.WHITE, Position.START_WHITE_1, 1));
        pieces.add(new BoardPiece(BoardPiece.PieceType.QUEEN, Team.WHITE, Position.START_WHITE_2, 2));
        pieces.add(new BoardPiece(BoardPiece.PieceType.QUEEN, Team.WHITE, Position.START_WHITE_3, 3));
        pieces.add(new BoardPiece(BoardPiece.PieceType.QUEEN, Team.WHITE, Position.START_WHITE_4, 4));

        pieces.add(new BoardPiece(BoardPiece.PieceType.QUEEN, Team.BLACK, Position.START_BLACK_1, 5));
        pieces.add(new BoardPiece(BoardPiece.PieceType.QUEEN, Team.BLACK, Position.START_BLACK_2, 6));
        pieces.add(new BoardPiece(BoardPiece.PieceType.QUEEN, Team.BLACK, Position.START_BLACK_3, 7));
        pieces.add(new BoardPiece(BoardPiece.PieceType.QUEEN, Team.BLACK, Position.START_BLACK_4, 8));
        return new Board(pieces);
    }

    private BoardPiece(PieceType type, Team team, Position pos, int id){
        if(!type.equals(PieceType.QUEEN)){
            System.out.println("Wrong constructor! Use the one for arrows!");
            System.exit(1);
        }
        this.type = type;
        this.team = team;
        this.pos = pos;
        this.queenID = id;
    }
    public BoardPiece(PieceType type, Team team, Position pos){
        if(type.equals(PieceType.QUEEN)) {
            System.out.println("Wrong constructor! Use the one for queens!");
            System.exit(1);
        }
        this.type = type;
        this.team = team;
        this.pos = pos;
    }

    public PieceType getType() {
        return type;
    }

    public Position getPos() {
        return pos;
    }

    public Team getTeam() {
        return team;
    }

    public int getQueenID() {
        if(type.equals(PieceType.QUEEN)) {
            return queenID;
        }else{
            System.out.println("Tried to get the queen id for an arrow :C");
            System.exit(1);
            return -1;
        }
    }

    public void moveTo(Position newPos) {
        this.pos = newPos;
    }

    /**
     * Used only when starting new games and making new boards;
     */
    public static void resetQueenCount(){
        queensMade = 0;
    }

    @Override
    public String toString() {
        return "BoardPiece{" +
                "queenID=" + queenID +
                ", type=" + type +
                ", team=" + team +
                ", pos=" + pos +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardPiece that = (BoardPiece) o;
        return type == that.type &&
                team == that.team &&
                pos.equals(that.pos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, team, pos);
    }
}
