package state;

import java.util.ArrayList;
import java.util.Objects;

public class Position {

    public static Position START_BLACK_1 = new Position(0,3);
    public static Position START_BLACK_2 = new Position(3,0);
    public static Position START_BLACK_3 = new Position(6,0);
    public static Position START_BLACK_4 = new Position(9,3);

    public static Position START_WHITE_1 = new Position(0,6);
    public static Position START_WHITE_2 = new Position(3,9);
    public static Position START_WHITE_3 = new Position(6,9);
    public static Position START_WHITE_4 = new Position(9,6);

    private int x;
    private int y;

    public Position(int x, int y){
        if(x < 0 || x >= Board.BOARD_WIDTH || y < 0 || y >= Board.BOARD_HEIGHT){
            System.out.println("INVALID BOARD POSITION GIVEN: x = " + x + ", y = " + y);
            System.exit(-1);
        }
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public ArrayList<Integer> toListForRMC(){
        ArrayList<Integer> xs = new ArrayList<>();
        xs.add(y); // Row
        xs.add(x); // Column
        return xs;
    }
    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x &&
                y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public int dist(Position other) {
        int xDist = Math.abs(this.x - other.getX());
        int yDist = Math.abs(this.y - other.getY());
        return Math.max(xDist, yDist); //I'm pretty sure this is right
    }
}
