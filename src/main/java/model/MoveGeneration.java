package model;

import java.util.ArrayList;

import helper.Bit;
import helper.Debug;
import helper.Offset;
import helper.Printer;
import helper.Timer;
import helper.Xray;
import model.attacks.KingAttacks;
import model.attacks.KnightAttacks;
import model.magic.CompactMagicBitboard;

public class MoveGeneration {
    private static ArrayList<Short> legalMoveList = new ArrayList<>();
    private static long opponentProtected = 0, opponentPinners = 0, opponentCheckers = 0, pinnedPieces = 0, checkMask = 0, kingUnsafe = 0;
    private static int numMoves = 0, captures = 0, ep = 0, castles = 0, promotions = 0, checks = 0, discoveryChecks = 0, doubleChecks = 0, checkmates = 0;
    private static int pawnMoves = 0, knightMoves = 0, bishopMoves = 0, rookMoves = 0, queenMoves = 0, kingMoves = 0;

    public static void generateLegalMoves() {
        wipeMoveGeneration();

        // Generates a bitboard of all squares "controlled" by the opponent
        // Printer.printBitboard(opponentProtected, "prelim");
        generatePawnProtection();
        // Printer.printBitboard(opponentProtected, "pawns");
        generateKnightProtection();
        // Printer.printBitboard(opponentProtected, "knights");
        generateBishopProtection();
        // Printer.printBitboard(opponentProtected, "bishops");
        generateRookProtection();
        // Printer.printBitboard(opponentProtected, "rooks");
        generateQueenProtection();
        // Printer.printBitboard(opponentProtected, "queens");

        // create move limiters based on pins and checks
        calculatePinnedPieces();
        // Printer.printBitboard(opponentProtected, "pin");
        calculateCheckMask();
        // Printer.printBitboard(opponentProtected, "checks");

        if(Debug.on("E3")) {
            Timer.start("generateLegalMoves", "nano");
        }
        
        // generate all legal moves for self
        generateLegalKingMoves();
        generateLegalPawnMoves();
        generateLegalKnightMoves();
        generateLegalBishopMoves();
        generateLegalRookMoves();
        generateLegalQueenMoves();
        
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
            // System.out.println("----------------------");
            // System.out.println(captures + " captures");
            // System.out.println(ep + " " + "ep");
            // System.out.println(castles + " castles");
            // System.out.println(promotions + " promotions");
            // System.out.println(checks + " checks");
            // System.out.println(discoveryChecks + " discoveryChecks");
            // System.out.println(doubleChecks + " doubleChecks");
            // System.out.println(checkmates + " checkmates");
        }
        
        if(Debug.on("D2")) {
            System.out.println();
            for(short move : legalMoveList) {
                Move.printIndexed(move);
            }
        }
        
        if(Debug.on("D3")) {
            System.out.println();
            Move.printAlgebraic(legalMoveList);
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
         *   Promotion + Promotion Captures ✓
         *   Pins ✓
         *   Checks
         * 
         *  Note: Promotions to different pieces count as unique moves when counting for PERFT. A promotion to a queen 
         *      is not the same move as promotion to a knight
         */

        long selfBitboard = Bitboard.getBitboard(GameInfo.getTurn() + "Pawn");
        long emptyBitboard = Bitboard.getBitboard("empty");
        long pinMask = 0;
        int fromIndex, forward, leftForwardDiagonal, rightForwardDiagonal;

        while(Bit.hasNextBit(selfBitboard)) {
            fromIndex = Long.numberOfTrailingZeros(selfBitboard);
            forward = Offset.forward(fromIndex);
            pinMask = 0;

            // pin mask
            // mask the moves if the piece is pinned
            if(Bit.isSet(pinnedPieces, fromIndex)) {
                pinMask = Bitboard.getLineMask(fromIndex, KingAttacks.getSelfKingIndex());
            }

            // forward
            if(
                Bit.isSet(emptyBitboard, forward) && // forward is empty
                ((pinMask != 0) ? (Bit.isSet(pinMask, forward)) : (true)) // if pinMask is in effect, does it include forward? : otherwise true
            ) {
                // forward
                if(((checkMask != 0)) ? (Bit.isSet(checkMask, forward)) : (true)) {
                    if(Offset.isRelativePromotion(forward)) {
                        // forward promotion 
                        legalMoveList.add(Move.createMove((byte) fromIndex, (byte) forward, Bit.QUEEN_PROMO));
                        legalMoveList.add(Move.createMove((byte) fromIndex, (byte) forward, Bit.ROOK_PROMO));
                        legalMoveList.add(Move.createMove((byte) fromIndex, (byte) forward, Bit.BISHOP_PROMO));
                        legalMoveList.add(Move.createMove((byte) fromIndex, (byte) forward, Bit.KNIGHT_PROMO));
                        
                        // there are always 4 legal promotions (although one or more may lead to stalemate)
                        numMoves += 4;
                        pawnMoves += 4;
                        promotions += 4;
                    } else {
                        // forward quiet
                        legalMoveList.add(Move.createMove((byte) fromIndex, (byte) forward, Bit.QUIET));
                        numMoves++;
                        pawnMoves++;
                    }
                }
                
                // double forward
                if(
                    Bit.isSet(Bitboard.getBitboard(GameInfo.getTurn() + "PawnStart"), fromIndex) && // on a starting square
                    Bit.isSet(emptyBitboard, Offset.doubleForward(fromIndex)) && // double forward is empty
                    ((checkMask != 0) ? (Bit.isSet(checkMask, Offset.doubleForward(fromIndex))) : (true))
                ) {
                    legalMoveList.add(Move.createMove((byte) fromIndex, (byte) Offset.doubleForward(fromIndex), Bit.DOUBLE_PAWN));
                    
                    numMoves++;
                    pawnMoves++;
                }
            }

            leftForwardDiagonal = Offset.leftForwardDiagonal(fromIndex);
            rightForwardDiagonal = Offset.rightForwardDiagonal(fromIndex);

            // left diagonal capture
            if(Offset.isNotRelativeLeftEdge(fromIndex) && ((pinMask != 0) ? (Bit.isSet(pinMask, leftForwardDiagonal)) : (true))) {
                if(Bit.isSet(Bitboard.getBitboard(GameInfo.getOpponent()), leftForwardDiagonal)) {
                    if(Offset.isRelativePromotion(leftForwardDiagonal)) {
                        // left diagonal capture promotion
                        legalMoveList.add(Move.createMove((byte) fromIndex, (byte) leftForwardDiagonal, Bit.QUEEN_PROMO_CAPTURE));
                        legalMoveList.add(Move.createMove((byte) fromIndex, (byte) leftForwardDiagonal, Bit.ROOK_PROMO_CAPTURE));
                        legalMoveList.add(Move.createMove((byte) fromIndex, (byte) leftForwardDiagonal, Bit.BISHOP_PROMO_CAPTURE));
                        legalMoveList.add(Move.createMove((byte) fromIndex, (byte) leftForwardDiagonal, Bit.KNIGHT_PROMO_CAPTURE));

                        // there are always 4 legal promotions (although one or more may lead to stalemate)
                        numMoves += 4;
                        pawnMoves += 4;
                        captures += 4;
                        promotions += 4;
                    } else {
                        // left diagonal capture
                        legalMoveList.add(Move.createMove((byte) fromIndex, (byte) leftForwardDiagonal, Bit.CAPTURE));
                        numMoves++;
                        pawnMoves++;
                        captures++;
                    }
                    
                }

                if(Bit.isSet(Bitboard.getBitboard("ep"), leftForwardDiagonal)) {
                    // left diagonal ep capture
                    legalMoveList.add(Move.createMove((byte) fromIndex, (byte) leftForwardDiagonal, Bit.EP_CAPTURE));
                    
                    numMoves++;
                    pawnMoves++;
                    captures++;
                    ep++;
                }
            }

            // right diagonal capture
            if(Offset.isNotRelativeRightEdge(fromIndex) && ((pinMask != 0) ? (Bit.isSet(pinMask, rightForwardDiagonal)) : (true))) {
                if(Bit.isSet(Bitboard.getBitboard(GameInfo.getOpponent()), rightForwardDiagonal)) {
                    if(Offset.isRelativePromotion(rightForwardDiagonal)) {
                        // right diagonal capture promotion
                        legalMoveList.add(Move.createMove((byte) fromIndex, (byte) rightForwardDiagonal, Bit.QUEEN_PROMO_CAPTURE));
                        legalMoveList.add(Move.createMove((byte) fromIndex, (byte) rightForwardDiagonal, Bit.ROOK_PROMO_CAPTURE));
                        legalMoveList.add(Move.createMove((byte) fromIndex, (byte) rightForwardDiagonal, Bit.BISHOP_PROMO_CAPTURE));
                        legalMoveList.add(Move.createMove((byte) fromIndex, (byte) rightForwardDiagonal, Bit.KNIGHT_PROMO_CAPTURE));

                        numMoves += 4;
                        pawnMoves += 4;
                        captures += 4;
                        promotions += 4;
                    } else {
                        // right diagonal capture
                        legalMoveList.add(Move.createMove((byte) fromIndex, (byte) rightForwardDiagonal, Bit.CAPTURE));
                        numMoves++;
                        pawnMoves++;
                        captures++;
                    }
                    
                }

                if(Bit.isSet(Bitboard.getBitboard("ep"), rightForwardDiagonal)) {
                    // right diagonal ep capture
                    legalMoveList.add(Move.createMove((byte) fromIndex, (byte) rightForwardDiagonal, Bit.EP_CAPTURE));
                    
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

            toSquareBitboard = KnightAttacks.getKnightAttacks(fromIndex);
            toSquareBitboard &= ~Bitboard.getBitboard(GameInfo.getTurn());

            // mask moves if the piece is pinned
            if(Bit.isSet(pinnedPieces, fromIndex)) {
                long pinMask = Bitboard.getLineMask(fromIndex, KingAttacks.getSelfKingIndex());

                toSquareBitboard &= pinMask;
            }

            if(KingAttacks.isInCheck(GameInfo.getTurn())) {
                toSquareBitboard &= checkMask;
            }
    
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
            int fromIndex = Bit.getNextBitIndex(selfBitboard);
            byte flags;

            toSquareBitboard = CompactMagicBitboard.getBishopMoves(fromIndex);
            toSquareBitboard &= ~Bitboard.getBitboard(GameInfo.getTurn());

            // mask moves if the piece is pinned
            if(Bit.isSet(pinnedPieces, fromIndex)) {
                long pinMask = Bitboard.getLineMask(fromIndex, KingAttacks.getSelfKingIndex());

                toSquareBitboard &= pinMask;
            }
            
            if(KingAttacks.isInCheck(GameInfo.getTurn())) {
                toSquareBitboard &= checkMask;
            }

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

            selfBitboard = Bit.clearBit(selfBitboard, fromIndex); 
        }
    }

    private static void generateLegalRookMoves() {
        long toSquareBitboard;
        long selfBitboard = Bitboard.getBitboard(GameInfo.getTurn() + "Rook");
        while(Bit.hasNextBit(selfBitboard)) {
            int fromIndex = Bit.getNextBitIndex(selfBitboard); 
            byte flags;

            toSquareBitboard = CompactMagicBitboard.getRookMoves(fromIndex);
            toSquareBitboard &= ~Bitboard.getBitboard(GameInfo.getTurn());

            if(Bit.isSet(pinnedPieces, fromIndex)) {
                long pinMask = Bitboard.getLineMask(fromIndex, KingAttacks.getSelfKingIndex());
                toSquareBitboard &= pinMask;
            }

            if(KingAttacks.isInCheck(GameInfo.getTurn())) {
                toSquareBitboard &= checkMask;
            }

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

            selfBitboard = Bit.clearBit(selfBitboard, fromIndex); 
        }
    }

    private static void generateLegalQueenMoves() {
        long toSquareBitboard;
        long selfBitboard = Bitboard.getBitboard(GameInfo.getTurn() + "Queen");
        while(Bit.hasNextBit(selfBitboard)) {
            int fromIndex = Bit.getNextBitIndex(selfBitboard);
            byte flags;

            toSquareBitboard = CompactMagicBitboard.getQueenMoves(fromIndex);
            toSquareBitboard &= ~Bitboard.getBitboard(GameInfo.getTurn());

            if(Bit.isSet(pinnedPieces, fromIndex)) {
                long pinMask = Bitboard.getLineMask(fromIndex, KingAttacks.getSelfKingIndex());
                toSquareBitboard &= pinMask;
            }

            if(KingAttacks.isInCheck(GameInfo.getTurn())) {
                toSquareBitboard &= checkMask;
            }

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

            selfBitboard = Bit.clearBit(selfBitboard, fromIndex); 
        }
    }

    private static void generateLegalKingMoves() {
        long toSquareBitboard;
        int fromIndex = KingAttacks.getSelfKingIndex();
        byte flags;

        toSquareBitboard = KingAttacks.getKingAttacks(fromIndex);
        toSquareBitboard &= ~Bitboard.getBitboard(GameInfo.getTurn());
        toSquareBitboard &= ~opponentProtected;
        toSquareBitboard &= ~kingUnsafe;

        // normal 8 "ring" moves
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
            kingMoves++;

            toSquareBitboard = Bit.clearBit(toSquareBitboard, toIndex);
        }

        // kingside castle
        if(canCastleKingside()) {
            flags = Bit.KING_CASTLE;
            legalMoveList.add(Move.createMove((byte) fromIndex, (byte) (fromIndex + 2), flags));
            numMoves++;
            kingMoves++;
        }
            
        // queenside castle
        if(canCastleQueenside()) {
            flags = Bit.QUEEN_CASTLE;
            legalMoveList.add(Move.createMove((byte) fromIndex, (byte) (fromIndex - 2), flags));
            numMoves++;
            kingMoves++;
        }
    }

    private static void generatePawnProtection() {
        long opponentBitboard = Bitboard.getBitboard(GameInfo.getOppositeTurn() + "Pawn");
        int fromIndex, leftForwardDiagonal, rightForwardDiagonal;

        while(opponentBitboard != 0) {
            fromIndex = Long.numberOfTrailingZeros(opponentBitboard);
            leftForwardDiagonal = Offset.opponentLeftForwardDiagonal(fromIndex);
            rightForwardDiagonal = Offset.opponentRightForwardDiagonal(fromIndex);
            
            if(Offset.opponentIsNotRelativeLeftEdge(fromIndex)) {
                opponentProtected = Bit.setBit(opponentProtected, leftForwardDiagonal);
            }

            if(Offset.opponentIsNotRelativeRightEdge(fromIndex)) {
                opponentProtected = Bit.setBit(opponentProtected, rightForwardDiagonal);
            }

            if(leftForwardDiagonal == KingAttacks.getSelfKingIndex() || rightForwardDiagonal == KingAttacks.getSelfKingIndex()) {
                opponentCheckers = Bit.setBit(opponentCheckers, fromIndex);
            }

            opponentBitboard &= (opponentBitboard - 1);
        }
    }

    private static void generateKnightProtection() {
        long toSquareBitboard;
        long opponentBitboard = Bitboard.getBitboard(GameInfo.getOppositeTurn() + "Knight");
        
        while(Bit.hasNextBit(opponentBitboard)) {
            int fromIndex = Bit.getNextBitIndex(opponentBitboard);

            toSquareBitboard = KnightAttacks.getKnightAttacks(fromIndex);
            opponentProtected |= toSquareBitboard;    

            if(Bit.isSet(toSquareBitboard, KingAttacks.getSelfKingIndex())) {
                opponentCheckers = Bit.setBit(opponentCheckers, fromIndex);
            }

            opponentBitboard = Bit.clearBit(opponentBitboard, fromIndex);
        }
    }

    private static void generateBishopProtection() {
        long toSquareBitboard;
        long opponentBitboard = Bitboard.getBitboard(GameInfo.getOppositeTurn() + "Bishop");

        while(Bit.hasNextBit(opponentBitboard)) {
            int fromIndex = Bit.getNextBitIndex(opponentBitboard); 
            
            toSquareBitboard = CompactMagicBitboard.getBishopMoves(fromIndex);
            opponentProtected |= toSquareBitboard;

            if(Bit.isSet(toSquareBitboard, KingAttacks.getSelfKingIndex())) {
                opponentCheckers = Bit.setBit(opponentCheckers, fromIndex);
            }

            if(Bit.isSet(Xray.get(KingAttacks.getSelfKingIndex()), fromIndex)) {
                opponentPinners = Bit.setBit(opponentPinners, fromIndex);
            }
            
            opponentBitboard = Bit.clearBit(opponentBitboard, fromIndex); 
        }
    }

    private static void generateRookProtection() {
        long toSquareBitboard;
        long opponentBitboard = Bitboard.getBitboard(GameInfo.getOppositeTurn() + "Rook");

        while(Bit.hasNextBit(opponentBitboard)) {
            int fromIndex = Bit.getNextBitIndex(opponentBitboard);
            toSquareBitboard = CompactMagicBitboard.getRookMoves(fromIndex);
            opponentProtected |= toSquareBitboard;

            if(Bit.isSet(toSquareBitboard, KingAttacks.getSelfKingIndex())) {
                opponentCheckers = Bit.setBit(opponentCheckers, fromIndex);
            }

            if(Bit.isSet(Xray.get(KingAttacks.getSelfKingIndex()), fromIndex)) {
                opponentPinners = Bit.setBit(opponentPinners, fromIndex);
            }

            opponentBitboard = Bit.clearBit(opponentBitboard, fromIndex); 
        }
    }

    private static void generateQueenProtection() {
        long toSquareBitboard;
        long opponentBitboard = Bitboard.getBitboard(GameInfo.getOppositeTurn() + "Queen");

        while(Bit.hasNextBit(opponentBitboard)) {
            int fromIndex = Bit.getNextBitIndex(opponentBitboard); 
            
            toSquareBitboard = CompactMagicBitboard.getQueenMoves(fromIndex);
            opponentProtected |= toSquareBitboard;

            if(Bit.isSet(toSquareBitboard, KingAttacks.getSelfKingIndex())) {
                opponentCheckers = Bit.setBit(opponentCheckers, fromIndex);
            }

            if(Bit.isSet(Xray.get(KingAttacks.getSelfKingIndex()), fromIndex)) {
                opponentPinners = Bit.setBit(opponentPinners, fromIndex);
            }

            opponentBitboard = Bit.clearBit(opponentBitboard, fromIndex); 
        }
    }

    private static void generateKingProtection() {
        
    }

    private static void calculatePinnedPieces() {
        /*
         * Pins are rather annoying to calculate. This explains how I calculate which pieces are pinned. =]
         * 
         * There are two concepts that need to be distinguished: Xray and Super. The distinction is important for 
         * understanding how we deal with pins and seeing through pieces when we need to. The Xray attack set as
         * defined in Xray.java extends in all linear directions to the edge of the board. It is essentially the 
         * queen if the queen was the only piece on the board. This allows us to determine if a piece is "in line"
         * with another piece which is useful for "seeing through" pieces to calculate potential pins. Conversely, 
         * Super is the actual queen attack set and (the bitwise or of rook and bishop attack sets) and will stop
         * at blockers (see CompactMagicBitboards for details). Thus references to the "superKing" are simply 
         * references to the king if the king had the same attack set as the queen.
         * 
         * The normal pinned piece setup involves an opponent ray piece and a super king both attacking a self piece.
         * This obviously does not consider the chance that the attacked piece is "in between" the opponent ray piece
         * and the super king. The way we fix this is by storing all potential opponent pinners. We then loop through 
         * them one at a time and access a pre-computed mask that draws a line through both squares to the edge of the
         * board. If all conditions hold (superKing and opponentRay both attack the same piece, that piece is a self
         * piece, and that piece falls in the linear mask) the piece is pinned and is added to the pinnedPieces bitboard. 
         * 
         * NOTE: checkMask must be global to the class since it applies to all pieces. If the king is in check then making
         * a move that does not address the check is illegal. However, pinMask must be local (and so calculated at the 
         * moment of use when generating moves) because every pin mask is unique since they are all radial to the king. 
         * 
         * NOTE: This approach does not consider en-passant pins or other potential edge cases which have to be handled 
         * specially.
         * 
         * Case EP:
         *   EP is handled by "removing" (clearing) the bit of the ep pawn (the pawn that has just moved double forward)
         *   By clearing this it effectively blinds the CompactMagicBitboard process from the fact that the ep pawn exists
         *   thus allowing it to "x-ray" through the ep pawn to any pieces "behind" it (from the kings perspective). We can
         *   then proceed with absolute pin checks as normal. After we are done, we put the ep pawn "back" by setting
         *   the occupied bitboard bit back. No other bitboards are changed in the process. 
         */

        pinnedPieces = 0;
        long superKing;
        
        // calculate super king with ep-handling
        if(Bitboard.getBitboard("ep") != 0) {
            int epPawnIndex = Offset.behind(Bit.getNextBitIndex(Bitboard.getBitboard("ep")));
            
            // remove ep pawn from occupied
            Bitboard.hide("occupied", epPawnIndex);
            
            // calculate super king with "x-ray" vision through the ep pawn (still stops at all non-ep pawn blockers)
            superKing = CompactMagicBitboard.getQueenMoves(KingAttacks.getSelfKingIndex());
            
            // put ep pawn back
            Bitboard.restore("occupied", epPawnIndex);
        } else {
            superKing = CompactMagicBitboard.getQueenMoves(KingAttacks.getSelfKingIndex());
        }

        while(Bit.hasNextBit(opponentPinners)) {
            int index = Bit.getNextBitIndex(opponentPinners);

            long superPinner;
            long pinLine = Bitboard.getLineMask(index, KingAttacks.getSelfKingIndex());

            if(BoardLookup.getPieceByBitIndex((byte) index).equals(GameInfo.getOpponent() + "Rook")) {
                superPinner = CompactMagicBitboard.getRookMoves(index);
            } else if(BoardLookup.getPieceByBitIndex((byte) index).equals(GameInfo.getOpponent() + "Bishop")) {
                superPinner = CompactMagicBitboard.getBishopMoves(index);
            } else {
                superPinner = CompactMagicBitboard.getQueenMoves(index);
            }

            long pinResult = (superKing & pinLine & superPinner & Bitboard.getBitboard(GameInfo.getTurn()));

            // check to make sure we aren't over-pinning in the case of ep
            if(Bitboard.getBitboard("ep") != 0) {
                if(Long.bitCount(pinResult) > 1) {
                    System.out.println("WARNING: Multiple pinned pieces when only 1 should exist. See calculatePinnedPieces() -> MoveGeneration.java; Shutting down");
                    System.exit(1);
                }
                
                int pinnedIndex = Bit.getNextBitIndex(pinResult);
                int epPawnIndex = Offset.behind(Bit.getNextBitIndex(Bitboard.getBitboard("ep")));

                if(Bit.isSet(pinLine, epPawnIndex) && !BoardLookup.getPieceByBitIndex((byte) pinnedIndex).equals(GameInfo.getTurn() + "Pawn")) {
                    pinResult = Bit.clearBit(pinResult, pinnedIndex);
                }

            }

            pinnedPieces |= pinResult;

            opponentPinners = Bit.clearBit(opponentPinners, index);
        }



        if(Debug.on("F1")) {
            if(pinnedPieces != 0) {
                Printer.printBitboard(pinnedPieces, "Pinned pieces");
            } else {
                System.out.println("\nNo absolute pins");
            }
        }
    }

    private static void calculateCheckMask() {
        if(opponentCheckers != 0) {
            KingAttacks.setCheck(GameInfo.getTurn());

            // initially all bits are 1; we then eliminate some every iteration of the while loop with & operations
            checkMask = -1L;
            kingUnsafe = opponentProtected;

            Printer.printBitboard(kingUnsafe, "kingUnsafe-pre");

            while(Bit.hasNextBit(opponentCheckers)) {
                int index = Bit.getNextBitIndex(opponentCheckers);
                long checkLine = Bitboard.getLineMask(index, KingAttacks.getSelfKingIndex());
                
                if(checkLine != 0) {
                    // pawn, bishop, rook, queen check
                    long superChecker = CompactMagicBitboard.getQueenMoves(index) | (1L << index);
                    long superKing = CompactMagicBitboard.getQueenMoves(KingAttacks.getSelfKingIndex());

                    checkMask &= (checkLine & superKing & superChecker);

                    // ray pieces can make squares through the king "unsafe" even if those squares are not "protected"
                    if(!BoardLookup.getPieceByBitIndex((byte) index).equals(GameInfo.getOpponent() + "Pawn")) {
                        Bitboard.hide("occupied", KingAttacks.getSelfKingIndex());
                        kingUnsafe |= (CompactMagicBitboard.getQueenMoves(index) & KingAttacks.getKingAttacks(KingAttacks.getSelfKingIndex()) & checkLine);
                        Bitboard.restore("occupied", KingAttacks.getSelfKingIndex());

                        Printer.printBitboard(kingUnsafe, "kingUnsafe");
                    }
                } else {
                    /*
                     * Knight check: Since the knight can jump over pieces it cannot be blocked. Thus if a knight
                     * check occurs, the only options are to move the king or capture the knight. If a bitwise and
                     * on checkMask (with only the knight square set) leaves checkMask = 0 then there are not 
                     * non-King moves that can be made and the king must either be moved or declare checkmate. 
                     */

                    checkMask &= (1L << index);

                    if(checkMask == 0) {
                        break;
                    }
                }

                opponentCheckers = Bit.clearBit(opponentCheckers, index);
            }

            Printer.printBitboard(kingUnsafe, "kingUnsafe-post");
        }
    }

    private static boolean canCastleKingside() {
        int offset = KingAttacks.getSelfKingIndex() + 3;

        return  (offset > 63) ? false : 
                KingAttacks.hasCastleRights(GameInfo.getTurn(), 0) && // has kingside castling rights (king has not moved)
                !KingAttacks.isInCheck(GameInfo.getTurn()) && // king is not in check
                BoardLookup.getPieceByBitIndex((byte) offset).equals(GameInfo.getTurn() + "Rook") && // self rook is in corner
                (Bitboard.getBitboard(GameInfo.getTurn() + "Kingside") & opponentProtected) == 0 && // castling squares are not in check
                (Bitboard.getBitboard(GameInfo.getTurn() + "Kingside") & Bitboard.getBitboard("occupied")) == 0; // castling squares are not occupied
    }

    private static boolean canCastleQueenside() {
        int offset = KingAttacks.getSelfKingIndex() - 4;

        return  (offset < 0) ? false :
                KingAttacks.hasCastleRights(GameInfo.getTurn(), 1) && // has queenside castling rights (king has not moved)
                !KingAttacks.isInCheck(GameInfo.getTurn()) && // king is not in check
                BoardLookup.getPieceByBitIndex((byte) offset).equals(GameInfo.getTurn() + "Rook") && // self rook is in corner
                (Bitboard.getBitboard(GameInfo.getTurn() + "Queenside") & opponentProtected) == 0 && // castling squares are not in check
                (Bitboard.getBitboard(GameInfo.getTurn() + "Queenside") & Bitboard.getBitboard("occupied")) == 0; // castling squares are not occupied
    }

    private static void wipeMoveGeneration() {
        legalMoveList.clear();
        KingAttacks.clearCheck(GameInfo.getOppositeTurn());

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

        opponentProtected = 0;
        opponentCheckers = 0;
        opponentPinners = 0;
        pinnedPieces = 0;
        checkMask = 0;
        kingUnsafe = 0;
    }
}
