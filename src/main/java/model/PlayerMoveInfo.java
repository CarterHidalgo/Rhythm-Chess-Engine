package model;

public class PlayerMoveInfo {
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
}
