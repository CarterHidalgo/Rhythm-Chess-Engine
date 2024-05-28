package model;

import java.util.ArrayList;

import helper.Bit;
import helper.Debug;
import helper.Offset;
import helper.Timer;
import model.attacks.KnightAttacks;
import model.magic.CompactMagicBitboard;
import model.magic.MagicBitboard;

public class MoveGeneration {
    private static ArrayList<Short> legalMoveList = new ArrayList<>();
    private static int numMoves = 0, captures = 0, ep = 0, castles = 0, promotions = 0, checks = 0, discoveryChecks = 0, doubleChecks = 0, checkmates = 0;
    private static int pawnMoves = 0, knightMoves = 0, bishopMoves = 0, rookMoves = 0, queenMoves = 0, kingMoves = 0;

    public static void generateLegalMoves() {

        if(Debug.on("E3")) {
            Timer.start("generateLegalMoves", "nano");
        }

        wipeMoveGeneration();

        generateLegalPawnMoves();
        generateLegalKnightMoves();
        generateLegalBishopMoves();
        generateLegalRookMoves();
        generateLegalQueenMoves();
        generateLegalKingMoves();

        numMoves = legalMoveList.size();
        
        // safety check to ensure breakdown of moves equals total moves
        if((pawnMoves + knightMoves + bishopMoves + rookMoves + queenMoves + kingMoves) != numMoves) {
            System.out.println("WARNING: Total moves does not equal the sum of its parts");
        }
        
        if(Debug.on("E3")) {
            Timer.stop("generateLegalMoves", true);
        }
        
        if(Debug.on("D1")) {
            System.out.println("\n" + numMoves + ((numMoves == 1) ? " move found" : " moves found") + " for " + GameInfo.getTurn());
            System.out.println(" " + pawnMoves + " pawn");
            System.out.println(" " + knightMoves + " knight");
            System.out.println(" " + bishopMoves + " bishop");
            System.out.println(" " + rookMoves + " rook");
            System.out.println(" " + queenMoves + " queen");
            System.out.println(" " + kingMoves + " king");
            System.out.println("----------------------");
            System.out.println(captures + " captures");
            System.out.println(ep + " " + "ep");
            System.out.println(castles + " castles");
            System.out.println(promotions + " promotions");
            System.out.println(checks + " checks");
            System.out.println(discoveryChecks + " discoveryChecks");
            System.out.println(doubleChecks + " doubleChecks");
            System.out.println(checkmates + " checkmates");
        }
        
        if(Debug.on("D2")) {
            System.out.println();
            for(short move : legalMoveList) {
                Move.print(move);
            }
        }
    }

    public static boolean legalMoveListContainsMove(short move) {
        return legalMoveList.contains(move);
    }

    public static short getMoveWithSimple(short simpleMove) {
        for(short move : legalMoveList) {
            if(simpleMove == (move & 0x0FFF)) {
                return move;
            }
        }

        return 0;
    }

    public static long getMoveBitboard(int square) {
        long moves = 0;
        
        for(short move : legalMoveList) {
            if(Move.getFromIndex(move) == square) {
                moves = Bit.setBit(moves, Move.getToIndex(move));
            }
        }

        return moves;
    }
    
    private static void generateLegalPawnMoves() {
        /*
         * Things to consider
         *   Single pawn push ✓
         *   Double pawn push ✓
         *   En-Passant ✓
         *   Diagonal captures ✓
         *   Promotion
         *   Pins
         *   Checks
         * 
         *  Note: Promotions to different pieces count as unique moves when counting for PERFT. Promotion to a queen 
         *      is not the same move as promotion to a knight
         */

        long selfBitboard = Bitboard.getBitboard(GameInfo.getTurn() + "Pawn");
        long emptyBitboard = Bitboard.getBitboard("empty");
        while(Bit.hasNextBit(selfBitboard)) {
            int fromIndex = Bit.getNextBitIndex(selfBitboard);
            int toIndex;

            // for each pawn in selfBitboard consider a single push, a double push, and a diagonal capture (inc EP)
            
            // single push
            if(Bit.isSet(emptyBitboard, Offset.forward(fromIndex))) {
                toIndex = Offset.forward(fromIndex);

                legalMoveList.add(Move.createMove((byte) fromIndex, (byte) toIndex, Bit.QUIET));
                numMoves++;
                pawnMoves++;

                // double push
                if(
                    Bit.isSet(Bitboard.getBitboard(GameInfo.getTurn() + "PawnStart"), fromIndex) &&
                    Bit.isSet(emptyBitboard, Offset.doubleForward(fromIndex))  
                ) {
                    toIndex = Offset.doubleForward(fromIndex);

                    legalMoveList.add(Move.createMove((byte) fromIndex, (byte) toIndex, Bit.DOUBLE_PAWN));
                    numMoves++;
                    pawnMoves++;
                }
            }

            int leftForwardDiagonal = Offset.leftForwardDiagonal(fromIndex);
            int rightForwardDiagonal = Offset.rightForwardDiagonal(fromIndex);

            // left diagonal
            if(Offset.isNotRelativeLeftEdge(fromIndex)) {
                if(Bit.isSet(Bitboard.getBitboard(GameInfo.getOpponent()), leftForwardDiagonal)) {
                    // left diagonal capture
                    toIndex = leftForwardDiagonal;
                    legalMoveList.add(Move.createMove((byte) fromIndex, (byte) toIndex, Bit.CAPTURE));
                    numMoves++;
                    pawnMoves++;
                    captures++;
                }

                if(Bit.isSet(Bitboard.getBitboard("ep"), leftForwardDiagonal)) {
                    // right diagonal ep capture
                    toIndex = leftForwardDiagonal;
                    legalMoveList.add(Move.createMove((byte) fromIndex, (byte) toIndex, Bit.EP_CAPTURE));
                    numMoves++;
                    pawnMoves++;
                    captures++;
                    ep++;
                }
            }

            // right diagonal
            if(Offset.isNotRelativeRightEdge(fromIndex)) {
                if(Bit.isSet(Bitboard.getBitboard(GameInfo.getOpponent()), rightForwardDiagonal)) {
                    // right diagonal capture
                    toIndex = rightForwardDiagonal;
                    legalMoveList.add(Move.createMove((byte) fromIndex, (byte) toIndex, Bit.CAPTURE));
                    numMoves++;
                    pawnMoves++;
                    captures++;
                }

                if(Bit.isSet(Bitboard.getBitboard("ep"), rightForwardDiagonal)) {
                    // right diagonal ep capture
                    toIndex = rightForwardDiagonal;
                    legalMoveList.add(Move.createMove((byte) fromIndex, (byte) toIndex, Bit.EP_CAPTURE));
                    numMoves++;
                    pawnMoves++;
                    captures++;
                    ep++;
                }
            }

            selfBitboard = Bit.clearBit(selfBitboard, fromIndex);
        }
    }
    
    private static void generateLegalKnightMoves() {
        long toSquareBitboard;
    
        long selfBitboard = Bitboard.getBitboard(GameInfo.getTurn() + "Knight");
        while(Bit.hasNextBit(selfBitboard)) {
            int fromIndex = Bit.getNextBitIndex(selfBitboard);
            byte flags;

            toSquareBitboard = KnightAttacks.getKnightToSquareBitboard(fromIndex);
            toSquareBitboard &= ~Bitboard.getBitboard(GameInfo.getTurn());
    
            while(Bit.hasNextBit(toSquareBitboard)) {
                int toIndex = Bit.getNextBitIndex(toSquareBitboard);

                if(Bit.isSet(Bitboard.getBitboard(GameInfo.getOpponent()), toIndex)) {
                    flags = Bit.CAPTURE;
                    captures++;
                } else {
                    flags = Bit.QUIET;
                }
                
                legalMoveList.add(Move.createMove((byte) fromIndex, (byte) toIndex, flags));
                numMoves++;
                knightMoves++;
    
                toSquareBitboard = Bit.clearBit(toSquareBitboard, toIndex);
            }
    
            selfBitboard = Bit.clearBit(selfBitboard, fromIndex);
        }
    }

    private static void generateLegalBishopMoves() {
        long toSquareBitboard;
        long selfBitboard = Bitboard.getBitboard(GameInfo.getTurn() + "Bishop");

        while(Bit.hasNextBit(selfBitboard)) {
            int fromIndex = Bit.getNextBitIndex(selfBitboard); // get lsb
            byte flags;

            toSquareBitboard = CompactMagicBitboard.getBishopMoves(fromIndex);
            toSquareBitboard &= ~Bitboard.getBitboard(GameInfo.getTurn());

            while(Bit.hasNextBit(toSquareBitboard)) {
                int toIndex = Bit.getNextBitIndex(toSquareBitboard);

                if(Bit.isSet(Bitboard.getBitboard(GameInfo.getOpponent()), toIndex)) {
                    flags = Bit.CAPTURE;
                    captures++;
                } else {
                    flags = Bit.QUIET;
                }

                legalMoveList.add(Move.createMove((byte) fromIndex, (byte) toIndex, flags));
                numMoves++;
                bishopMoves++;

                toSquareBitboard = Bit.clearBit(toSquareBitboard, toIndex);
            }

            selfBitboard = Bit.clearBit(selfBitboard, fromIndex); // pop lsb
        }
    }

    private static void generateLegalRookMoves() {
        long toSquareBitboard;
        long selfBitboard = Bitboard.getBitboard(GameInfo.getTurn() + "Rook");
        while(Bit.hasNextBit(selfBitboard)) {
            int fromIndex = Bit.getNextBitIndex(selfBitboard); // get lsb
            byte flags;

            toSquareBitboard = CompactMagicBitboard.getRookMoves(fromIndex);
            toSquareBitboard &= ~Bitboard.getBitboard(GameInfo.getTurn());

            while(Bit.hasNextBit(toSquareBitboard)) {
                int toIndex = Bit.getNextBitIndex(toSquareBitboard);

                if(Bit.isSet(Bitboard.getBitboard(GameInfo.getOpponent()), toIndex)) {
                    flags = Bit.CAPTURE;
                    captures++;
                } else {
                    flags = Bit.QUIET;
                }

                legalMoveList.add(Move.createMove((byte) fromIndex, (byte) toIndex, flags));
                numMoves++;
                rookMoves++;

                toSquareBitboard = Bit.clearBit(toSquareBitboard, toIndex);
            }

            selfBitboard = Bit.clearBit(selfBitboard, fromIndex); // pop lsb
        }
    }

    private static void generateLegalQueenMoves() {
        long toSquareBitboard;
        long selfBitboard = Bitboard.getBitboard(GameInfo.getTurn() + "Queen");
        while(Bit.hasNextBit(selfBitboard)) {
            int fromIndex = Bit.getNextBitIndex(selfBitboard); // get lsb
            byte flags;

            // queen is the bitwise or of rooks and bishops
            toSquareBitboard = CompactMagicBitboard.getRookMoves(fromIndex) | CompactMagicBitboard.getBishopMoves(fromIndex);
            toSquareBitboard &= ~Bitboard.getBitboard(GameInfo.getTurn());

            while(Bit.hasNextBit(toSquareBitboard)) {
                int toIndex = Bit.getNextBitIndex(toSquareBitboard);

                if(Bit.isSet(Bitboard.getBitboard(GameInfo.getOpponent()), toIndex)) {
                    flags = Bit.CAPTURE;
                    captures++;
                } else {
                    flags = Bit.QUIET;
                }

                legalMoveList.add(Move.createMove((byte) fromIndex, (byte) toIndex, flags));
                numMoves++;
                queenMoves++;

                toSquareBitboard = Bit.clearBit(toSquareBitboard, toIndex);
            }

            selfBitboard = Bit.clearBit(selfBitboard, fromIndex); // pop lsb
        }
    }

    private static void generateLegalKingMoves() {
        
    }

    private static void wipeMoveGeneration() {
        legalMoveList.clear();

        numMoves = 0;
        captures = 0;
        ep = 0;
        castles = 0;
        promotions = 0;
        checks = 0;
        discoveryChecks = 0;
        doubleChecks = 0;
        checkmates = 0;
        pawnMoves = 0;
        knightMoves = 0;
        bishopMoves = 0;
        rookMoves = 0;
        queenMoves = 0;
        kingMoves = 0;
    }
}
