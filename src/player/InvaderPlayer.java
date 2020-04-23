package player;

import state.Board;
import state.Move;
import state.Team;
import util.InvaderGameLog;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Plays using the invader2.1 executable
 * Does some sketchy ass shit. All coordinates are based on the default size window in the upper left corner of a 1920x1080p screen
 */
public class InvaderPlayer extends AbstractPlayer {
    private static final int BUTTON_ROW_Y = 66;
    private static final int BUTTON_ROW_X_PAUSE = 42;

    private Robot invaderRobot;
    private Process invaderProc;
    private File logFileCopy = new File("./resources/tmp/x.txt");
    //private File logFileCopy = new File("./resources/tmp/copy.txt");
    public InvaderPlayer(Team team) {
        super(team);
        try {
            invaderRobot = new Robot();
            //invaderProc = Runtime.getRuntime().exec("./resources/invader.exe");
            Thread.sleep(1000);

            //invaderProc = Runtime.getRuntime().exec("cmd.exe /C start ./resources/invader.exe");
        } catch (AWTException | InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public Board play(Board board) {
        try {
            if(!board.getMoveLog().isEmpty()) {
                //logFileCopy.delete();
                InvaderGameLog.writeLog(logFileCopy, board.getMoveLog());
                //Files.move(Path.of(logFileCopy.getPath()), Path.of("./resources/tmp/x.txt"), StandardCopyOption.ATOMIC_MOVE);
            }

            invaderProc = Runtime.getRuntime().exec("./resources/invader.exe");
            Thread.sleep(1000);
            loadBoard();

            //Thread.sleep(1000);
            getMove();

            Thread.sleep(1000);
            saveBoard();
            Thread.sleep(1000);
            invaderProc.destroy();
            Thread.sleep(1000);

            //Files.move(Path.of("./resources/tmp/x.txt"), Path.of(logFileCopy.getPath()), StandardCopyOption.ATOMIC_MOVE);
            Move newMove = InvaderGameLog.getLastMove(logFileCopy);
            board.moveQueenAndFire(team, board.getQueenAt(newMove.getQueenPos()), newMove.getQueenMove(), newMove.getArrow());
        } catch(Exception e){
            e.printStackTrace();
            System.out.println("InvaderAI failed!");
            System.exit(1);
        }

        return board;
    }

    public void loadBoard() throws InterruptedException {
        //Press open
        click(BUTTON_ROW_X_PAUSE+49, BUTTON_ROW_Y);

        //Press browse
        click(471, 312);

        //Type in the filename
//        type(KeyEvent.VK_R);
//        type(KeyEvent.VK_E);
//        type(KeyEvent.VK_S);
//        type(KeyEvent.VK_O);
//        type(KeyEvent.VK_U);
//        type(KeyEvent.VK_R);
//        type(KeyEvent.VK_C);
//        type(KeyEvent.VK_E);
//        type(KeyEvent.VK_S);
//        type(KeyEvent.VK_SLASH);
//        type(KeyEvent.VK_T);
//        type(KeyEvent.VK_M);
//        type(KeyEvent.VK_P);
//        type(KeyEvent.VK_SLASH);
        Thread.sleep(200); //wait for the window to open
        type(KeyEvent.VK_X);
//        type(KeyEvent.VK_PERIOD);
//        type(KeyEvent.VK_T);
//        type(KeyEvent.VK_X);
//        type(KeyEvent.VK_T);

        //Press open
        type(KeyEvent.VK_ENTER);

        //Move back to load and hit enter
        type(KeyEvent.VK_LEFT);
        type(KeyEvent.VK_LEFT);
        type(KeyEvent.VK_ENTER);
        Thread.sleep(500); //wait for it to load

    }

    public void getMove() throws Exception{
        // unpause game
        click(BUTTON_ROW_X_PAUSE, BUTTON_ROW_Y);

        // ask AI for a move
        click(BUTTON_ROW_X_PAUSE+273, BUTTON_ROW_Y); //start compute
        Thread.sleep(1000); //varies depending on how long the AI takes to make a move, which is a mega pain in the ass
                                  // as the game ends the invader AI just stops, but we play til the bitter end
        click(BUTTON_ROW_X_PAUSE+247,BUTTON_ROW_Y); //force move
        Thread.sleep(500); //wait for animation

        // stop game
        click(BUTTON_ROW_X_PAUSE+20, BUTTON_ROW_Y);
    }
    public void saveBoard() throws Exception{
        //click save
        click(BUTTON_ROW_X_PAUSE+80, BUTTON_ROW_Y);

        Thread.sleep(200); //wait for the window to open

        //enter filename
        type(KeyEvent.VK_X);

        Thread.sleep(500);
        type(KeyEvent.VK_ENTER);

        type(KeyEvent.VK_LEFT);
        Thread.sleep(500);

        type(KeyEvent.VK_ENTER);
    }

    private void type(int key) {
        invaderRobot.keyPress(key);
        invaderRobot.keyRelease(key);
    }

    private void click(int x, int y){
        invaderRobot.mouseMove(x,y);
        invaderRobot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        invaderRobot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    public void cleanup(){
        invaderProc.destroy();
    }

    public static void main(String[] args) throws Exception{
        System.out.println("Starting");
        InvaderPlayer player = new InvaderPlayer(Team.WHITE);
//
        for(int i = 0; i < 10; i++) {
            Thread.sleep(1000);
            player.loadBoard();

            //Thread.sleep(1000);
            player.getMove();
            //System.out.println("Got move");

            Thread.sleep(1000);
            //System.out.println("Saving game");
            player.saveBoard();
        }

//        Thread.sleep(5000);  // some time for user to position mouse
//        Point spot = MouseInfo.getPointerInfo().getLocation();
//        System.out.println(
//                String.valueOf(spot.getX())
//                        +","+
//                        String.valueOf(spot.getY()));

//        Thread.sleep(1000);
//        System.out.println("Destroying");
//        player.cleanup();
    }
}
