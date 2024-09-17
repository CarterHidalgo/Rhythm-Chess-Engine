package helper;

import model.Bitboard;
import model.GameInfo;

public class Offset {
    /*
     * Directions are relative to the side making the move. Forward is increasing rank for white
     * but decreasing for black (multiplier swaps sign)
     */
    
    public static int forward(int index) {
        int multiplier = (GameInfo.getTurn().equals("white")) ? 1 : -1;
        return index + (8 * multiplier);
    }

    public static int doubleForward(int index) {
        int multiplier = (GameInfo.getTurn().equals("white")) ? 1 : -1;
        return index + (16 * multiplier);
    }

    public static int leftForwardDiagonal(int index) {
        int multiplier = (GameInfo.getTurn().equals("white")) ? 1 : -1;
        return index + (7 * multiplier);
    }

    public static int rightForwardDiagonal(int index) {
        int multiplier = (GameInfo.getOppositeTurn().equals("black")) ? 1 : -1;
        return index + (9 * multiplier);
    }

    public static int opponentLeftForwardDiagonal(int index) {
        int multiplier = (GameInfo.getOppositeTurn().equals("white")) ? 1 : -1;
        return index + (7 * multiplier);
    }

    public static int opponentRightForwardDiagonal(int index) {
        int multiplier = (GameInfo.getOppositeTurn().equals("white")) ? 1 : -1;
        return index + (9 * multiplier);
    }

    public static int behind(int index) {
        int multiplier = (GameInfo.getTurn().equals("white")) ? 1 : -1;
        return index - (8 * multiplier);
    }

    public static boolean isFileA(int index) {
        return Bit.isSet(Bitboard.getBitboard("aFile"), index);
    }

    public static boolean isNotFileA(int index) {
        return !Bit.isSet(Bitboard.getBitboard("aFile"), index);
    }

    public static boolean isFileH(int index) {
        return Bit.isSet(Bitboard.getBitboard("hFile"), index);
    }

    public static boolean isNotFileH(int index) {
        return !Bit.isSet(Bitboard.getBitboard("hFile"), index);
    }

    public static boolean isNotRelativeLeftEdge(int index) {
        if(GameInfo.getTurn().equals("white")) {
            return !Bit.isSet(Bitboard.getBitboard("aFile"), index);
        } else {
            return !Bit.isSet(Bitboard.getBitboard("hFile"), index);
        }
    }

    public static boolean isNotRelativeRightEdge(int index) {
        if(GameInfo.getTurn().equals("white")) {
            return !Bit.isSet(Bitboard.getBitboard("hFile"), index);
        } else {
            return !Bit.isSet(Bitboard.getBitboard("aFile"), index);
        }
    }

    public static boolean opponentIsNotRelativeLeftEdge(int index) {
        if(GameInfo.getOppositeTurn().equals("white")) {
            return !Bit.isSet(Bitboard.getBitboard("aFile"), index);
        } else {
            return !Bit.isSet(Bitboard.getBitboard("hFile"), index);
        }
    }

    public static boolean opponentIsNotRelativeRightEdge(int index) {
        if(GameInfo.getOppositeTurn().equals("white")) {
            return !Bit.isSet(Bitboard.getBitboard("hFile"), index);
        } else {
            return !Bit.isSet(Bitboard.getBitboard("aFile"), index);
        }
    }

    public static boolean isBorder(int index) {
        return Bit.isSet(Bitboard.getBitboard("border"), index);
    }

    public static boolean isRelativePromotion(int index) {
        return Bit.isSet(Bitboard.getBitboard(GameInfo.getTurn() + "Promotion"), index);
    }

    public static int getSquareRelationship(int square1, int square2) {
        int rank1 = square1 / 8;
        int file1 = square1 % 8;
        int rank2 = square2 / 8;
        int file2 = square2 % 8;

        if (rank1 == rank2) {
            return 0; // Same rank
        } else if (file1 == file2) {
            return 1; // Same file
        }else if ((rank1 - file1) == (rank2 - file2)) {
            return 2; // Same major diagonal
        }else if ((rank1 + file1) == (rank2 + file2)) {
            return 3; // Same minor diagonal
        } else {
            return -1;
        }
    }
}
