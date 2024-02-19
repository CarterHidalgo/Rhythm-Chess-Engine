package model;

public class Move {
    private String pieceSelected;
    private int fromIndex;
    private int toIndex;
    private int offset;
    private boolean capture = false;
    private boolean enPassant = false;
    private boolean promotion = false;
    private String promotionSelected = "none";
    private String pieceCaptured = "none";
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

    public void setPieceCaptured(String set) {
        pieceCaptured = set;
    }

    public String getPieceCaptured() {
        return pieceCaptured;
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

    public String getPromotionSelected() {
        return promotionSelected;
    }

    public void setPromotionSelected(int section) {
        switch(section) {
            case 0:
                promotionSelected = "Queen";
            break;
            case 1:
                promotionSelected = "Knight";
            break;
            case 2: 
                promotionSelected = "Rook";
            break;
            case 3: 
                promotionSelected = "Bishop";
            break;
            case -1:
                promotionSelected = "undo";
            break;
            default:
                System.out.println("Error in Move.java -> setPromotionSelected(int section): Attempting to set promotionSelected to " + section);
                System.exit(1);
        }
    }

    public String getMovePlayer() {
        return player;
    }

    public int getOffset() {
        return offset;
    }

    public String toString() {
        String s = "\n[Move]" + 
        "\nPlayer: " + player +
        "\nPieceSelected: " + pieceSelected + 
        "\nFromIndex: " + fromIndex +
        "\nToIndex: " + toIndex +
        "\nCapture: " + capture;

        if(capture) {
            s += ", " + pieceCaptured;
        }

        s += "\nEnPassant: " + enPassant +
        "\nPromotion: " + promotion;

        if(promotion) {
            s += ", " + promotionSelected;
        }

        return s;
    }
}
