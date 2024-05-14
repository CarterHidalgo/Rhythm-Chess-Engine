package helper;

public class Bit {
    /*
     * Bit information:
     *  BoardLookup codes are stored with even bitIndex -> low order 4 bits
     *  and odd bitIndex -> high order 4 bits. Note Java prints with the highest
     *  order bit first, thus in the starting position output is as shown below:
     * 
     *          whiteRook   whiteKnight
     * example  1234        5678
     * output   56781234
     */

    // BoardLookup piece enumerations
    public static final byte EMPTY = 0b0000; // empty
    public static final byte WHITE_PAWN = 0b0001; // white
    public static final byte WHITE_KNIGHT = 0b0010;
    public static final byte WHITE_BISHOP = 0b0011;
    public static final byte WHITE_ROOK = 0b0100;
    public static final byte WHITE_QUEEN = 0b0101;
    public static final byte WHITE_KING = 0b0110;
    public static final byte BLACK_PAWN = 0b1001; // black
    public static final byte BLACK_KNIGHT = 0b1010;
    public static final byte BLACK_BISHOP = 0b1011;
    public static final byte BLACK_ROOK = 0b1100;
    public static final byte BLACK_QUEEN = 0b1101;
    public static final byte BLACK_KING = 0b1110;


    // Move flag enumerations
    public static final byte QUIET = 0b0000;
    public static final byte DOUBLE_PAWN = 0b0001;
    public static final byte KING_CASTLE = 0b0010;
    public static final byte QUEEN_CASTLE = 0b0011;
    public static final byte CAPTURE = 0b0100;
    public static final byte EP_CAPTURE = 0b0101;
    public static final byte KNIGHT_PROMO = 0b1000;
    public static final byte BISHOP_PROMO = 0b1001;
    public static final byte ROOK_PROMO = 0b1010;
    public static final byte QUEEN_PROMO = 0b1011;
    public static final byte KNIGHT_PROMO_CAPTURE = 0b1100;
    public static final byte BISHOP_PROMO_CAPTURE = 0b1101;
    public static final byte ROOK_PROMO_CAPTURE = 0b1110;
    public static final byte QUEEN_PROMO_CAPTURE = 0b1111;
    
    public static byte getBit(byte value, int index) {
        return (byte) ((value >> index) & 1);
    }

    public static byte getBit(short value, int index) {
        return (byte) ((value >> index) & 1);
    }

    public static byte getBit(int value, int index) {
        return (byte) ((value >> index) & 1);
    }

    public static byte getBit(long value, int index) {
        return (byte) ((value >> index) & 1);
    }

    public static byte getBitRange(byte value, int startIndex, int endIndex) {
        startIndex = Math.max(0, Math.min(8, startIndex));
        endIndex = Math.max(0, Math.min(8, endIndex));

        if(startIndex > endIndex) {
            int temp = startIndex;
            startIndex = endIndex;
            endIndex = temp;
        }

        byte shifted = (byte) (value >> startIndex);
        byte mask = (byte) ((1 << (endIndex - startIndex + 1)) - 1);
        byte result = (byte) ((shifted & mask) & 0xFF);

        // System.out.println("given: " + Integer.toBinaryString(value & 0xFF));
        // System.out.println("range: [" + startIndex + ", " + endIndex + "]");
        // System.out.println("found: " + Integer.toBinaryString(result) + " or " + result);

        return result;
    }

    public static short getBitRange(short value, int startIndex, int endIndex) {
        startIndex = Math.max(0, Math.min(15, startIndex));
        endIndex = Math.max(0, Math.min(15, endIndex));

        if(startIndex > endIndex) {
            int temp = startIndex;
            startIndex = endIndex;
            endIndex = temp;
        }

        short mask = (short) (((1 << (endIndex - startIndex + 1)) - 1) << startIndex);
        short result = (short) ((value & mask) >>> startIndex);

        return result;
    }

    public static byte setBit(byte value, int index) {
        byte mask = (byte) (1 << index);

        return (byte) (value | mask);
    }
    
    public static short setBit(short value, int index) {
        short mask = (short) (1 << index);
        
        return (short) (value | mask);
    }
    
    public static int setBit(int value, int index) {
        int mask = 1 << index;
        
        return value | mask;
    }

    public static long setBit(long value, int index) {
        long mask = 1L << index;
        
        return value | mask;
    }

    public static byte clearBit(byte value, int index) {
        byte mask = (byte) (1 << index);

        return (byte) (value & ~mask);
    }

    public static short clearBit(short value, int index) {
        short mask = (short) (1 << index);

        return (short) (value & ~mask);
    }

    public static int clearBit(int value, int index) {
        int mask = 1 << index;

        return value & ~mask;
    }

    public static long clearBit(long value, int index) {
        long mask = 1L << index;

        return value & ~mask;
    }

    public static byte setBitRange(byte value, int index, byte code, int codeLength) {
        if(index > 7) {
            System.out.println("WARNING: index has been modulated to fit");
        }
        index %= 8;

        if(codeLength + index > 7) {
            System.out.println("WARNING: mask has been pushed off the left side in byte setBitRange -> Bit.java");
        }
        byte setMask = (byte) ((1 << (codeLength + 1)) - 1);
        setMask <<= index;
        value &= ~setMask;
        code <<= index;
        value |= code;

        return value;
    }

    public static short setBitRange(short value, int index, short code, int codeLength) {
        if(index > 15) {
            System.out.println("WARNING: index has been modulated to fit");
        }
        index %= 16;

        if(codeLength + index > 15) {
            System.out.println("WARNING: mask has been pushed off the left side in short setBitRange -> Bit.java");
        }
        short setMask = (short) ((1 << (codeLength + 1)) - 1);
        setMask <<= index;
        value &= ~setMask;
        code <<= index;
        value |= code;

        return value;
    }

    // int and long setBitRange methods will be created on an as-needed basis

    public static String flagsToString(byte flags) {
        switch(flags) {
            case QUIET: return "quiet";
            case DOUBLE_PAWN: return "double-pawn";
            case KING_CASTLE: return "king-castle";
            case QUEEN_CASTLE: return "queen-castle";
            case CAPTURE: return "capture";
            case EP_CAPTURE: return "ep-capture";
            case KNIGHT_PROMO: return "knight-promotion";
            case BISHOP_PROMO: return "bishop-promotion";
            case ROOK_PROMO: return "rook-promotion";
            case QUEEN_PROMO: return "queen-promotion";
            case KNIGHT_PROMO_CAPTURE: return "knight-promotion-capture";
            case BISHOP_PROMO_CAPTURE: return "bishop-promotion-capture";
            case ROOK_PROMO_CAPTURE: return "rook-promotion-capture";
            case QUEEN_PROMO_CAPTURE: return "queen-promo-capture";
            default: 
                System.out.println("Error: Impossible flag was sent to flagsToString() -> Bit.java");
                System.exit(1);
                return "";
        }
    }

    public static String toPaddedBinaryString(byte value, int padding) {
        String paddingSize = "%" + String.valueOf(padding) + "s";
        return String.format(paddingSize, Integer.toBinaryString(value)).replace(' ', '0');
    }
}
