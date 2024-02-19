package model;

import java.util.ArrayList;

import helper.Debug;

public class MoveRecord {
    private static ArrayList<Move> moves = new ArrayList<>();

    public static void pushMove(Move move) {
        if(Debug.on("B7")) {
            System.out.println(move);
        }
        
        moves.add(move);
    }

    public static Move peekMove() {
        return moves.get(moves.size() - 1);
    }
}
