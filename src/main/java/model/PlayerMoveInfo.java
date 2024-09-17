package model;

public class PlayerMoveInfo {
    /*
     * Player moves are not the same as algorithm moves since player moves are dependent on the GUI.
     * This class stores information about what move the player is *attempting* to make when they
     * click on a piece and then release it. This is not involved in MoveGeneration or Validation -
     * it only serves as storage when the player is making a GUI-based move on their turn only. 
     */
    private static String pieceSelected = "empty";
    private static byte fromIndex = -1;
    private static byte toIndex = -1;

    public static void setPieceSelected(String arg) {
        pieceSelected = arg;
    }

    public static String getPieceSelected() {
        return pieceSelected;
    }

    public static void setFromIndex(byte arg) {
        fromIndex = arg;
    }

    public static byte getFromIndex() {
        return fromIndex;
    }

    public static void setToIndex(byte arg) {
        toIndex = arg;
    }

    public static byte getToIndex() {
        return toIndex;
    }

    public static void print() {
        System.out.println("\npieceSelected: " + pieceSelected + 
        "\nfromIndex: " + String.valueOf(fromIndex) + 
        "\ntoIndex: " + String.valueOf(toIndex));
    }
}
