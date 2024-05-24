package model;

import helper.Bit;
import view.board.BoardOverlayGraphic;

public class Move {
    /*
     * bits (0-5): fromIndex
     * bits (6-11): toIndex
     * bits (12-15): flags
     * 
     * flag enumeration
     * _______________________________________________________________________
     * code	promotion	capture	special 1	special 0	kind of move
     * 0	0	        0	    0	        0	        quiet moves
     * 1	0	        0	    0	        1	        double pawn push
     * 2	0	        0	    1	        0	        king castle
     * 3	0	        0	    1	        1	        queen castle
     * 4	0	        1	    0	        0	        captures
     * 5	0	        1	    0	        1	        ep-capture
     * 8	1	        0	    0	        0	        knight-promotion
     * 9	1	        0	    0	        1	        bishop-promotion
     * 10	1	        0	    1	        0	        rook-promotion
     * 11	1	        0	    1	        1	        queen-promotion
     * 12	1	        1	    0	        0	        knight-promo capture
     * 13	1	        1	    0	        1	        bishop-promo capture
     * 14	1	        1	    1	        0	        rook-promo capture
     * 15	1	        1	    1	        1	        queen-promo capture
     * _______________________________________________________________________
     * 
     */

    public static short createSimpleMove(byte fromIndex, byte toIndex) {
        return (short) ((toIndex << 6) | fromIndex);
    }

    public static short createMove(byte fromIndex, byte toIndex, byte flags) {
        short move = (short) ((toIndex << 6) | fromIndex);

        move = Bit.setBitRange(move, 12, flags, 3);

        return move;
    }

    public static void updateWithMove(short move) {
        /* 
         * A move has been offered in 1 of 2 situations
         *   0: the player selected a self piece and released it constituting a move
         *   1: the algorithm has decided on a move and wishes to push it through to the board
         */

        // if promotion Promotion.initPromotion(move)

        Bitboard.updateWithMove(move);
        BoardLookup.updateWithMove(move);
        EnPassant.updateWithMove(move);
        BoardOverlayGraphic.highlightMove(move);
        
        if(GameInfo.getTurn() == "black") {
            GameInfo.incrementMove();
        }
        // GameInfo.nextTurn();

        MoveGeneration.generateLegalMoves();
    }

    public static short upgradeSimpleMove(short simpleMove) {
        return MoveGeneration.getMoveWithSimple(simpleMove);
    }

    public static boolean isLegalUpgradedMove(short upgradedMove) {
        return upgradedMove != 0;
    }

    public static boolean isLegalMove(short move) {
        return MoveGeneration.legalMoveListContainsMove(move);
    }

    public static boolean isCapture(short move) {
        return Bit.getBit(move, 14) == 1;
    }

    public static boolean isPromotion(short move) {
        return Bit.getBit(move, 15) == 1;
    }

    public static boolean isEnPassant(short move) {
        return Bit.getBitRange(move, 12, 15) == Bit.EP_CAPTURE;
    }

    public static boolean isDoublePawnPush(short move) {
        return Bit.getBitRange(move, 12, 15) == Bit.DOUBLE_PAWN;
    }

    public static boolean isCastle(short move) {
        return false;
    }

    public static byte getToIndex(short move) {
        return (byte) ((move >> 6) & 0b111111);
    }

    public static byte getFromIndex(short move) {
        return (byte) ((move & 0b111111));
    }

    public static void print(short move) {
        byte fromIndex = (byte) (move & 0b111111); 
        byte toIndex = (byte) ((move >> 6) & 0b111111);
        byte flags = (byte) ((move >> 12) & 0b1111);
        
        System.out.println(fromIndex + " | " + toIndex + " | " + Bit.flagsToString(flags));
    }
}