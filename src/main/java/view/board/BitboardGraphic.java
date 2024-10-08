package view.board;

import java.util.ArrayList;

import helper.Convert;
import helper.Vec2;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import model.GameInfo;

public class BitboardGraphic {
    public static final Color bitboardColor = Color.rgb(0, 0, 255, 0.4);
    public static final Color movesColor = Color.rgb(255, 0, 255, 0.4);
    
    private static Canvas bitboardCanvas = new Canvas(GameInfo.getBoardLength(), GameInfo.getBoardLength());
    private static GraphicsContext bitboardContext = bitboardCanvas.getGraphicsContext2D();

    public static Canvas getBitboardCanvas() {
        return bitboardCanvas;
    }

    public static void drawBitboardGraphic(long bitboard, Color color) {
        ArrayList<Vec2> bitboardCorners = Convert.bitboardToCorners(bitboard);
        bitboardContext.setFill(color);

        for(Vec2 vector : bitboardCorners) {
            bitboardContext.fillRect(
                vector.getXAsInt(),
                vector.getYAsInt(),
                GameInfo.getSquareLength(),
                GameInfo.getSquareLength()
            );
        }
    }

    public static void clearBitboardGraphic() {
        bitboardContext.clearRect(0, 0, GameInfo.getBoardLength(), GameInfo.getBoardLength());
    }
}
