package state;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        this.pos = newPos;
    }

    @Override
    public String toString() {
        return "BoardPiece{" +
                "type=" + type +
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
