package model;

import helper.Bit;
import helper.Printer;
import helper.Debug;
import view.board.BoardLookupGraphic;
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

    public static short createMove(byte fromIndex, byte toIndex) {
        short move = (short) ((toIndex << 6) | fromIndex);

        byte flags = 0b0000;
        // byte fromIndexCode = BoardLookup.getByteCodeByBitIndex(fromIndex);
        byte toIndexCode = BoardLookup.getByteCodeByBitIndex(toIndex);

        boolean promotion = (toIndex > 55 && toIndex < 64) ? true : false;
        boolean capture = (Bit.getBit(toIndexCode, 3) == 1) ? true : false;

        if(promotion) {
            if(capture) {
                flags = Bit.setBit(flags, 2);
            }
            
            flags = Bit.setBitRange(flags, 0, (byte) (toIndexCode+2), 3);

            if(flags > 15) {
                // Absurd King-promo capture

                // TODO: figure out a way to handle the player trying to make this move
                // current solution is to set everything to 0 which is a1a1-0000 quiet move 
                // this should be detectable as an absurd move 
                flags = 0;
                move = 0;
            }
        } else {
            if(capture) {
                flags = Bit.setBit(flags, 2);

                // TODO: ep-capture
            }

            if(
                BoardLookup.getPieceByBitIndex(fromIndex).equals(GameInfo.getSideToPlay() + "Pawn") && 
                (toIndex - fromIndex) == 16 && 
                BoardLookup.getPieceByBitIndex((byte) (fromIndex + 8)) == "empty") {
                    flags = Bit.setBit(flags, 0);
            }


            // k-castle
            // q-castle
            // quiet
        }

        move = Bit.setBitRange(move, 12, flags, 3);

        return move;
    }

    /*
     * A simple move is a move where the last 4 high-order bits are left
     * unset. This should be used exclusively for player-made moves
     * where all we care about is fromIndex and toIndex. Flags can be
     * copied over from legal move list iff we find a match for the 
     * first 12
     */
    public static short createSimpleMove(byte fromIndex, byte toIndex) {
        return (short) ((toIndex << 6) | fromIndex);
    }

    public static void updateWithMove(short move) {
        /* 
         * A move has been offered in 1 of 2 situations
         *   0: the player selected a self piece and released it constituting a move
         *   1: the rhythm has decided on a move and wishes to push it through
         */

        // if promotion Promotion.initPromotion(move)

        Bitboard.updateWithMove(move);
        BoardLookup.updateWithMove(move);
        BoardOverlayGraphic.highlightMove(move);

        // if not promotion MoveRecord.pushMove(move); and GameInfo.nextTurn(); <- check to see turn vs move vs side etc
    }

    public static boolean isValid(short move) {
        return true;
        // return MoveValidation.consider(move);
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
        
        System.out.println("");
        System.out.println("fromIndex: " + fromIndex);
        System.out.println("toIndex: " + toIndex);
        System.out.println("flags: " + Bit.flagsToString(flags));
    }
}