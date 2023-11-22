package view.board;

import helper.Convert;
import helper.Vec2;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import model.GameInfo;
import model.Move;

public class BoardOverlayGraphic {
    private static final Color highlightColor = Color.rgb(10, 100, 10, 0.4);
    private static final Color moveColor = Color.rgb(215, 224, 59, 0.5);

    private static Canvas overlayCanvas = new Canvas(GameInfo.getBoardLength(), GameInfo.getBoardLength());
    private static GraphicsContext overlayContext = overlayCanvas.getGraphicsContext2D();

    private static int highlightIndex = -1;
    private static int clickCount = 0;

    private static Vec2 highlightCorner;
    private static Vec2 fromCorner;
    private static Vec2 toCorner;

    public static Canvas getBoardOverlayCanvas() {
        return overlayCanvas;
    }

    public static void drawHighlightSquare(double mouseX, double mouseY) {
        if(highlightIndex == Convert.mouseToBitIndex(mouseX, mouseY)) {
            clickCount++;
        } else {
            clickCount = 1;
        }

        highlightIndex = Convert.mouseToBitIndex(mouseX, mouseY);
        safeClear();
        highlightCorner = Convert.mouseToCorner(mouseX, mouseY);

        overlayContext.setFill(highlightColor);
        overlayContext.fillRect(highlightCorner.getXAsInt(), highlightCorner.getYAsInt(), GameInfo.getSquareLength(), GameInfo.getSquareLength());
    }

    public static void highlightMove(Move move) {
        clearOverlayCanvas();

        fromCorner = Convert.bitIndexToCorner(move.getFromIndex());
        toCorner = Convert.bitIndexToCorner(move.getToIndex());

        overlayContext.setFill(moveColor);
        overlayContext.fillRect(fromCorner.getXAsInt(), fromCorner.getYAsInt(), GameInfo.getSquareLength(), GameInfo.getSquareLength());
        overlayContext.fillRect(toCorner.getXAsInt(), toCorner.getYAsInt(), GameInfo.getSquareLength(), GameInfo.getSquareLength());
    }

    public static void updateOverlayCanvas(double mouseX, double mouseY) {
        if(clickCount % 2 == 0) {
            safeClear();
        }
    }

    public static void clearHighlightSquare() {
        if(highlightCorner == null) {
            return;
        }

        overlayContext.clearRect(
            highlightCorner.getXAsInt(), 
            highlightCorner.getYAsInt(), 
            GameInfo.getSquareLength(), 
            GameInfo.getSquareLength()
        );
    }

    public static void safeClear() {
        clearOverlayCanvas();
        highlightMove();
    }

    public static void resetOverlayCanvas() {
        safeClear();
        clickCount = 0;
    }

    public static void clearOverlayCanvas() {
        overlayContext.clearRect(0, 0, GameInfo.getBoardLength(), GameInfo.getBoardLength());
    }
    
    private static void highlightMove() {
        if(fromCorner == null || toCorner == null) {
            return;
        }

        overlayContext.setFill(moveColor);
        overlayContext.fillRect(fromCorner.getXAsInt(), fromCorner.getYAsInt(), GameInfo.getSquareLength(), GameInfo.getSquareLength());
        overlayContext.fillRect(toCorner.getXAsInt(), toCorner.getYAsInt(), GameInfo.getSquareLength(), GameInfo.getSquareLength());
    }
}
