package state;

import ygraphs.ai.smart_fox.games.Queen;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
public class Board {
    private List<BoardPiece> pieces = new ArrayList<>();
    private List<Move> moveLog = new ArrayList<>();

    public Board(){
        pieces.add(new BoardPiece(BoardPiece.PieceType.QUEEN, Team.WHITE, Position.START_WHITE_1));
        pieces.add(new BoardPiece(BoardPiece.PieceType.QUEEN, Team.WHITE, Position.START_WHITE_2));
        pieces.add(new BoardPiece(BoardPiece.PieceType.QUEEN, Team.WHITE, Position.START_WHITE_3));
        pieces.add(new BoardPiece(BoardPiece.PieceType.QUEEN, Team.WHITE, Position.START_WHITE_4));

        pieces.add(new BoardPiece(BoardPiece.PieceType.QUEEN, Team.BLACK, Position.START_BLACK_1));
        pieces.add(new BoardPiece(BoardPiece.PieceType.QUEEN, Team.BLACK, Position.START_BLACK_2));
        pieces.add(new BoardPiece(BoardPiece.PieceType.QUEEN, Team.BLACK, Position.START_BLACK_3));
        pieces.add(new BoardPiece(BoardPiece.PieceType.QUEEN, Team.BLACK, Position.START_BLACK_4));
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
    public void moveQueenAndFire(Team team, BoardPiece queen, Position move, Position arrow) {
        if (!pieces.contains(queen)) {
            System.out.println("Board does not contain the given queen");
            System.exit(1);
        }

        if (!queen.getType().equals(BoardPiece.PieceType.QUEEN) && queen.getTeam().equals(team)) {
            System.out.println("Given piece is an arrow not a queen or is on the wrong team");
            System.exit(1);
        }

        List<AbstractMap.SimpleEntry<Position,List<Position>>> movesAndArrows = getValidMoves(queen.getPos());
        List<Position> validMoves = movesAndArrows.stream().map(x -> x.getKey()).collect(Collectors.toList());
        if (validMoves.contains(move)) {
            Position oldPos = queen.getPos();
            queen.moveTo(move);

            List<Position> validArrows = movesAndArrows.get(validMoves.indexOf(move)).getValue();
            if(validArrows.contains(arrow)){
                pieces.add(new BoardPiece(BoardPiece.PieceType.ARROW, team, arrow));
                moveLog.add(new Move(oldPos, move, arrow, team));
                //System.out.println(team + " has chosen the queen at " + oldPos + " and plans to move it to " + move + ", and to fire an arrow to " + arrow);
            } else {
                System.out.println("Attempted to make an invalid shot: From " + queen.getPos() + " to " + arrow );
                System.exit(1);
            }

        } else {
            System.out.println("Attempted to make an invalid move " + queen.getPos() + " to " + move );
            System.exit(1);
        }
    }

    public BoardPiece getQueenAt(Position pos){
        List<BoardPiece> xs =  pieces.stream().filter(x -> x.getPos().equals(pos)).collect(Collectors.toList());
        if(xs.size() > 1 || xs.isEmpty()){
            System.out.println("Failed to find a queen at position " + pos);
        }
        return xs.get(0);
    }

    public List<AbstractMap.SimpleEntry<Position,List<Position>>> getValidMoves(Position p){
        return getValidMoves(p, true);
    }

    // Raw because it does not account for whatever move may have been made to the board before attempting to fire the arrow
    public List<Position> getValidArrowsRaw(Position p){
        return getValidMoves(p, false).stream().map(x -> x.getKey()).collect(Collectors.toList()); //Drop all the "arrow" positions;
    }

    // Moves and checks for valid arrows for the given queen + move
    public List<Position> moveAndGetValidArrows(BoardPiece queen, Position move){
        Position oldPosition = queen.getPos();
        queen.moveTo(move);
        List<Position> arrows = getValidArrowsRaw(move);
        queen.moveTo(oldPosition);
        return arrows;
    }

    private List<AbstractMap.SimpleEntry<Position,List<Position>>> getValidMoves(Position startPosition, boolean recurse){
        List<AbstractMap.SimpleEntry<Position,List<Position>>>  validMoves = new ArrayList<>();
        List<Position> allPositions = pieces.stream().map(x -> x.getPos()).collect(Collectors.toList());

        int x = startPosition.getX();
        int y = startPosition.getY();
        Position newPos;

        // RIGHT
        for(int i = x + 1; i <= 9; i ++){
            newPos = new Position(i,y);
            // If there's something in the way then break out of the loop
            if(allPositions.contains(newPos)) {
                break;
            }
            checkArrows(recurse, validMoves, newPos, startPosition);
        }

        // LEFT
        for(int i = x - 1; i >= 0; i --){
            newPos = new Position(i,y);
            if(allPositions.contains(newPos)){
                break;
            }
            checkArrows(recurse, validMoves, newPos, startPosition);
        }

        // DOWN
        for(int i = y + 1; i <= 9; i ++){
            newPos = new Position(x,i);
            if(allPositions.contains(newPos)){
                break;
            }
            checkArrows(recurse, validMoves, newPos, startPosition);
        }

        // UP
        for(int i = y - 1; i >= 0; i --){
            newPos = new Position(x,i);
            if(allPositions.contains(newPos)){
                break;
            }
            checkArrows(recurse, validMoves, newPos, startPosition);
        }

        // UP LEFT diagonal
        int tempX = x;
        int tempY = y;
        tempX-=1;
        tempY-=1;
        while(tempX >= 0 && tempY >= 0){
            newPos = new Position(tempX,tempY);
            if(allPositions.contains(newPos)){
                break;
            }
            checkArrows(recurse, validMoves, newPos, startPosition);
            tempX-=1;
            tempY-=1;
        }

        // UP RIGHT diagonal
        tempX = x;
        tempY = y;
        tempX+=1;
        tempY-=1;
        while(tempX <= 9 && tempY >= 0){
            newPos = new Position(tempX,tempY);
            if(allPositions.contains(newPos)){
                break;
            }
            checkArrows(recurse, validMoves, newPos, startPosition);
            tempX+=1;
            tempY-=1;
        }

        // DOWN LEFT diagonal
        tempX = x;
        tempY = y;
        tempX-=1;
        tempY+=1;
        while(tempX >= 0 && tempY <= 9){
            newPos = new Position(tempX,tempY);
            if(allPositions.contains(newPos)){
                break;
            }
            checkArrows(recurse, validMoves, newPos, startPosition);
            tempX-=1;
            tempY+=1;
        }

        // DOWN RIGHT diagonal
        tempX = x;
        tempY = y;
        tempX+=1;
        tempY+=1;
        while(tempX <= 9 && tempY <= 9 ){
            newPos = new Position(tempX,tempY);
            if(allPositions.contains(newPos)){
                break;
            }
            checkArrows(recurse, validMoves, newPos, startPosition);
            tempX+=1;
            tempY+=1;
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
    private void checkArrows(boolean recurse, List<AbstractMap.SimpleEntry<Position,List<Position>>> validMoves, Position newPos, Position startPosition){
        // If we can't shoot an arrow after the move then it's not a valid move
        if(!recurse) {
            validMoves.add(new AbstractMap.SimpleEntry<>(newPos, null));
        }else{
            List<Position> arrows = moveAndGetValidArrows(getQueenAt(startPosition), newPos);
            if(!arrows.isEmpty()){
                validMoves.add(new AbstractMap.SimpleEntry<>(newPos, arrows));
            }
        }
    }
    public String displayBoard(){
        String[][] board = new String[10][];
        String[] display = new String[]{"  ","  ","  ","  ","  ","  ","  ","  ","  ","  "};
        for(int y = 0; y <= 9; y++){
            board[y] = display.clone();
        }

        for(BoardPiece piece: pieces){
            String pieceStr = piece.getType() == BoardPiece.PieceType.QUEEN ?
                piece.getTeam() == Team.WHITE ? "W " : "B "
            : "x ";

            Position pos = piece.getPos();
            board[pos.getX()][pos.getY()] = pieceStr;
        }

        String boardLayout = "";
        String line = "\nx--- --- --- --- --- --- --- --- --- ---x";
        for (int y = 0; y < 10; y++) {
            boardLayout += line + "\n";
            for (int x = 0; x < 10; x++) {
                boardLayout += "| ";
                boardLayout += board[x][y];
            }
            boardLayout += "|";
        }
        boardLayout += line;
        return boardLayout;
    }
}
