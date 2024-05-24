package helper;

public class FEN {
    private static final String[] FEN_STRINGS = {
        "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", // [0] Starting position
        "8/8/2R5/8/8/5B2/8/8 w - - 0 1", // [1] Rook magic bitboard test 
        "2r1B3/1p1p4/p1Q2RpP/n7/8/2B2p2/6P1/7R w - - 0 1", // [2] queen magic bitboard test
        "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - ", // [3] Position 2 (https://www.chessprogramming.org/Perft_Results)
        "rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8 ", // [4] Position 5 (ibid)
        "r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1 w - - 0 10", // [5] Position 6 (ibid)
    };
    private static String currentFEN = FEN_STRINGS[0];

    public static String getStartingFEN() {
        return FEN_STRINGS[0];
    }

    public static String getCurrentFEN() {
        return currentFEN;
    }

    public static void parseFEN(byte[] byteArray) {
        byte rank = 7;
        byte file = 0;
        byte mask = 0b1111;
        byte index;
        byte nibble = 0;

        for(char c : currentFEN.toCharArray()) {
            if(c == ' ') {
                break;
            } else if (c == '/') {
                rank--;
                file = 0;
            } else if(Character.isDigit(c)) {
                int emptySquares = Character.getNumericValue(c);
                for(int i = 0; i < emptySquares; i++) {
                    index = (byte) ((rank * 8 + file) / 2);
                    nibble = (byte) ((file % 2 == 0) ? 4 : 0);
                    byteArray[index] = (byte) ((byteArray[index] & ~(mask << nibble)) | (Bit.EMPTY << nibble));
                    file++;
                }
            } else {
                byte pieceCode = getPieceCode(c);

                index = (byte) ((rank * 8 + file) / 2);
                nibble = (byte) ((file % 2 == 0) ? 4 : 0);
                byteArray[index] = (byte) ((byteArray[index] & ~(mask << nibble)) | (pieceCode << nibble));
                file++;
            }
        }
    }

    public static int charToPieceID(char c) {
        int ID = 0;

        if(Character.isUpperCase(c)) {
            ID += 8;
        } else {
            ID += 16;
        }

        switch(Character.toLowerCase(c)) {
            case 'p':
                ID += 6;
            break;
            case 'n':
                ID += 4;
            break;
            case 'b':
                ID += 3;
            break;
            case 'r':
                ID += 5;
            break;
            case 'q':
                ID += 2;
            break;
            case 'k':
                ID += 1;
            break;
            default:
                System.out.println("Error in \"FEN.java\": Expected valid piece char");
                return -1;
        }

        return ID;
    }

    public static void updateCurrentFEN() {
        // update current FEN using BoardLookup
    }

    public static byte getPieceCode(char piece) {
        switch (piece) {
            case 'P': return Bit.WHITE_PAWN; // white
            case 'N': return Bit.WHITE_KNIGHT;
            case 'B': return Bit.WHITE_BISHOP;
            case 'R': return Bit.WHITE_ROOK;
            case 'Q': return Bit.WHITE_QUEEN;
            case 'K': return Bit.WHITE_KING;
            case 'p': return Bit.BLACK_PAWN; // black
            case 'n': return Bit.BLACK_KNIGHT;
            case 'b': return Bit.BLACK_BISHOP;
            case 'r': return Bit.BLACK_ROOK;
            case 'q': return Bit.BLACK_QUEEN;
            case 'k': return Bit.BLACK_KING;
            default: return Bit.EMPTY;
        }
    }

    public static String getPieceString(char piece) {
        switch (piece) {
            case 'P': return "whitePawn"; // white
            case 'N': return "whiteKnight";
            case 'B': return "whiteBishop";
            case 'R': return "whiteRook";
            case 'Q': return "whiteQueen";
            case 'K': return "whiteKing";
            case 'p': return "blackPawn"; // black
            case 'n': return "blackKnight";
            case 'b': return "blackBishop";
            case 'r': return "blackRook";
            case 'q': return "blackQueen";
            case 'k': return "blackKing";
            default: return "";
        }
    }
}