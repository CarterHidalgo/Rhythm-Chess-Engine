package model;

import helper.FEN;
import helper.Offset;
import helper.Bit;
// import helper.Printer;

public class BoardLookup {
    /*
     * Piece enumeration for BoardLookup array. Note this is NOT the same enumeration
     * used in PieceGraphic and is separate from it. Enumeration is arbitrary. 
     * 
     * Bit 0: Color (1 bit)
     *  0: White
     *  1: Black
     * Bits 1-3: Piece type (3 bits)
     *  000: Empty square
     *  001: Pawn
     *  010: Knight
     *  011: Bishop
     *  100: Rook
     *  101: Queen
     *  110: King
     *  111: [unused]
     */

    private static byte[] board = new byte[32];

    public static void initBoardLookup() {
        FEN.parseFEN(board);
    }

    public static void updateWithMove(short move) {
        setSquare(Move.getToIndex(move), getByteCodeByBitIndex(Move.getFromIndex(move))); // update to square
        setSquare(Move.getFromIndex(move), Bit.EMPTY); // update from square

        if(Move.isEnPassant(move)) {
            setSquare((byte) Offset.behind(Move.getToIndex(move)), Bit.EMPTY);
        } else if(Move.isCastle(move)) {

        } else if(Move.isPromotion(move)) {

        }
    }

    private static void setSquare(byte bitIndex, byte code) {
        board[bitIndex / 2] = Bit.setBitRange(board[bitIndex / 2], ((bitIndex % 2 == 0) ? 4 : 0), code, 3);
    }

    public static String getPieceByBitIndex(byte bitIndex) {
        byte code = getByteCodeByBitIndex(bitIndex);

        switch(code) {
            case Bit.WHITE_PAWN: return "whitePawn";
            case Bit.WHITE_KNIGHT: return "whiteKnight"; 
            case Bit.WHITE_BISHOP: return "whiteBishop";
            case Bit.WHITE_ROOK: return "whiteRook";
            case Bit.WHITE_QUEEN: return "whiteQueen";
            case Bit.WHITE_KING: return "whiteKing";
            
            case Bit.BLACK_PAWN: return "blackPawn";
            case Bit.BLACK_KNIGHT: return "blackKnight";
            case Bit.BLACK_BISHOP: return "blackBishop";
            case Bit.BLACK_ROOK: return "blackRook";
            case Bit.BLACK_QUEEN: return "blackQueen";
            case Bit.BLACK_KING: return "blackKing";

            case Bit.EMPTY: return "empty";
            default: 
                System.out.println("Impossible bit index | cannot return a piece string in getPieceByBitIndex() -> BoardLookup.java");
                // System.out.println("code: " + Bit.toPaddedBinaryString(code, 8) + " at " + bitIndex);
                System.exit(1);
                return "";
        }
    }

    public static byte getByteCodeByBitIndex(byte bitIndex) {
        int startIndex = (bitIndex % 2 == 0) ? 4 : 0;
        byte value = Bit.getBitRange(board[bitIndex / 2], startIndex, startIndex + 3);

        return value;
    }
}
