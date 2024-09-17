package controller;

import helper.Convert;
import helper.Debug;
import helper.Printer;
import helper.Vec2;
import model.Bitboard;
import model.BoardLookup;
import model.GameInfo;
import model.Move;
import model.MoveGeneration;
import model.PlayerMoveInfo;
import view.board.BitboardGraphic;
import view.board.BoardGraphic;
import view.board.BoardOverlayGraphic;
import view.board.HighPieceGraphic;
import view.board.PromotionGraphic;

public class BoardMouseHandler {
    public static void handleMousePressed(float mouseX, float mouseY) {
        // promotion events are handled in Promotion.java and PromotionGraphic.java
        if(GameInfo.stateIs("pre-promote")) {
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
        if(GameInfo.stateIs("pre-promote")) {
            return;
        }

        if(!PlayerMoveInfo.getPieceSelected().equals("empty")) {
            HighPieceGraphic.updateWhenDragged(mouseX, mouseY);
        }
    }

    public static void handleMouseReleased(float mouseX, float mouseY) {
        // do not update board overlay graphic or last player move info when in promotion-limbo
        if(!GameInfo.stateIs("pre-promote")) {
            BoardOverlayGraphic.updateOverlayCanvas(mouseX, mouseY);
            PlayerMoveInfo.setToIndex(Convert.mouseToBitIndex(mouseX, mouseY));
        }
        
        // we can simply check for non-emptiness because opponent pieces are filtered out in handleMousePressed
        if(!PlayerMoveInfo.getPieceSelected().equals("empty")) {
            /*
            * We are already calculating flags for moves in the MoveGeneration. To avoid
            * re-calculating flags when we create an offeredMove from the player we 
            * instead create a "simple move" which is a move with the 0000 flag. We then
            * "upgrade" this move by searching through the already calculated legal move
            * list in MoveGeneration and see if there is a matching move (excepting the flags).
            * If there is, we "upgrade" the offered simple move by returning the full move
            * from the legal move list. If not, we return the "null" move (16 zeros).
            */
            
            short offeredMove = Move.createSimpleMove(PlayerMoveInfo.getFromIndex(), PlayerMoveInfo.getToIndex());
            short upgradedMove = Move.upgradeSimpleMove(offeredMove);
            
            if(Move.isLegalUpgradedMove(upgradedMove)) {
                Move.updateWithMove(upgradedMove);
            } else if(PlayerMoveInfo.getFromIndex() != PlayerMoveInfo.getToIndex()) {
                BoardOverlayGraphic.resetOverlayCanvas();
            }

            /*
             * In either case the piece has been released and the board must be either
             * reset or updated by re-drawing
             */
            BoardGraphic.drawBoardGraphic();
            if(!GameInfo.stateIs("pre-promote")) {
                PlayerMoveInfo.setPieceSelected("empty");
            }

            if(Debug.on("C1") || Debug.on("C4")) {
                BitboardGraphic.clearBitboardGraphic();
            }
        }
    }

    public static void handleMouseClicked(float mouseX, float mouseY) {
        // implement on an as-needed basis
    }

    public static void handleMouseMoved(float mouseX, float mouseY) {
        if(GameInfo.stateIs("pre-promote")) {
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
