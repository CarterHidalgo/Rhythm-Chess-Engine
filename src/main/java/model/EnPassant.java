package model;

import helper.Offset;

public class EnPassant {
    // only the EP bitboard should be updated here
    public static void updateWithMove(short move) {
        Bitboard.clearEnPassant();

        if(Move.isDoublePawnPush(move)) {            
            Bitboard.setEnPassant(Offset.behind(Move.getToIndex(move)));
        }
    }
}
