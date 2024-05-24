package helper;

import model.GameInfo;
import model.Bitboard;

public class Offset {
    /*
     * Directions are relative to the side making the move. Forward is increasing rank for white
     * but decreasing for black (multiplier swaps sign)
     */
    
    public static int forward(int index) {
        int multiplier = (GameInfo.getTurn().equals("white")) ? 1 : -1;
        int result = index + (8 * multiplier);

        if(result < 0 || result > 63) {
            System.out.println("WARNING: \"forward\" square is outside the board in forward() -> Offset.java");
        }

        return result;
    }

    public static int doubleForward(int index) {
        int multiplier = (GameInfo.getTurn().equals("white")) ? 1 : -1;
        int result = index + (16 * multiplier);

        if(result < 0 || result > 63) {
            System.out.println("WARNING: \"double forward\" square is outside the board in doubleForward() -> Offset.java");
        }

        return result;
    }

    public static int leftForwardDiagonal(int index) {
        int multiplier = (GameInfo.getTurn().equals("white")) ? 1 : -1;
        int result = index + (7 * multiplier);

        if(result < 0 || result > 63) {
            System.out.println("WARNING: \"leftForwardDiagonal\" square is outside the board in leftForwardDiagonal() -> Offset.java");
        }

        return result;
    }

    public static int rightForwardDiagonal(int index) {
        int multiplier = (GameInfo.getTurn().equals("white")) ? 1 : -1;
        int result = index + (9 * multiplier);

        if(result < 0 || result > 63) {
            System.out.println("WARNING: \"rightForwardDiagonal\" square is outside the board in rightForwardDiagonal() -> Offset.java");
        }

        return result;
    }

    public static int behind(int index) {
        int multiplier = (GameInfo.getTurn().equals("white")) ? 1 : -1;
        int result = index - (8 * multiplier);

        if(result < 0 || result > 63) {
            System.out.println("WARNING: \"behind\" square is outside the board in behind() -> Offset.java");
        }

        return result;
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

    public static boolean isBorder(int index) {
        return Bit.isSet(Bitboard.getBitboard("border"), index);
    }
}
