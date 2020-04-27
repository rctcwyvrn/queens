package player;

import state.Board;
import state.BoardPiece;
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
import java.security.Key;
import java.util.List;

/**
 * Plays using the invader2.1 executable
 * Does some sketchy ass shit. All coordinates are based on the default size window in the upper left corner of a 1920x1080p screen
 * Currently can't play invader vs invader because I'm too lazy to make the system type in the team color to differentiate them
 */
public class InvaderPlayer extends AbstractPlayer {
    private static final int BUTTON_ROW_Y = 66;
    private static final int BUTTON_ROW_X_PAUSE = 42;

    private Robot invaderRobot;
    private Process invaderProc;
    private File logFileIn = new File("./resources/tmp/0.txt");
    private File logFileOut= new File("./resources/tmp/0i.txt");
    private int invaderPlays = 0;

    static{
        System.setProperty("java.awt.headless", "false"); // We're running it headless but we need awt
    }
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
        int tries = 0;
        boolean overwrite = false;
        while(true) {
            try {
                logFileIn = new File("./resources/tmp/" + invaderPlays + ".txt");
                logFileOut = new File("./resources/tmp/" + invaderPlays + "i.txt");

                System.out.println("Writing new queen and arrow positions to log file " + logFileIn.getPath());
                List<BoardPiece> arrows = board.getPieces(Team.WHITE, BoardPiece.PieceType.ARROW);
                arrows.addAll(board.getPieces(Team.BLACK, BoardPiece.PieceType.ARROW));
                InvaderGameLog.writeLog(team, logFileIn,
                        board.getMoveLog(),
                        board.getPieces(Team.WHITE, BoardPiece.PieceType.QUEEN),
                        board.getPieces(Team.BLACK, BoardPiece.PieceType.QUEEN),
                        arrows);

                invaderProc = Runtime.getRuntime().exec("./resources/invader.exe");
                Thread.sleep(1500);
                loadBoard();

                //Thread.sleep(1000);
                getMove();
                Thread.sleep(500);

                saveBoard(overwrite);
                Thread.sleep(1500);

                Move newMove = InvaderGameLog.getLastMove(logFileOut);
                board.moveQueenAndFire(team, board.getQueenAt(newMove.getQueenPos()), newMove.getQueenMove(), newMove.getArrow());
                invaderPlays+=1;
                return board;
            } catch (Exception e) {
                e.printStackTrace();
                tries+=1;
                overwrite = true;
                System.out.println("InvaderAI failed! Tries = " + tries);
                if(tries >= 3){
                    System.out.println("Exiting :c");
                    System.exit(1);
                }
            } finally{
                invaderProc.destroy();
            }
        }
    }

    public void loadBoard() throws InterruptedException {
        //Press open
        click(BUTTON_ROW_X_PAUSE+49, BUTTON_ROW_Y);

        //Press browse
        click(471, 312);

        Thread.sleep(200); // wait for the window

        //Type in the filename
        if(invaderPlays >= 10) {
            type((int) (KeyEvent.VK_0 + Math.ceil(invaderPlays/10)));
        }
        type(KeyEvent.VK_0 + (invaderPlays % 10));

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

    /**
     *
     * @param overwrite if we expect the file to already exist and will be overwriting it
     * @throws Exception
     */
    public void saveBoard(boolean overwrite) throws Exception{
        //click the "win" button just in case we maybe won!
        click(422, 294);
        Thread.sleep(200);

        //click save
        click(BUTTON_ROW_X_PAUSE+80, BUTTON_ROW_Y);

        Thread.sleep(200); //wait for the window to open

        //enter filename
        if(invaderPlays >= 10) {
            type((int) (KeyEvent.VK_0 + Math.ceil(invaderPlays/10)));
        }
        type(KeyEvent.VK_0 + (invaderPlays % 10));
        type(KeyEvent.VK_I);
        type(KeyEvent.VK_ENTER);

        if(overwrite) {
            type(KeyEvent.VK_LEFT);
            Thread.sleep(500);

            type(KeyEvent.VK_ENTER);
        }
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
        for(File f: new File("./resources/tmp").listFiles()){
            f.delete();
        }
    }

    // Helpful little thing to get the xy coordinates recalibrated if the window gets resized or moved or w/e
    public static void main(String[] args) throws Exception{
        System.out.println("Starting");

        Thread.sleep(5000);  // some time for user to position mouse
        Point spot = MouseInfo.getPointerInfo().getLocation();
        System.out.println(
                String.valueOf(spot.getX())
                        +","+
                        String.valueOf(spot.getY()));

        Thread.sleep(1000);
    }
}
