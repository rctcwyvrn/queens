import state.Position;

public class PositionDistTest {
    public static void main(String[] args) throws Exception{
        //tests are a pain in the butt
        Position origin = new Position(0, 0);

        Position p1 = new Position(0, 0);
        System.out.println(origin.dist(p1));

        Position p2 = new Position(5, 5);
        System.out.println(origin.dist(p2));

        Position p3 = new Position(2, 1);
        System.out.println(origin.dist(p3));

        Position p4 = new Position(1, 0);
        System.out.println(p3.dist(p4));
    }
}
