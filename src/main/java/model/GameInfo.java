package model;

public class GameInfo {
    private static boolean debug = true; // set to false to manually override all debug settings to off

    // Allowed Game States: [play, pause, promote, checkmate]
    private static final String[] allowedGameStates = {
        "play",
        "pause",
        "pre-promote",
        "post-promote",
        "checkmate",
    };
    private static String gameState = allowedGameStates[0];

    /*
     * DEFINE:
     *   turn: a play made by either white or black (flip flops true/false to indicate who's TURN it is to play)
     *   move: a move is a turn by white and black and will increment after a turn by black (int++)
     *   side: a side is what color the player plays as *for the entire game* (white = false; black = true)
     */
    private static short move = 0; // initially no moves have been played
    private static boolean turn = false; // initially white has the first turn
    private static boolean side = false; // initially player defaults to white
    private static boolean hasHuman = true; // indicates a human is playing using the GUI 
    
    // GUI consts
    private static final int BOARD_LENGTH = 800;
    private static final int SQUARE_LENGTH = 100;
    private static final int UI_LENGTH = 300;

    public static short getMove() {
        return move;
    }

    public static void incrementMove() {
        move++;
    }

    public static String getTurn() {
        return (turn) ? "black" : "white";
    }

    public static String getOppositeTurn() {
        return (turn) ? "white" : "black";
    }

    public static String getOpponent() {
        return (turn) ? "white" : "black";
    }

    public static void nextTurn() {
        turn = !turn;
    }
    
    public static void setSide(int newSide) {
        side = !side;
    }

    public static String getSide() {
        return (side) ? "black" : "white";
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

    public static void setGameState(String newGameState) {
        for(String s : allowedGameStates) {
            if(s.equals(newGameState)) {
                gameState = newGameState;
                return;
            }
        }

        System.out.println("Error in GameInfo.java -> setGameState(String newGameState): Attempted to set an invalid game state; shutting down.");
        System.exit(1);
    }

    public static String getGameState() {
        return gameState;
    }

    public static boolean stateIs(String s) {
        for(String state : allowedGameStates) {
            if(state.equals(s)) {
                return s.equals(gameState);    
            }
        }

        System.out.println("WARNING: \"" + s + "\" was not found in the allowed game states");
        return false;
    }

    public static boolean stateIsNot(String s) {
        for(String state : allowedGameStates) {
            if(state.equals(s)) {
                return !s.equals(gameState);    
            }
        }

        System.out.println("WARNING: \"" + s + "\" was not found in the allowed game states");
        return false;
    }

    public static boolean hasHuman() {
        return hasHuman;
    }
}
