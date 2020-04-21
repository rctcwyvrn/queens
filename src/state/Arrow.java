package state;

//FIXME: can this be merged with queen somehow, they're basically the same -> board piece object with a field that tells it if its a queen or an arrow
public class Arrow {
    private Team team;
    public Arrow(Team team){
        this.team = team;
    }
}
