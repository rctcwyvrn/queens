package state;

import exception.InvalidStateException;
import ygraphs.ai.smart_fox.games.Queen;

import java.util.*;
import java.util.stream.Collectors;
public class Board {
    public static final boolean DEBUG = false;
    public static final int BOARD_WIDTH = 10;
    public static final int BOARD_HEIGHT = 10;
    public static Position CENTER;

    static {
        try {
            CENTER = new Position(Math.floorDiv(BOARD_WIDTH, 2), Math.floorDiv(BOARD_HEIGHT, 2));
        } catch (InvalidStateException e) {
            e.printStackTrace();
        }
    }

    private List<BoardPiece> pieces = new ArrayList<>();
    private List<Move> moveLog = new ArrayList<>();

    public Board(List<BoardPiece> pieces){
        this.pieces = pieces;
    }


    public List<BoardPiece> getPieces(Team team, BoardPiece.PieceType type){
        return pieces.stream()
                .filter(x -> x.getType() == type && x.getTeam().equals(team))
                .collect(Collectors.toList());
    }

    public List<Move> getMoveLog() {
        return moveLog;
    }

    public Move getLastMove(){
        if(moveLog.isEmpty()){
            return null;
        }
        return moveLog.get(moveLog.size()-1);
    }

    public List<BoardPiece> getQueensWithValidMoves(Team team){
        return getPieces(team, BoardPiece.PieceType.QUEEN).stream().filter(
                x -> !getValidMoves(x.getPos()).isEmpty())
                .collect(Collectors.toList());
    }

    // Only use this function to modify the board state. Must be called once per turn
    public void moveQueenAndFire(Team team, BoardPiece queen, Position move, Position arrow) throws InvalidStateException {
        if (!pieces.contains(queen)) {
            throw new InvalidStateException("Board does not contain the given queen");
        }

        if (!queen.getType().equals(BoardPiece.PieceType.QUEEN) && queen.getTeam().equals(team)) {
            throw new InvalidStateException("Given piece is an arrow and not a queen, or is on the wrong team");
        }

        if(getLastMove() != null && getLastMove().getTeam() != team.getOther()){
            throw new InvalidStateException("Other player didn't make a move last turn?");
        }

        Map<Position,List<Position>> movesAndArrows = getValidMoves(queen.getPos());
        Set<Position> validMoves = movesAndArrows.keySet();
        if (validMoves.contains(move)) {
            Position oldPos = queen.getPos();
            queen.moveTo(move);

            List<Position> validArrows = movesAndArrows.get(move);
            if(validArrows.contains(arrow)){
                pieces.add(new BoardPiece(BoardPiece.PieceType.ARROW, team, arrow));
                moveLog.add(new Move(oldPos, move, arrow, team));
                if(DEBUG) System.out.println(team + " has chosen the queen at " + oldPos + " and plans to move it to " + move + ", and to fire an arrow to " + arrow);
            } else {
                throw new InvalidStateException("Attempted to make an invalid shot: From " + queen.getPos() + " to " + arrow);
            }

        } else {
            throw new InvalidStateException("Attempted to make an invalid move " + queen.getPos() + " to " + move );
        }
    }

    public BoardPiece getQueenAt(Position pos){
        List<BoardPiece> xs =  pieces.stream().filter(x -> x.getPos().equals(pos)).collect(Collectors.toList());
        if(xs.size() > 1 || xs.isEmpty()){
            System.out.println("Failed to find a queen at position " + pos);
        }
        return xs.get(0);
    }

    public BoardPiece findQueenWithID(Integer id) {
        List<BoardPiece> queen = pieces.stream().filter(x -> x.getType().equals(BoardPiece.PieceType.QUEEN)).filter(x -> x.getQueenID() == id).collect(Collectors.toList());
        if(queen.size() != 1){
            System.out.println("Multiple or no queens with the given id = " + id);
            System.exit(1);
        }
        return queen.get(0);
    }

    public Map<Position,List<Position>> getValidMoves(Position startPosition){
        return getValidMoves(startPosition, true);
    }

    // Raw because it does not account for whatever move may have been made to the board before attempting to fire the arrow
    public List<Position> getValidArrowsRaw(Position startPosition){
        return new ArrayList<>(getValidMoves(startPosition, false).keySet()); //Drop all the "arrow" positions;
    }

    // Moves and checks for valid arrows for the given queen + move
    public List<Position> moveAndGetValidArrows(BoardPiece queen, Position move){
        Position oldPosition = queen.getPos();
        queen.moveTo(move);
        List<Position> arrows = getValidArrowsRaw(move);
        queen.moveTo(oldPosition);
        return arrows;
    }

    private Map<Position,List<Position>> getValidMoves(Position startPosition, boolean recurse){
        Map<Position,List<Position>>  validMoves = new HashMap<>();
        List<Position> allPositions = pieces.stream().map(x -> x.getPos()).collect(Collectors.toList());

        int x = startPosition.getX();
        int y = startPosition.getY();
        Position newPos;
        try {
            // RIGHT
            for (int i = x + 1; i < BOARD_WIDTH; i++) {
                newPos = new Position(i, y);
                // If there's something in the way then break out of the loop
                if (allPositions.contains(newPos)) {
                    break;
                }
                checkArrows(recurse, validMoves, newPos, startPosition);
            }

            // LEFT
            for (int i = x - 1; i >= 0; i--) {
                newPos = new Position(i, y);
                if (allPositions.contains(newPos)) {
                    break;
                }
                checkArrows(recurse, validMoves, newPos, startPosition);
            }

            // DOWN
            for (int i = y + 1; i < BOARD_HEIGHT; i++) {
                newPos = new Position(x, i);
                if (allPositions.contains(newPos)) {
                    break;
                }
                checkArrows(recurse, validMoves, newPos, startPosition);
            }

            // UP
            for (int i = y - 1; i >= 0; i--) {
                newPos = new Position(x, i);
                if (allPositions.contains(newPos)) {
                    break;
                }
                checkArrows(recurse, validMoves, newPos, startPosition);
            }

            // UP LEFT diagonal
            int tempX = x;
            int tempY = y;
            tempX -= 1;
            tempY -= 1;
            while (tempX >= 0 && tempY >= 0) {
                newPos = new Position(tempX, tempY);
                if (allPositions.contains(newPos)) {
                    break;
                }
                checkArrows(recurse, validMoves, newPos, startPosition);
                tempX -= 1;
                tempY -= 1;
            }

            // UP RIGHT diagonal
            tempX = x;
            tempY = y;
            tempX += 1;
            tempY -= 1;
            while (tempX < BOARD_WIDTH && tempY >= 0) {
                newPos = new Position(tempX, tempY);
                if (allPositions.contains(newPos)) {
                    break;
                }
                checkArrows(recurse, validMoves, newPos, startPosition);
                tempX += 1;
                tempY -= 1;
            }

            // DOWN LEFT diagonal
            tempX = x;
            tempY = y;
            tempX -= 1;
            tempY += 1;
            while (tempX >= 0 && tempY < BOARD_HEIGHT) {
                newPos = new Position(tempX, tempY);
                if (allPositions.contains(newPos)) {
                    break;
                }
                checkArrows(recurse, validMoves, newPos, startPosition);
                tempX -= 1;
                tempY += 1;
            }

            // DOWN RIGHT diagonal
            tempX = x;
            tempY = y;
            tempX += 1;
            tempY += 1;
            while (tempX < BOARD_WIDTH && tempY < BOARD_HEIGHT) {
                newPos = new Position(tempX, tempY);
                if (allPositions.contains(newPos)) {
                    break;
                }
                checkArrows(recurse, validMoves, newPos, startPosition);
                tempX += 1;
                tempY += 1;
            }
        }catch(InvalidStateException e){
            System.out.println("Something went terribly wrong while finding new moves " + e);
            e.printStackTrace();
            System.exit(1);
        }

        return validMoves;
    }

    //ugly af but i really don't want to keep having to copy paste this around
    /**
     * Checks if we have valid arrows and adds it to the list of validMoves if so
     * @param recurse
     * @param validMoves
     * @param newPos
     * @param startPosition
     */
    private void checkArrows(boolean recurse, Map<Position,List<Position>> validMoves, Position newPos, Position startPosition){
        // If we can't shoot an arrow after the move then it's not a valid move
        if(!recurse) {
            validMoves.put(newPos, null);
        }else{
            List<Position> arrows = moveAndGetValidArrows(getQueenAt(startPosition), newPos);
            if(!arrows.isEmpty()){
                validMoves.put(newPos, arrows);
            }
        }
    }
    public String displayBoard(){
        String[][] board = new String[BOARD_HEIGHT][];
        String[] display = new String[BOARD_WIDTH];
        for(int x = 0; x < BOARD_WIDTH; x++){
            display[x] = "  ";
        }
        for(int y = 0; y < BOARD_HEIGHT; y++){
            board[y] = display.clone();
        }

        for(BoardPiece piece: pieces){
            String pieceStr = piece.getType() == BoardPiece.PieceType.QUEEN ?
                piece.getTeam() == Team.WHITE ? "W " : "B "
                //piece.getQueenID() + " "
            : "x ";

            Position pos = piece.getPos();
            board[pos.getY()][pos.getX()] = pieceStr;
        }

        String boardLayout = "";
        String line = "\nx" + new String(new char[BOARD_WIDTH]).replace("\0", "--- ").trim() + "x";
        for (int y = 0; y < BOARD_HEIGHT; y++) {
            boardLayout += line + "\n";
            for (int x = 0; x < BOARD_WIDTH; x++) {
                boardLayout += "| ";
                boardLayout += board[y][x];
            }
            boardLayout += "|";
        }
        boardLayout += line;
        return boardLayout + "\n";
    }
}
