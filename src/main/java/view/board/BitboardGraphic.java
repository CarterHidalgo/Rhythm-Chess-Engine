package view.board;

import java.util.ArrayList;

import helper.Convert;
import helper.Vec2;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import model.GameInfo;

public class BitboardGraphic {
    private static Canvas bitboardCanvas = new Canvas(GameInfo.getBoardLength(), GameInfo.getBoardLength());
    private static GraphicsContext bitboardContext = bitboardCanvas.getGraphicsContext2D();
    private static final Color bitboardSelectedColor = Color.rgb(255, 255, 0, 0.4);

    public static Canvas getBoardOverlayCanvas() {
        return bitboardCanvas;
    }

    public static void drawBitboard(long bitboard) {
        ArrayList<Vec2> bitboardCorners = Convert.bitboardToCorners(bitboard);
        bitboardContext.setFill(bitboardSelectedColor);

        for(Vec2 vector : bitboardCorners) {
            bitboardContext.fillRect(
                vector.getXAsInt(),
                vector.getYAsInt(),
                GameInfo.getSquareLength(),
                GameInfo.getSquareLength()
            );
        }
    }

    public static void clearBitboard() {
        bitboardContext.clearRect(0, 0, GameInfo.getBoardLength(), GameInfo.getBoardLength());
    }
}
