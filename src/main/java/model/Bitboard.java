package model;

import java.util.HashMap;

import controller.ControllerMain;
import helper.Convert;
import helper.Debug;
import helper.Printer;
import view.board.BoardOverlayGraphic;

public class Bitboard {
    public static String[] keys = {
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
    private static int startWhiteKey = 0;
    private static int stopWhiteKey = 6;
    private static int startBlackKey = 6;
    private static int stopBlackKey = 12;

    private static HashMap<String, Long> bitboard = new HashMap<>();

    public static void initBitboards() {
        if(GameInfo.getSide() == 0) {
            bitboard.put("whitePawn", 0xFF00L);
            bitboard.put("whiteKnight", 0x42L);
            bitboard.put("whiteBishop", 0x24L);
            bitboard.put("whiteRook", 0x81L);
            bitboard.put("whiteQueen", 0x8L);
            bitboard.put("whiteKing", 0x10L);
            bitboard.put("white", 0xFFFFL);

            bitboard.put("blackPawn", 0xFF000000000000L);
            bitboard.put("blackKnight", 0x4200000000000000L);
            bitboard.put("blackBishop", 0x2400000000000000L);
            bitboard.put("blackRook", 0x8100000000000000L);
            bitboard.put("blackQueen", 0x800000000000000L);
            bitboard.put("blackKing", 0x1000000000000000L);
            bitboard.put("black", 0xFFFF000000000000L);
        } else {
            bitboard.put("blackPawn", 0xFF00L);
            bitboard.put("blackKnight", 0x42L);
            bitboard.put("blackBishop", 0x24L);
            bitboard.put("blackRook", 0x81L);
            bitboard.put("blackKing", 0x10L);
            bitboard.put("blackQueen", 0x8L);
            bitboard.put("black", 0xFFFFL);

            bitboard.put("whitePawn", 0xFF000000000000L);
            bitboard.put("whiteKnight", 0x4200000000000000L);
            bitboard.put("whiteBishop", 0x2400000000000000L);
            bitboard.put("whiteRook", 0x8100000000000000L);
            bitboard.put("whiteQueen", 0x800000000000000L);
            bitboard.put("whiteKing", 0x1000000000000000L);
            bitboard.put("white", 0xFFFF000000000000L);
        }

        bitboard.put("occupied", 0xFFFF00000000FFFFL);

        bitboard.put("whitePawnStart", 0xff00L);
        bitboard.put("blackPawnStart", 0xff000000000000L);
        bitboard.put("whitePromotion", 0xff00000000000000L);
        bitboard.put("blackPromotion", 0xffL);

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
    };

    public static long getBitboard(String key) {
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

    public static String getKeyFromBitIndex(int bitIndex) {
        long bitmask = 1L << bitIndex;

        if((bitmask & bitboard.get("white")) != 0) {
            for(int i = startWhiteKey; i < stopWhiteKey; i++) {
                if((bitmask & bitboard.get(keys[i])) != 0) {
                    return keys[i];
                }
            }
        } else {
            for(int i = startBlackKey; i < stopBlackKey; i++) {
                if((bitmask & bitboard.get(keys[i])) != 0) {
                    return keys[i];
                }
            }
        }

        System.out.println("Error in Bitboard.java -> getKeyFromBitIndex(int bitIndex): Should have returned a value");
        System.exit(1);
        
        return "NaN";
    }

    public static void updateWithMove(Move move) {
        if(move.isPromotion()) {
            if(move.getPromotionSelected().equals("none")) {
                removeFromBitboard(GameInfo.getSideToPlay() + "Pawn", move.getFromIndex());
                removeFromBitboard(GameInfo.getSideToPlay(), move.getFromIndex());
                removeFromBitboard("occupied", move.getFromIndex());
            } else if(move.getPromotionSelected().equals("undo")) {
                addToBitboard(GameInfo.getSideToPlay() + "Pawn", move.getFromIndex());
                addToBitboard(GameInfo.getSideToPlay(), move.getFromIndex());
                addToBitboard(GameInfo.getSideToPlay(), move.getFromIndex());
                
                // put highlighting to previous move and terminate to regular play
                BoardOverlayGraphic.highlightMove(MoveRecord.peekMove());
                Promotion.terminatePromotion();
                ControllerMain.redrawBoard();
            } else {
                if(move.isCapture()) {
                    removeEnemy(move);
                }

                addToBitboard(GameInfo.getSideToPlay() + move.getPromotionSelected(), move.getToIndex()); // add selected self piece
                addToBitboard(GameInfo.getSideToPlay(), move.getToIndex());
                addToBitboard("occupied", move.getToIndex());

                Promotion.endPromotion();
                ControllerMain.redrawBoard();
            }
        } else if(move.isEnPassant()) {
            move.setPieceCaptured(GameInfo.getSideToWait() + "Pawn");

            removeFromBitboard(GameInfo.getSideToWait(), Convert.bitIndexShiftBySide(GameInfo.getSideToPlay(), move.getToIndex(), -8));
            removeFromBitboard(GameInfo.getSideToWait() + "Pawn", Convert.bitIndexShiftBySide(GameInfo.getSideToPlay(), move.getToIndex(), -8));
            removeFromBitboard("occupied", Convert.bitIndexShiftBySide(GameInfo.getSideToPlay(), move.getToIndex(), -8));

            advanceSelf(move);
        } else {
            if(move.isCapture()) {
                removeEnemy(move);
            }

            advanceSelf(move);
        }
    }

    private static void advanceSelf(Move move) {
        updateBitboard(move.getPiece(), move.getFromIndex(), move.getToIndex()); // add self piece 
        updateBitboard(GameInfo.getSideToPlay(), move.getFromIndex(), move.getToIndex()); // add self side
        updateBitboard("occupied", move.getFromIndex(), move.getToIndex()); // add occupied
    }

    private static void removeEnemy(Move move) {
        move.setPieceCaptured(getKeyFromBitIndex(move.getToIndex())); // set piece captured
        removeFromBitboard(getKeyFromBitIndex(move.getToIndex()), move.getToIndex()); // remove enemy piece 
        removeFromBitboard(GameInfo.getSideToWait(), move.getToIndex()); // remove enemy side 
    }

    private static void updateBitboard(String key, int bitIndexToRemove, int bitIndexToAdd) {
        bitboard.put(key, bitboard.get(key) & ~(1L << bitIndexToRemove));
        bitboard.put(key, bitboard.get(key) | (1L << bitIndexToAdd));
    }

    private static void removeFromBitboard(String key, int bitIndexToRemove) {
        bitboard.put(key, bitboard.get(key) & ~(1L << bitIndexToRemove));
    }

    private static void addToBitboard(String key, int bitIndexToAdd) {
        bitboard.put(key, bitboard.get(key) | (1L << bitIndexToAdd));
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
}
