package player;

public enum PlayerType {
    //Test(new TestPlayer());
    Test(new RandomPlayer());

    private AbstractPlayer player;
    PlayerType(AbstractPlayer p){
        this.player = p;
    }

    public AbstractPlayer getPlayer(){
        return this.player;
    }
}
