package model;

import java.util.HashMap;

import helper.Bit;
import helper.Convert;
import helper.Debug;
import helper.FEN;
import helper.Offset;
import helper.Printer;
import helper.Timer;
import model.attacks.KingAttacks;

public class Bitboard {
    // a list of all bitboards used to track pieces (i.e. whiteKnight or blackBishop)
    public static String[] pieceKeys = {
        "whitePawn",
        "whiteKnight",
        "whiteBishop",
        "whiteRook",
        "whiteQueen",
        "whiteKing",

        "blackPawn",
        "blackKnight",
        "blackBishop",
        "blackRook",
        "blackQueen",
        "blackKing",
    };

    // technically pieces are dynamic but these are non-piece specific dynamic bitboards
    public static String[] dynamicKeys = {
        "white",
        "black",
        "empty",
        "occupied",
        "ep",
    };

    // a list of all static bitboards used to check position on the board (i.e. whitePawnStart or blackPromotion)
    public static String[] staticKeys = {
        "whitePawnStart",
        "whitePromotion",

        "blackPawnStart",
        "blackPromotion",

        "border",
        "aFile",
        "hFile",
        "1Rank",
        "8Rank",
    };

    private static HashMap<String, Long> bitboard = new HashMap<>();

    public static void initBitboardsByFEN(String currentFEN) {
        // for when the starting FEN does not include all pieces we still need to define them to 0 to avoid bitboard lookup errors
        defineBitboardsByKeys(); 

        if(Debug.on("E1")) {
            Timer.start("initBitboards", "nano");
        }

        // piece and dynamic bitboards
        byte rank = 7;
        byte file = 0;

        for(char c : currentFEN.toCharArray()) {
            if(c == ' ') {
                break;
            } else if (c == '/') {
                rank--;
                file = 0;
            } else if(Character.isDigit(c)) {
                int emptySquares = Character.getNumericValue(c);
                for(int i = 0; i < emptySquares; i++) {
                    safeAddToBitboard("empty", Convert.uvToBitIndex(rank, file));
                    file++;
                }
            } else {
                int bitIndex = Convert.uvToBitIndex(rank, file);

                safeAddToBitboard(FEN.getPieceString(c), bitIndex);
                safeAddToBitboard("occupied", bitIndex);

                if(Character.isLowerCase(c)) {
                    safeAddToBitboard("black", bitIndex);
                } else {
                    safeAddToBitboard("white", bitIndex);
                }

                file++;
            }
        }
        bitboard.put("ep", 0x0L);

        // static bitboards
        // Note: I am currently ignoring the possibility that the player may choose to play as black, thus swapping white and black
        bitboard.put("whitePawnStart", 0xFF00L);
        bitboard.put("whitePromotion", 0xFF00000000000000L);

        bitboard.put("blackPawnStart", 0xFF000000000000L);
        bitboard.put("blackPromotion", 0xFFL);

        bitboard.put("border", 0xff818181818181ffL);
        bitboard.put("aFile", 0x101010101010101L);
        bitboard.put("hFile", 0x8080808080808080L);
        bitboard.put("1Rank", 0xFFL);
        bitboard.put("8Rank", 0xFF00000000000000L);

        // precomputed rank, file, major and minor diagonal masks used for limiting pinned pieces
        for(rank = 0; rank < 8; rank++) {
            bitboard.put("rank" + rank, 0xFFL << (rank * 8));
        }

        long fileMask = 0;
        for(file = 0; file < 8; file++) {
            fileMask = 0;
            for(rank = 0; rank < 8; rank++) {
                fileMask |= (1L << (rank * 8 + file));
            }
            bitboard.put("file" + file, fileMask);
        }

        /* 
            This approach to diagonals technically stores an extra 4 masks that will not be used - you 
            must have at least 3 squares available for a pin to exist (pinning piece -> pinned piece - > king) 
            however the lost space is negligable and it makes naming more annoying so I am choosing to ignore this 
        */
        long majorMask = 0;
        for(byte diagonal = -7; diagonal <= 7; diagonal++) {
            majorMask = 0;
            for(rank = 0; rank < 8; rank++) {
                file = (byte) (rank - diagonal);
                if(file >= 0 && file < 8) {
                    majorMask |= (1L << (rank * 8 + file));
                }
            }
            bitboard.put("major" + (diagonal + 7), majorMask);
        }
                    
        long minorMask = 0;
        for(byte diagonal = 0; diagonal <= 14; diagonal++) {
            minorMask = 0;
            for(rank = 0; rank < 8; rank++) {
                file = (byte) (diagonal - rank);
                if(file >= 0 && file < 8) {
                    minorMask |= (1L << (rank * 8 + file));
                }
            }
            bitboard.put("minor" + diagonal, minorMask);
        }

        // Debugging info
        if(Debug.on("E1")) {
            Timer.stop("initBitboards", true);
        }

        if(Debug.on("A1")) {
            Printer.print("\n >> Printing all initialized bitboards as squares");
            Printer.printAllBitboards();
            Printer.print("\n >> Successfully printed all initialized bitboards as squares");
        }

        if(Debug.on("A2")) {
            Printer.print("\n >> Printing all initialized bitboards as lines (LSB)");
            Printer.printAllBitboardsAsLines();
            Printer.print("\n >> Successfully printed all initialized bitboards as lines (LSB)");
        }
    }

    public static long getBitboard(String key) {
        if(bitboard.get(key) == null) {
            System.out.println("Failed to retrieve \"" + key + "\" bitboard in Bitboard.java -> getBitboard(); shutting down");
            System.exit(1);
        }

        return bitboard.get(key);
    }

    public static void setBitboard(String key, long value) {
        if(bitboard.get(key) == null) {
            System.out.println("Failed to retrieve \"" + key + "\" bitboard in Bitboard.java -> setBitboard(); shutting down");
            System.exit(1);
        }

        bitboard.put(key, value);
    }

    public static int getPieceIDFromKey(String key) {
        String[] words = splitCamelCase(key);

        int ID = 0;

        if(words[0].equals("white")) {
            ID += 8;
        } else {
            ID += 16;
        }

        switch(words[1]) {
            case "Pawn":
                ID += 6;
            break;
            case "Knight":
                ID += 4;
            break;
            case "Bishop":
                ID += 3;
            break;
            case "Rook":
                ID += 5;
            break;
            case "Queen":
                ID += 2;
            break;
            case "King":
                ID += 1;
            break;
            default:
                System.out.println("Error in getPieceIDFromKey() in model.Bitboard.java");
                return -1;
        }

        return ID;
    }

    // only and all bitboards should be updated in this method
    public static void updateWithMove(short move) {
        clearEnPassant();
        
        if(Move.isCapture(move)) {
            removeOpponent(move);
        }

        if(Move.isDoublePawnPush(move)) {            
            setEnPassant(Offset.behind(Move.getToIndex(move)));
        }

        if(Move.isPromotion(move)) {
            removeFromBitboard(BoardLookup.getPieceByBitIndex(Move.getToIndex(move)), Move.getToIndex(move));
        }

        if(Move.isKing(move)) {
            KingAttacks.setSelfKingIndex(Move.getToIndex(move));
        }

        advanceSelf(move);
        updateNeutrals(move);
    }

    public static long getLineMask(int square1, int square2) {
        // rank and file could be from square1 or square2. It doesn't matter because they are only used if a line-relationship is found
        int rank = square1 / 8;
        int file = square1 % 8;

        switch(Offset.getSquareRelationship(square1, square2)) {
            case 0:
                return Bitboard.getBitboard("rank" + rank);
            case 1:
                return Bitboard.getBitboard("file" + file);
            case 2:
                return Bitboard.getBitboard("major" + (rank - file + 7));
            case 3:
                return Bitboard.getBitboard("minor" + (rank + file));
            default:
                return 0;
        }
    }

    public static long getSlidingPieces(String side) {
        return (bitboard.get(side + "Bishop") | bitboard.get(side + "Rook") | bitboard.get(side + "Queen"));
    }

    public static void setEnPassant(int index) {
        bitboard.put("ep", Bit.setBit(bitboard.get("ep"), index));
    }

    public static void clearEnPassant() {
        bitboard.put("ep", 0x0L);
    }

    public static void hide(String key, int index) {
        Bitboard.setBitboard(key, Bit.clearBit(Bitboard.getBitboard(key), index));
    }

    public static void restore(String key, int index) {
        Bitboard.setBitboard(key, Bit.setBit(Bitboard.getBitboard(key), index));
    }

    private static void advanceSelf(short move) {
        if(Move.isPromotion(move)) {
            addToBitboard(Move.getKeyFromFlag(move), Move.getToIndex(move)); // add self piece (promotion case)
            removeFromBitboard(BoardLookup.getPieceByBitIndex(Move.getFromIndex(move)), Move.getFromIndex(move));
        } else {
            updateBitboard(BoardLookup.getPieceByBitIndex(Move.getFromIndex(move)), Move.getFromIndex(move), Move.getToIndex(move)); // add self piece 
        }

        updateBitboard(GameInfo.getTurn(), Move.getFromIndex(move), Move.getToIndex(move)); // add self side
        updateBitboard("occupied", Move.getFromIndex(move), Move.getToIndex(move)); // add occupied
    }

    private static void removeOpponent(short move) {
        if(Move.isEnPassant(move)) {
            removeFromBitboard(BoardLookup.getPieceByBitIndex((byte) Offset.behind(Move.getToIndex(move))), Offset.behind(Move.getToIndex(move))); // piece
            removeFromBitboard(GameInfo.getOpponent(), Offset.behind(Move.getToIndex(move))); // side
        } else {
            removeFromBitboard(BoardLookup.getPieceByBitIndex(Move.getToIndex(move)), Move.getToIndex(move)); // piece
            removeFromBitboard(GameInfo.getOpponent(), Move.getToIndex(move)); // side
        }
    }

    private static void updateNeutrals(short move) {
        // update empty bitboard
        addToBitboard("empty", Move.getFromIndex(move));
        removeFromBitboard("empty", Move.getToIndex(move));

        if(Move.isEnPassant(move)) {
            addToBitboard("empty", Offset.behind(Move.getToIndex(move)));
        }
        
        // occupied is the negation of empty
        bitboard.put("occupied", ~bitboard.get("empty")); 
    }

    private static void updateBitboard(String key, int bitIndexToRemove, int bitIndexToAdd) {
        bitboard.put(key, Bit.clearBit(bitboard.get(key), bitIndexToRemove));
        bitboard.put(key, Bit.setBit(bitboard.get(key), bitIndexToAdd));
    }

    private static void removeFromBitboard(String key, int bitIndexToRemove) {
        bitboard.put(key, Bit.clearBit(bitboard.get(key), bitIndexToRemove));
    }

    private static void addToBitboard(String key, int bitIndexToAdd) {
        bitboard.put(key, Bit.setBit(bitboard.get(key), bitIndexToAdd));
    }

    // for use only when we know failure is okay and should result in a new bitboard being created
    private static void safeAddToBitboard(String key, int index) {
        if(bitboard.get(key) == null) {
            bitboard.put(key, 0x0L);
        }
        
        bitboard.put(key, Bit.setBit(bitboard.get(key), index));
    }

    private static String[] splitCamelCase(String camelCaseString) {
        int index = 0;
        while (index < camelCaseString.length() && !Character.isUpperCase(camelCaseString.charAt(index))) {
            index++;
        }

        String[] words = new String[2];
        words[0] = camelCaseString.substring(0, index);
        words[1] = camelCaseString.substring(index);

        return words;
    }

    private static void defineBitboardsByKeys() {
        for(int i = 0; i < pieceKeys.length; i++) {
            bitboard.put(pieceKeys[i], 0x0L);
        }

        for(int i = 0; i < dynamicKeys.length; i++) {
            bitboard.put(dynamicKeys[i], 0x0L);
        }

        for(int i = 0; i < staticKeys.length; i++) {
            bitboard.put(staticKeys[i], 0x0L);
        }
    }
}
