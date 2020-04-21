package state;

public class Move {
    private Position queenPos;
    private Position queenMove;
    private Position arrow;
    private Team team;

    public Move(Position queenPos, Position queenMove, Position arrow, Team team) {
        this.queenPos = queenPos;
        this.queenMove = queenMove;
        this.arrow = arrow;
        this.team = team;
    }

    public Position getQueenPos() {
        return queenPos;
    }

    public Position getQueenMove() {
        return queenMove;
    }

    public Position getArrow() {
        return arrow;
    }

    public Team getTeam() {
        return team;
    }

    @Override
    public String toString() {
        return "Move{" +
                "queenPos=" + queenPos +
                ", queenMove=" + queenMove +
                ", arrow=" + arrow +
                ", team=" + team +
                '}';
    }
}
