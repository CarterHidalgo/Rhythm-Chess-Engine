package controller;

import model.GameInfo;
import model.Move;
import model.MoveRecord;
import model.Promotion;
import model.Bitboard;
import helper.Convert;
import helper.Debug;
import helper.Printer;
import helper.Vec2;
import view.board.BitboardGraphic;
import view.board.BoardGraphic;
import view.board.BoardOverlayGraphic;
import view.board.HighPieceGraphic;
import view.board.PromotionGraphic;

public class BoardMouseHandler {
    public static void handleMousePressed(double mouseX, double mouseY) {
        if(GameInfo.getGameState() == "promote") {
            return;
        }
        
        if(isOverHomePiece(mouseX, mouseY)) {
            int bitIndex = Convert.mouseToBitIndex(mouseX, mouseY);
            GameInfo.setPieceSelected(Bitboard.getKeyFromBitIndex(bitIndex));
            GameInfo.setFromIndex(bitIndex);
            BoardGraphic.pieceClickedByMouse(mouseX, mouseY);

            if(Debug.on("C1")) {                
                BitboardGraphic.drawBitboard(Bitboard.getBitboard(GameInfo.getPieceSelected()));
            }
        } else {
            BoardOverlayGraphic.resetOverlayCanvas();
        }
    }

    public static void handleMouseDragged(double mouseX, double mouseY) {
        if(!GameInfo.getPieceSelected().equals("none")) {
            HighPieceGraphic.updateWhenDragged(mouseX, mouseY);
        }
    }

    public static void handleMouseReleased(double mouseX, double mouseY) {
        if(GameInfo.getGameState() == "promote") {
            return;
        }

        BoardOverlayGraphic.updateOverlayCanvas(mouseX, mouseY);
        GameInfo.setToIndex(Convert.mouseToBitIndex(mouseX, mouseY));

        if(!GameInfo.getPieceSelected().equals("none")) {
            Move offeredMove = new Move(GameInfo.getPieceSelected(), GameInfo.getFromIndex(), GameInfo.getToIndex(), GameInfo.getSideToPlay());

            if(offeredMove.isValid()) {
                if(offeredMove.isPromotion()) {
                    Promotion.initPromotion(offeredMove);
                }

                Bitboard.updateWithMove(offeredMove);
                BoardOverlayGraphic.highlightMove(offeredMove);
                
                if(!offeredMove.isPromotion()) {
                    MoveRecord.pushMove(offeredMove);
                    GameInfo.nextTurn();
                }
            } else {
                if(GameInfo.getFromIndex() != GameInfo.getToIndex()) {
                    BoardOverlayGraphic.resetOverlayCanvas();
                }
            }
            
            BoardGraphic.drawBoardGraphicByBitboard();
            
            if(Debug.on("C1")) {
                BitboardGraphic.clearBitboard();
            }

            GameInfo.setPieceSelected("none");
        }
    }

    public static void handleMouseClicked(double mouseX, double mouseY) {
        if(GameInfo.getGameState() == "promote") {
            Promotion.handlePieceSelection();
        }
    }

    public static void handleMouseMoved(double mouseX, double mouseY) {
        if(GameInfo.getGameState() == "promote") {
            PromotionGraphic.handleMouseOverCard(mouseX, mouseY);
        }
    }

    private static boolean isOverHomePiece(double mouseX, double mouseY) {
        long pieceLocation = Bitboard.getBitboard((GameInfo.getTurn() == 0) ? "white" : "black");

        Vec2 uv = Convert.mouseToUV(mouseX, mouseY);
        int bitIndex = Convert.mouseToBitIndex(mouseX, mouseY);
        
        if(Debug.on("B1")) {
            Printer.printBitboard(pieceLocation, "pieceLocation");
        }
        
        if(Debug.on("B2")) {
            Printer.printBitboardAsLine(pieceLocation, "pieceLocation");
        }
        
        if(Debug.on("B3")) {
            Printer.print(" >> " + mouseX + " " + mouseY);
        }
        
        if(Debug.on("B4")) {
            Printer.print(" >> " + uv.getXAsInt() + " " + uv.getYAsInt());
        }
        
        if(Debug.on("B5")) {
            Printer.print(" >> " + bitIndex);
        }
        
        if((pieceLocation & (1L << bitIndex)) != 0) {
            if(Debug.on("B6")) {
                Printer.print(" >> This square holds a home color piece");
            }

            return true;
        }
        
        return false;
    }
}
