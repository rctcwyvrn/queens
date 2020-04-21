package state;

public enum Team {
    BLACK, WHITE;

    public Team getOther(){
        if(this.equals(WHITE)){
            return BLACK;
        }else{
            return WHITE;
        }
    }
}
