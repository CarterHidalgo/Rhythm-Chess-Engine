package controller;

import model.GameInfo;
import model.PlayerMoveInfo;
import model.BoardLookup;
import model.Move;
import model.MoveGeneration;
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
    public static void handleMousePressed(float mouseX, float mouseY) {
        if(GameInfo.getGameState() == "promote") {
            return;
        }
        
        if(isOverSelfPiece(mouseX, mouseY)) {
            byte bitIndex = Convert.mouseToBitIndex(mouseX, mouseY);

            PlayerMoveInfo.setPieceSelected(BoardLookup.getPieceByBitIndex(bitIndex));
            PlayerMoveInfo.setFromIndex(bitIndex);
            BoardGraphic.pieceClickedByMouse(mouseX, mouseY);

            if(Debug.on("C1")) {
                BitboardGraphic.drawBitboardGraphic(Bitboard.getBitboard(PlayerMoveInfo.getPieceSelected()), BitboardGraphic.bitboardColor);
            }

            if(Debug.on("C4")) {
                BitboardGraphic.drawBitboardGraphic(MoveGeneration.getMoveBitboard(PlayerMoveInfo.getFromIndex()), BitboardGraphic.movesColor);
            }
        } else {
            BoardOverlayGraphic.resetOverlayCanvas();
        }
    }

    public static void handleMouseDragged(float mouseX, float mouseY) {
        if(!PlayerMoveInfo.getPieceSelected().equals("empty")) {
            HighPieceGraphic.updateWhenDragged(mouseX, mouseY);
        }
    }

    public static void handleMouseReleased(float mouseX, float mouseY) {
        if(GameInfo.getGameState() == "promote") {
            return;
        }

        BoardOverlayGraphic.updateOverlayCanvas(mouseX, mouseY);
        PlayerMoveInfo.setToIndex(Convert.mouseToBitIndex(mouseX, mouseY));

        if(!PlayerMoveInfo.getPieceSelected().equals("empty")) {
            short offeredMove = Move.createSimpleMove(PlayerMoveInfo.getFromIndex(), PlayerMoveInfo.getToIndex());
            short upgradedMove = Move.upgradeSimpleMove(offeredMove);

            if(Move.isLegalUpgradedMove(upgradedMove)) {
                Move.updateWithMove(upgradedMove);
            } else if(PlayerMoveInfo.getFromIndex() != PlayerMoveInfo.getToIndex()) {
                BoardOverlayGraphic.resetOverlayCanvas();
            }

            /*
             * In either case the piece has been released and the board must be either
             * reset or updated by drawing according to bitboards
             */
            BoardGraphic.drawBoardGraphicByBitboard();
            PlayerMoveInfo.setPieceSelected("empty");

            if(Debug.on("C1") || Debug.on("C4")) {
                BitboardGraphic.clearBitboardGraphic();
            }
        }
    }

    public static void handleMouseClicked(float mouseX, float mouseY) {
        if(GameInfo.getGameState() == "promote") {
            Promotion.handlePieceSelection();
        }
    }

    public static void handleMouseMoved(float mouseX, float mouseY) {
        if(GameInfo.getGameState() == "promote") {
            PromotionGraphic.handleMouseOverCard(mouseX, mouseY);
        }
    }

    private static boolean isOverSelfPiece(float mouseX, float mouseY) {
        long pieceLocation = Bitboard.getBitboard(GameInfo.getTurn());
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
            Vec2 uv = Convert.mouseToUV(mouseX, mouseY);
            Printer.print(" >> " + uv.getXAsInt() + " " + uv.getYAsInt());
        }
        
        if(Debug.on("B5")) {
            Printer.print(" >> " + bitIndex);
        }
        
        if((pieceLocation & (1L << bitIndex)) != 0) {
            if(Debug.on("B6")) {
                Printer.print(" >> This square holds a self color piece");
            }

            return true;
        }
        
        return false;
    }
}
