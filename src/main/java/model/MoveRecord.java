package model;

import java.util.ArrayList;

public class MoveRecord {
    private static ArrayList<Short> moves = new ArrayList<>();

    public static void pushMove(short move) {        
        moves.add(move);
    }

    public static short peekMove() {
        return moves.get(moves.size() - 1);
    }

    public static short getMove(int index) {
        if(index < 0 || index >= moves.size()) {
            System.out.println("Attempting to remove a move at an invalid index; shutting donw");
            System.exit(1);
        }

        return moves.get(index);
    }

    public static void replaceMove(short move) {
        moves.remove(moves.size() - 1);
        moves.add(move);
    }

    public static void print() {
        System.out.println("\n >> Move Record");
        for(Short s : moves) {
            Move.print(s);
        }
        System.out.println(" >> ");
    }
}
