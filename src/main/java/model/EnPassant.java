package model;

import helper.Offset;

public class EnPassant {
    public static void updateWithMove(short move) {
        Bitboard.clearEnPassant();

        if(Move.isDoublePawnPush(move)) {            
            Bitboard.setEnPassant(Offset.behind(Move.getToIndex(move)));
        }
    }
}
