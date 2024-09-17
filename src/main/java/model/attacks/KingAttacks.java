package model.attacks;

import helper.Bit;
import helper.Coord;
import helper.Vec2;
import model.Bitboard;
import model.GameInfo;

public class KingAttacks {
    private static Vec2 kingIndex = new Vec2(
        Bit.getNextBitIndex(Bitboard.getBitboard("whiteKing")), 
        Bit.getNextBitIndex(Bitboard.getBitboard("blackKing"))
    );
    private static Vec2 kingCheck = new Vec2(0, 0);

    private static final long[] KING_ATTACKS = {
        0x302L, // 0
        0x705L, // 1
        0xe0aL, // 2
        0x1c14L, // 3
        0x3828L, // 4
        0x7050L, // 5
        0xe0a0L, // 6
        0xc040L, // 7

        0x30203L, // 8
        0x70507L, // 9
        0xe0a0eL, // 10
        0x1c141cL, // 11
        0x382838L, // 12
        0x705070L, // 13
        0xe0a0e0L, // 14
        0xc040c0L, // 15

        0x3020300L, // 16
        0x7050700L, // 17
        0xe0a0e00L, // 18
        0x1c141c00L, // 19
        0x38283800L, // 20
        0x70507000L, // 21
        0xe0a0e000L, // 22
        0xc040c000L, // 23

        0x302030000L, // 24
        0x705070000L, // 25
        0xe0a0e0000L, // 26
        0x1c141c0000L, // 27
        0x3828380000L, // 28
        0x7050700000L, // 29
        0xe0a0e00000L, // 30
        0xc040c00000L, // 31

        0x30203000000L, // 32
        0x70507000000L, // 33
        0xe0a0e000000L, // 34
        0x1c141c000000L, // 35
        0x382838000000L, // 36
        0x705070000000L, // 37
        0xe0a0e0000000L, // 38
        0xc040c0000000L, // 39

        0x3020300000000L, // 40
        0x7050700000000L, // 41
        0xe0a0e00000000L, // 42
        0x1c141c00000000L, // 43
        0x38283800000000L, // 44
        0x70507000000000L, // 45
        0xe0a0e000000000L, // 46
        0xc040c000000000L, // 47

        0x302030000000000L, // 48
        0x705070000000000L, // 49
        0xe0a0e0000000000L, // 50
        0x1c141c0000000000L, // 51
        0x3828380000000000L, // 52
        0x7050700000000000L, // 53
        0xe0a0e00000000000L, // 54
        0xc040c00000000000L, // 55

        0x203000000000000L, // 56
        0x507000000000000L, // 57
        0xa0e000000000000L, // 58
        0x141c000000000000L, // 59
        0x2838000000000000L, // 60
        0x5070000000000000L, // 61
        0xa0e0000000000000L, // 62
        0x40c0000000000000L, // 63
    };

    public static long getKingAttacks(int square) {
        if(square < 0 || square > 64) {
            square %= 64;
            System.out.println("WARNING: square index has been modulated in getKingAttacks() -> KingAttacks.java");
        }

        return KING_ATTACKS[square];
    }

    public static void setSelfKingIndex(int index) {
        if(GameInfo.getTurn().equals("white")) {
            kingIndex.setX(index);
        } else {
            kingIndex.setY(index);
        }
    }

    public static int getSelfKingIndex() {
        if(GameInfo.getTurn().equals("white")) {
            return kingIndex.getXAsInt();
        } else {
            return kingIndex.getYAsInt();
        }
    }

    public static void printKingIndex() {
        System.out.println("white: " + kingIndex.getXAsInt());
        System.out.println("black: " + kingIndex.getYAsInt());
    }

    public static void printKingCheck() {
        System.out.println("\nturn: " + GameInfo.getTurn());
        System.out.println("white: " + ((kingCheck.getXAsInt() == 0) ? "no check" : "check"));
        System.out.println("black: " + ((kingCheck.getYAsInt() == 0) ? "no check" : "check"));
    }

    public static void setCheck(String side) {
        if(side.equals("white")) {
            kingCheck.setX(1);
        } else {
            kingCheck.setY(1);
        }
    }

    public static void clearCheck(String side) {
        if(side.equals("white")) {
            kingCheck.setX(0);
        } else {
            kingCheck.setY(0);
        }
    }

    public static boolean isInCheck(String side) {
        return (side.equals("white")) ? ((kingCheck.getXAsInt() == 1) ? true : false) : ((kingCheck.getYAsInt() == 1) ? true : false);
    }

    public static void generateKingAttacks() {
        for(int square = 0; square < 64; square++) {
            long attack = 0;
            Coord start = new Coord(square);

            for(Coord dir : Coord.rookDirections) {
                Coord attackCoord = start.add(dir);

                if(attackCoord.isValid()) {
                    attack = Bit.setBit(attack, attackCoord.getIndex());
                }
            }

            for(Coord dir : Coord.bishopDirections) {
                Coord attackCoord = start.add(dir);

                if(attackCoord.isValid()) {
                    attack = Bit.setBit(attack, attackCoord.getIndex());
                }
            }

            if(square % 8 == 0) {
                System.out.println();
            }

            System.out.println("0x" + Long.toHexString(attack) + "L, // " + square);
        }
    }
}
