package model;

import helper.Debug;
import helper.FEN;
import model.magic.CompactMagicBitboard;

public class ModelMain {
    public static void initModel() {
        if(Debug.off("G1")) {
            Bitboard.initBitboardsByFEN(FEN.getCurrentFEN());
            BoardLookup.initBoardLookup();
            CompactMagicBitboard.init();
            MoveGeneration.generateLegalMoves();
        }
    }
}
