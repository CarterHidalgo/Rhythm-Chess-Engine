package model;

import java.util.HashMap;
import helper.FEN;
import helper.Convert;
import helper.Bit;
import helper.Debug;
import helper.Printer;
import helper.Timer;
import helper.Offset;

public class Bitboard {
    // a list of all piece bitboards used to track pieces (i.e. whiteKnight or blackBishop)
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
        // for when the starting FEN does not include all pieces we still need to define them
        defineBitboardsByKeys(); 

        if(Debug.on("E1")) {
            Timer.start("initBitboards", "nano");
        }

        // DYNAMIC BITBOARDS

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


        // STATIC BITBOARDS
        // Note: Ignoring the possibility that the player may choose to play as black, thus swapping white and black
        bitboard.put("whitePawnStart", 0xFF00L);
        bitboard.put("whitePromotion", 0xFF00000000000000L);

        bitboard.put("blackPawnStart", 0xFF000000000000L);
        bitboard.put("blackPromotion", 0xFFL);

        bitboard.put("border", 0xff818181818181ffL);
        bitboard.put("aFile", 0x101010101010101L);
        bitboard.put("hFile", 0x8080808080808080L);
        bitboard.put("1Rank", 0xFFL);
        bitboard.put("8Rank", 0xFF00000000000000L);

        // for(int index = 0; index < 64; index++) {
        //     long rookMask = getRookMask(index);

        //     if(rookMask != 0) {
        //         System.out.println("0x" + Long.toHexString(rookMask) + "L, // " + index);
        //     }

        // }

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
            System.out.println("Failed to retrieve \"" + key + "\" bitboard in Bitboard.java; shutting down");
            System.exit(1);
        }

        return bitboard.get(key);
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

    public static void updateWithMove(short move) {
        if(Move.isCapture(move)) {
            removeOpponent(move);
        }

        advanceSelf(move);
        updateNeutrals(move);

        // if(Move.isPromotion(move)) {
            // if(move.getPromotionSelected().equals("none")) {
            //     removeFromBitboard(GameInfo.getSideToPlay() + "Pawn", move.getFromIndex());
            //     removeFromBitboard(GameInfo.getSideToPlay(), move.getFromIndex());
            //     removeFromBitboard("occupied", move.getFromIndex());
            // } else if(move.getPromotionSelected().equals("undo")) {
            //     addToBitboard(GameInfo.getSideToPlay() + "Pawn", move.getFromIndex());
            //     addToBitboard(GameInfo.getSideToPlay(), move.getFromIndex());
            //     addToBitboard(GameInfo.getSideToPlay(), move.getFromIndex());
                
            //     // put highlighting to previous move and terminate to regular play
            //     BoardOverlayGraphic.highlightMove(MoveRecord.peekMove());
            //     Promotion.terminatePromotion();
            //     ControllerMain.redrawBoard();
            // } else {
            //     if(move.isCapture()) {
            //         removeEnemy(move);
            //     }

            //     addToBitboard(GameInfo.getSideToPlay() + move.getPromotionSelected(), move.getToIndex()); // add selected self piece
            //     addToBitboard(GameInfo.getSideToPlay(), move.getToIndex());
            //     addToBitboard("occupied", move.getToIndex());

            //     Promotion.endPromotion();
            //     ControllerMain.redrawBoard();
            // }
        // } else if(Move.isEnPassant(move)) {
            // move.setPieceCaptured(GameInfo.getSideToWait() + "Pawn");

        //     removeFromBitboard(GameInfo.getSideToWait(), Convert.bitIndexShiftBySide(GameInfo.getSideToPlay(), Move.getToIndex(move), -8));
        //     removeFromBitboard(GameInfo.getSideToWait() + "Pawn", Convert.bitIndexShiftBySide(GameInfo.getSideToPlay(), Move.getToIndex(move), -8));
        //     removeFromBitboard("occupied", Convert.bitIndexShiftBySide(GameInfo.getSideToPlay(), Move.getToIndex(move), -8));

        //     advanceSelf(move);
        // } else {
        //     if(Move.isCapture(move)) {
        //         removeEnemy(move);
        //     }

        //     advanceSelf(move);
        // }
    }

    public static void setEnPassant(int index) {
        bitboard.put("ep", Bit.setBit(bitboard.get("ep"), index));
    }

    public static void clearEnPassant() {
        bitboard.put("ep", 0x0L);
    }

    public static long getRookMask(int idx) {
        if (idx < 0 || idx >= 64) {
            throw new IllegalArgumentException("Index must be between 0 and 63.");
        }

        int row = idx / 8;
        int col = idx % 8;

        long rowMask = 0xFFL << (row * 8);
        long colMask = 0x0101010101010101L << col;
        
        long finalMask = (rowMask | colMask);

        if(row != 0) {
            finalMask &= ~bitboard.get("1Rank");
        }

        if(row != 7) {
            finalMask &= ~bitboard.get("8Rank");
        }

        if(col != 0) {
            finalMask &= ~bitboard.get("aFile");
        }

        if(col != 7) {
            finalMask &= ~bitboard.get("hFile");
        }

        finalMask = Bit.clearBit(finalMask, idx);

        return finalMask;
    }

    private static void advanceSelf(short move) {
        updateBitboard(BoardLookup.getPieceByBitIndex(Move.getFromIndex(move)), Move.getFromIndex(move), Move.getToIndex(move)); // add self piece 
        updateBitboard(GameInfo.getTurn(), Move.getFromIndex(move), Move.getToIndex(move)); // add self side
        updateBitboard("occupied", Move.getFromIndex(move), Move.getToIndex(move)); // add occupied
    }

    private static void removeOpponent(short move) {
        if(Move.isEnPassant(move)) {
            removeFromBitboard(BoardLookup.getPieceByBitIndex((byte) Offset.behind(Move.getToIndex(move))), Offset.behind(Move.getToIndex(move)));
            removeFromBitboard(GameInfo.getOpponent(), Offset.behind(Move.getToIndex(move)));
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

        bitboard.put("occupied", ~bitboard.get("empty")); // occupied is the negation of empty
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
