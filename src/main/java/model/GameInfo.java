package model;

public class GameInfo {
    private static boolean debug = true; // set to false to manually override all debug settings to off

    // Allowed Game States: [play, pause, promote, checkmate]
    private static String gameState = "play";
    // private static String pieceSelected = "none";
    // private static int fromIndex = -1;
    // private static int toIndex = -1;

    private static short turn = 0;
    private static boolean move = false;
    private static boolean side = false; // The side you play as: white = false, black = true
    
    private static final int BOARD_LENGTH = 800;
    private static final int SQUARE_LENGTH = 100;
    private static final int UI_LENGTH = 300;

    public static int getTurn() {
        return turn;
    }
    
    public static void incrementTurn() {
        turn++;
    }

    public static void flipMove() {
        move = !move;
    }

    public static boolean getMove() {
        return move;
    }
    
    public static void setSide(int newSide) {
        side = !side;
    }

    public static boolean getSide() {
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

    public static String getGameState() {
        return gameState;
    }

    public static void setGameState(String newGameState) {
        gameState = newGameState;

        if(gameState != "play" && gameState != "pause" && gameState != "promote" && gameState != "checkmate") {
            System.out.println("Error in GameInfo.java -> setGameState(String newGameState): Attempted to set an invalid game state");
            System.exit(1);
        }
    }
}
