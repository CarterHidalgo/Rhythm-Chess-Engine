package model;

import helper.FEN;
import model.magic.MagicBitboard;

public class ModelMain {
    public static void initModel() {
        Bitboard.initBitboardsByFEN(FEN.getCurrentFEN());
        BoardLookup.initBoardLookup();
        MagicBitboard.init(false);
        
        MoveGeneration.generateLegalMoves();
    }
}
