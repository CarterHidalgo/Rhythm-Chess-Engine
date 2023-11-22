package model;

public class GameInfo {
    private static boolean debug = true;

    private static String pieceSelected = "none";
    private static int fromIndex = -1;
    private static int toIndex = -1;

    private static int turn = 0;
    private static int move = 0;
    private static int side = 0;

    private static final int BOARD_LENGTH = 800;
    private static final int SQUARE_LENGTH = 100;
    private static final int UI_LENGTH = 300;

    public static void setTurn(int newTurn) {
        turn = newTurn;
    }

    public static int getTurn() {
        return turn;
    }

    public static void nextTurn() {
        turn ^= 1;
        addMove();
    }

    public static void setSide(int newSide) {
        side = newSide;
    }

    public static int getSide() {
        return side;
    }

    public static boolean debug() {
        return debug;
    }

    public static void setDebug(boolean newDebug) {
        debug = newDebug;
    }

    public static int getBoardLength() {
        return BOARD_LENGTH;
    }

    public static int getSquareLength() {
        return SQUARE_LENGTH;
    }

    public static int getUILength() {
        return UI_LENGTH;
    }

    public static void setPieceSelected(String arg) {
        pieceSelected = arg;
    }

    public static String getPieceSelected() {
        return pieceSelected;
    }

    public static void setFromIndex(int arg) {
        fromIndex = arg;
    }

    public static int getFromIndex() {
        return fromIndex;
    }

    public static void setToIndex(int arg) {
        toIndex = arg;
    }

    public static int getToIndex() {
        return toIndex;
    }

    public static void addMove() {
        move++;
    }

    public static int getMove() {
        return move;
    }

    public static String getSideToPlay() {
        if(turn == 0) {
            return "white";
        } else {
            return "black";
        }
    }

    public static String getSideToWait() {
        if(turn == 0) {
            return "black";
        } else {
            return "white";
        }
    }
}
