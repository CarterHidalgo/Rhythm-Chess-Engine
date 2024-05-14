package model;

import helper.Convert;

public class MoveValidation {


    public static boolean legalListContains(short move) {
        return true;
    }

    public static boolean consider(short move) {
        // if(Move.getToIndex(move) < 0 || Move.getToIndex(move) > 63) {
        //     return false;
        // }

        // if(Move.getFromIndex(move) == Move.getToIndex(move)) {
        //     return false;
        // }

        // if(Convert.bitboardIsSet(move.getMovePlayer(), move.getToIndex())) {
        //     return false;
        // }

        // switch(move.getPiece()) {
        //     case "whitePawn":
        //     case "blackPawn":
        //         return validatePawn(move);
        //     case "whiteKnight":
        //     case "blackKnight":
        //         return validateKnight(move);
        //     case "whiteBishop":
        //     case "blackBishop":
        //         return validateBishop(move);
        //     case "whiteRook":
        //     case "blackRook":
        //         return validateRook(move);
        //     case "whiteQueen":
        //     case "blackQueen":
        //         return validateQueen(move);
        //     case "whiteKing":
        //     case "blackKing":
        //         return validateKing(move);
        // }

        // System.out.println("Error in MoveValidation.java -> consider(Move move): Should have returned a value");
        // System.exit(1);

        return false;
    }

    private static boolean validatePawn(Move move) {
        return true;

        // if(move.getOffset() == 7 || move.getOffset() == 9) {
        //     if(Convert.bitboardIsSet(GameInfo.getSideToWait(), move.getToIndex())) {
        //         move.setCapture();

        //         if(Convert.bitboardIsSet(move.getMovePlayer() + "Promotion", move.getToIndex())) {
        //             move.setPromotion();
        //         }

        //         return true;
        //     }

        //     if(EnPassantSquares.contains(move)) {
        //         move.setEnPassant();
        //         return true;
        //     }

        //     return false;
        // } else if(move.getOffset() == 8) {
        //     if(Convert.bitboardIsSet("occupied", move.getToIndex())) {
        //         return false;
        //     }

        //     if(Convert.bitboardIsSet(move.getMovePlayer() + "Promotion", move.getToIndex())) {
        //         move.setPromotion();
        //     }

        //     return true;
        // } else if(move.getOffset() == 16) {
        //     if(Convert.bitboardIsSet(move.getMovePlayer() + "PawnStart", move.getFromIndex()) &&
        //     !Convert.bitboardIsSet("occupied", Convert.bitIndexShiftBySide(move.getMovePlayer(), move.getFromIndex(), 8)) &&
        //     !Convert.bitboardIsSet("occupied", Convert.bitIndexShiftBySide(move.getMovePlayer(), move.getFromIndex(), 16))) {
        //         EnPassantSquares.add(move);
        //         return true;
        //     }

        //     return false;
        // }

        // return false;
    }

    private static boolean validateKnight(Move move) {
        return true;

        // Long offeredMoveMask = 1L << move.getToIndex();

        // if((KnightAttacks.getKnightAttack(move.getFromIndex()) & offeredMoveMask) != 0) {
        //     if((offeredMoveMask & Bitboard.getBitboard(GameInfo.getSideToWait())) != 0) {
        //         move.setCapture();
        //     }

        //     return true;
        // } else {
        //     return false;
        // }
    }

    private static boolean validateBishop(Move move) {
        
        return true;
    }

    private static boolean validateRook(Move move) {

        return true;
    }

    private static boolean validateQueen(Move move) {

        return true;
    }

    private static boolean validateKing(Move move) {

        return true;
    }
}
