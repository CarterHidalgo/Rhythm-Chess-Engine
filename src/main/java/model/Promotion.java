package model;

import helper.Convert;
import view.board.BoardOverlayGraphic;
import view.board.PromotionGraphic;

public class Promotion {
    private static boolean ignoreInitial = true;
    private static short promotionMove;

    public static void initPromotion(short move) {
        promotionMove = move;
        BoardOverlayGraphic.clearOverlayCanvas();
        GameInfo.setGameState("promote");
        PromotionGraphic.setCardX(Convert.bitIndexToUV(Move.getToIndex(move)).getYAsInt());
        PromotionGraphic.drawPromotionGraphic();
    }

    public static void handlePieceSelection() {
        if(ignoreInitial) {
            ignoreInitial = false;
            return;
        }
        
        if(PromotionGraphic.getSection() > -1) {
            // promotionMove.setPromotionSelected(PromotionGraphic.getSection());
        } else {
            // promotionMove.setPromotionSelected(-1); // undo the promotion
        }

        Bitboard.updateWithMove(promotionMove);
    }

    public static void endPromotion() {
        PromotionGraphic.resetPromotionGraphic();
        // MoveRecord.pushMove(promotionMove);
        GameInfo.setGameState("play");
        // GameInfo.setPieceSelected("none");
        GameInfo.flipMove();

        ignoreInitial = true;
        // promotionMove = null;
    }

    public static void terminatePromotion() {
        PromotionGraphic.resetPromotionGraphic();
        GameInfo.setGameState("play");
        // GameInfo.setPieceSelected("none");

        ignoreInitial = true;
        // promotionMove = null;
    }
}
