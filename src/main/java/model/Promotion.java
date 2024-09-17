package model;

import helper.Bit;
import helper.Convert;
import view.board.BoardOverlayGraphic;
import view.board.PromotionGraphic;

public class Promotion {
    /*
     * Promotions are weird. There are two types of promotions: player promotions and algorithm promotions.
     * Algorithm promotions are pretty straightforward since we know what the selected move will be and
     * do not need to show a GUI of the algorithm picking their promotion type. 
     * 
     * Player promotions are not as easy. We can split promotions into two groups depending on whether they
     * are a capture promotion or not. Each group has four different outcomes (depending on which piece the
     * player picks from the GUI). We need to show the move being made and the opponent piece being captured,
     * but we also need to not double count moves. Additionally, we are in a "limbo" state until the player
     * makes a selection, meaning we don't know what type of move it will be, yet we still need to make a move.
     * Worse, if it is a promotion, we need to capture the piece, but this makes determining whether the 
     * promotion was a capture *after* the player makes a selection considerably difficult since all evidence
     * that an oppoent piece once occupied that square has been wiped from the capture. Not capturing the piece
     * leads to two pieces occuping the same square which breaks both visually and in the board representation.
     * 
     * The solution is to capture the piece if there is one to capture, place the pawn on the final rank, and
     * wait for the player to make a selection. This move is either a capture or a quiet, and will be pushed
     * to MoveRecord. When the player finally makes a selection, we can reference MoveRecord to see whether
     * the pre-promote move was a capture or not and then swap it out for the upgraded, proper piece-promotion
     * (possibly capture as well) move. The pawn is then updated to whatever piece the player selected and all
     * board data structures are updated using the provided move. Finally, the GUI is closed and control is 
     * turned over to the opponent (algorithm). 
     * 
     * Most of the logic for handling promotions can be found in the upgradeSimpleMove and updateWithMove 
     * methods in Move.java
     */

    public static void initPromotion(short move) {
        BoardOverlayGraphic.clearOverlayCanvas();
        GameInfo.setGameState("pre-promote");
        PromotionGraphic.setCardX(Convert.bitIndexToUV(Move.getToIndex(move)).getYAsInt());
        PromotionGraphic.drawPromotionGraphic();
    }

    public static short getMove(boolean isCapture) {
        switch(PromotionGraphic.getSection()) {
            case 0:
            // queen
            if(isCapture) {
                return Move.createMove(PlayerMoveInfo.getFromIndex(), PlayerMoveInfo.getToIndex(), Bit.QUEEN_PROMO_CAPTURE);
            } else {
                return Move.createMove(PlayerMoveInfo.getFromIndex(), PlayerMoveInfo.getToIndex(), Bit.QUEEN_PROMO);
            }

            case 1:
            // knight
            if(isCapture) {
                return Move.createMove(PlayerMoveInfo.getFromIndex(), PlayerMoveInfo.getToIndex(), Bit.KNIGHT_PROMO_CAPTURE);
            } else {
                return Move.createMove(PlayerMoveInfo.getFromIndex(), PlayerMoveInfo.getToIndex(), Bit.KNIGHT_PROMO);
            }

            case 2:
            // rook
            if(isCapture) {
                return Move.createMove(PlayerMoveInfo.getFromIndex(), PlayerMoveInfo.getToIndex(), Bit.ROOK_PROMO_CAPTURE);
            } else {
                return Move.createMove(PlayerMoveInfo.getFromIndex(), PlayerMoveInfo.getToIndex(), Bit.ROOK_PROMO);
            }

            case 3:
            // bishop
            if(isCapture) {
                return Move.createMove(PlayerMoveInfo.getFromIndex(), PlayerMoveInfo.getToIndex(), Bit.BISHOP_PROMO_CAPTURE);
            } else {
                return Move.createMove(PlayerMoveInfo.getFromIndex(), PlayerMoveInfo.getToIndex(), Bit.BISHOP_PROMO);
            }
        }
        
        return 0;
    }

    // for when the promotion is finished by choosing a piece from the promotion card
    public static void finishPromotion() {
        PromotionGraphic.resetPromotionGraphic();
        GameInfo.setGameState("play");
    }

    // for when the promotion is undone by clicking outside the promotion card
    public static void abortPromotion() {
        PromotionGraphic.resetPromotionGraphic();
        GameInfo.setGameState("play");
        PlayerMoveInfo.setPieceSelected("none");
    }
}
