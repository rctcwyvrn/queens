package state;

import java.util.ArrayList;
import java.util.List;

public class BoardPiece {
    private final PieceType type;
    private final Team team;
    private Position pos;

    public enum PieceType {
        ARROW, QUEEN
    }

    public BoardPiece(PieceType type, Team team, Position pos){
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

    public void moveTo(Position newPos) {
        if(getValidMoves(this.pos).contains(newPos)) {
            this.pos = newPos;
        } else {
            System.out.println("attempted to make an invalid move " + this.pos + " to " + newPos);
        }
    }

    public static List<Position> getValidMoves(Position p){
        List<Position> validMoves = new ArrayList<>();
        int x = p.getX();
        int y = p.getY();

        for(int i = 0; i <= 9; i ++){
            validMoves.add(new Position(i,y));
        }

        for(int i = 0; i <= 9; i ++){
            validMoves.add(new Position(x,i));
        }

        // (x,y) gets added twice
        validMoves.remove(new Position(x,y));

        // UP LEFT diagonal
        int tempX = x;
        int tempY = y;
        tempX-=1;
        tempY-=1;
        while(tempX >= 0 && tempY >= 0){
            validMoves.add(new Position(tempX, tempY));
            tempX-=1;
            tempY-=1;
        }

        // UP RIGHT diagonal
        tempX = x;
        tempY = y;
        tempX+=1;
        tempY-=1;
        while(tempX <= 9 && tempY >= 0){
            validMoves.add(new Position(tempX, tempY));
            tempX+=1;
            tempY-=1;
        }

        // DOWN LEFT diagonal
        tempX = x;
        tempY = y;
        tempX-=1;
        tempY+=1;
        while(tempX >= 0 && tempY <= 9){
            validMoves.add(new Position(tempX, tempY));
            tempX-=1;
            tempY+=1;
        }

        // DOWN RIGHT diagonal
        tempX = x;
        tempY = y;
        tempX+=1;
        tempY+=1;
        while(tempX <= 9 && tempY <= 9 ){
            validMoves.add(new Position(tempX, tempY));
            tempX+=1;
            tempY+=1;
        }

        return validMoves;
    }
}
