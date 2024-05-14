package helper;

import model.Bitboard;

public class Printer {
    public static void print(String msg) {
        System.out.println(msg);
    }

    public static void printAllBitboards() {
        for(String currentKey : Bitboard.keys) {
            printBitboard(Bitboard.getBitboard(currentKey), "\n" + currentKey);
        }
    }

    public static void printAllBitboardsAsLines() {
        for(String currentKey : Bitboard.keys) {
            printBitboardAsLine(Bitboard.getBitboard(currentKey), "\n" + currentKey);
            System.out.println();
        }
    }

    public static void printBitboard(long bitboard, String description) {
        System.out.println("\n" + description);

        for(int i = 7; i >= 0; i--) {
            for(int j = 0; j < 8; j++) {
                int currentBit = (int) (bitboard >> (i*8+j) & 1);
                System.out.print(currentBit);
            }
            System.out.println();
        }
    }

    public static void printBitboard(String key, String description) {
        System.out.println("\n" + description);

        for(int i = 7; i >= 0; i--) {
            for(int j = 0; j < 8; j++) {
                int currentBit = (int) (Bitboard.getBitboard(key) >> (i*8+j) & 1);
                System.out.print(currentBit);
            }
            System.out.println();
        }
    }

    public static void printBitboardAsLine(long bitboard, String description) {
        System.out.println(description);

        String binaryString = String.format("%64s", Long.toBinaryString(bitboard)).replace(' ', '0');
        String reversedBinaryString = new StringBuilder(binaryString).reverse().toString();

        System.out.println(reversedBinaryString);
    }

    public static void printBinary(byte value) {
        System.out.println(String.format("%8s", Integer.toBinaryString(value & 0xFF)).replace(' ', '0'));
    }

    public static void printBinary(short value) {
        System.out.println(String.format("%16s", Integer.toBinaryString(value & 0xFFFF)).replace(' ', '0'));
    }
}
