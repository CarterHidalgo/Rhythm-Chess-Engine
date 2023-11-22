package model;

public class Move {
    private String pieceSelected;
    private int fromIndex;
    private int toIndex;
    private int offset;
    private boolean capture = false;
    private boolean enPassant = false;
    private boolean promotion = false;
    private String player;

    public Move(String pieceSelected, int fromIndex, int toIndex, String player) {
        this.pieceSelected = pieceSelected;
        this.fromIndex = fromIndex;
        this.toIndex = toIndex;
        this.player = player;

        if(this.player == "white") {
            offset = this.toIndex - this.fromIndex;
        } else {
            offset = this.fromIndex - this.toIndex;
        }
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

    public boolean isValid() {
        return MoveValidation.consider(this);
    }

    public void setCapture() {
        capture = true;
    }

    public boolean isCapture() {
        return capture;
    }

    public void setEnPassant() {
        enPassant = true;
        capture = true;
    }

    public boolean isEnPassant() {
        return enPassant;
    }

    public void setPromotion() {
        promotion = true;
    }

    public boolean isPromotion() {
        return promotion;
    }

    public String getMovePlayer() {
        return player;
    }

    public int getOffset() {
        return offset;
    }
}
