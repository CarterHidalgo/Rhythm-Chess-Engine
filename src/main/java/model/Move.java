package model;

public class Move {
    private String pieceSelected;
    private int fromIndex;
    private int toIndex;

    public Move(String pieceSelected, int fromIndex, int toIndex) {
        this.pieceSelected = pieceSelected;
        this.fromIndex = fromIndex;
        this.toIndex = toIndex;
    }

    public String getPiece() {
        return pieceSelected;
    }

    public int getFromIndex() {
        return fromIndex;
    }

    public int getToIndex() {
        return toIndex;
    }

    public boolean isLegal() {
        // Cannot place piece onto itself
        if(fromIndex == toIndex) {
            return false;
        }

        // Cannot place piece onto a friendly piece
        if((Bitboard.getBitboard((GameInfo.getTurn() == 0) ? "white" : "black") & (1L << toIndex)) != 0) {
            return false;
        }

        return true;
    }
}
