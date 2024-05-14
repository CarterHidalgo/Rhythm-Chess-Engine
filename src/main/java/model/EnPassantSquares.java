package model;

import java.util.ArrayList;

public class EnPassantSquares {
    private static ArrayList<Integer> indices = new ArrayList<>();
    private static ArrayList<Integer> moves = new ArrayList<>();

    public static void add(Move move) {
        // if(move.getMovePlayer().equals("white")) {
        //     indices.add(move.getFromIndex() + 8);
        // } else {
        //     indices.add(move.getFromIndex() - 8);
        // }
        // moves.add(GameInfo.getMove());
    }

    public static boolean contains(Move move) {
    //     for(int i = 0; i < indices.size(); i++) {
    //         if(indices.get(i) == move.getToIndex()) {
    //             return Math.abs(GameInfo.getMove() - moves.get(i)) == 1;
    //         }
    //     }

        return false;
    }
}
