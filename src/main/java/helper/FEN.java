package helper;

public class FEN {
    // starting FEN and various testing FENs
    // NOTE: Exactly 1 king of opposite colors must be on the board in all cases or the program will crash fatally

    private static final String[] FEN_STRINGS = {
        // Starting Position
        "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", // [0] Starting position

        // Implementation tests
        "8/4p3/8/1pppR1p1/4p3/4p3/4p3/8 w - - 0 1", // [1] Rook magic bitboard test 
        "7B/8/8/8/8/8/8/B7 w - - 0 1", // [2] Bishop magic bitboard test
        "2r1B3/1p1p4/p1Q2RpP/n7/8/2B2p2/6P1/7R w - - 0 1", // [3] queen magic bitboard test
        "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - ", // [4] Perft Position 2 (https://www.chessprogramming.org/Perft_Results)
        "8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - ", // [5] Perft Position 3
        "rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8 ", // [6] Perft Position 5 (ibid)
        "r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1 w - - 0 10", // [7] Perft Position 6 (ibid)
        "3n1b2/4P3/8/2p5/1p2p3/p7/PPPP1PPP/8 w - - 0 1", // [8] Player pawn promotion
        "r1bqkbnr/1ppp1ppp/p1n5/1B2p3/4P3/5N2/PPPP1PPP/RNBQK2R w KQkq - 0 4", // [9] Ruy Lopez Opening
        "rnbqkbnr/p3p2p/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", // [10] Pawn protection test
        "k2r3r/1p6/b1p1QN2/1P6/8/8/4K3/R6R w - - 0 1", // [11] King Castling and Pin Test 1
        "k7/8/8/3P4/4p2R/8/3P4/K6Q w - - 0 1", // [12] Pin Test 2
        "k7/8/8/8/4p2R/8/3P4/K6Q w - - 0 1", // [13] Pin Test 3
        "8/7k/8/8/3K1p1r/8/2b1P3/1B6 w - - 0 1", // [14] EP | Pin Test 4
        "k7/8/5r2/8/3R4/8/1K6/8 w - - 0 1", // [15] EP | Pin Test 5
        "8/3q4/5k2/1Rp5/4Pp2/8/3B1N2/K7 w - - 0 1", // [16] King Check (Ray + Pawn) 
        "r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1", // [17] Castling

        // Manual Web Perft Checks
        "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1", // [18] Kiwipete
        "8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 1", // [19] Position 3
        "r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1", // [20] Position 4
        "rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8", // [21] Position 5
        "r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1 w - - 0 10", // [22] Position 6

        "8/2k1r3/8/8/4K1r1/8/8/8 w - - 0 1", // [23] temp
    };

    private static String currentFEN = FEN_STRINGS[21];

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
            case 'P': return "whitePawn";
            case 'N': return "whiteKnight";
            case 'B': return "whiteBishop";
            case 'R': return "whiteRook";
            case 'Q': return "whiteQueen";
            case 'K': return "whiteKing";
            case 'p': return "blackPawn"; 
            case 'n': return "blackKnight";
            case 'b': return "blackBishop";
            case 'r': return "blackRook";
            case 'q': return "blackQueen";
            case 'k': return "blackKing";
            default: return "";
        }
    }
}