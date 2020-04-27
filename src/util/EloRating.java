package util;

/**
 * Chess elo rating just based on win/loss
 * Calculating with the actual board "score" is hard :c
 * Though I should really figure out how to do that
 */
public class EloRating {
    private static final double D = 400;
    private static final double K = 32;
    private double rating = 1500;

    public void setRating(double rating){
        if(rating > 0) {
            this.rating = rating;
        }else{
            this.rating = 0;
        }
    }

    public double getRating(){
        return rating;
    }

    @Override
    public String toString() {
        return "EloRating{" +
                "rating=" + rating +
                '}';
    }

    public static void updateRatings(EloRating winner, EloRating loser){
        // Player A = the winner
        // Player B = the loser
        double E_A = 1 / (1 + Math.pow(10, (loser.rating - winner.rating)/D));
        double E_B = 1 / (1 + Math.pow(10, (winner.rating - loser.rating)/D));
        double A_newRating = winner.rating + K * (1 - E_A);
        double B_newRating = loser.rating + K * (0 - E_B);
        winner.setRating(A_newRating);
        loser.setRating(B_newRating);
    }

    public static void main(String[] args){
        EloRating A = new EloRating();
        EloRating B = new EloRating();
        System.out.println(A + " | " + B);
        updateRatings(A,B);
        System.out.println(A + " | " + B);
        updateRatings(A,B);
        System.out.println(A + " | " + B);
        updateRatings(A,B);
        System.out.println(A + " | " + B);
    }
}
