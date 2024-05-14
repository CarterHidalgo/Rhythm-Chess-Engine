package helper;

public class FEN {
    private static String startingFEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    private static String currentFEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    public static String getStartingFEN() {
        return startingFEN;
    }

    public static void parseFEN(byte[] byteArray) {
        byte file = 0;
        byte rank = 7;
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

                // System.out.println(c + " " + Bit.toPaddedBinaryString(byteArray[index], 8));
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
        // TODO: update current FEN using BoardLookup
    }

    private static byte getPieceCode(char piece) {
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
}
