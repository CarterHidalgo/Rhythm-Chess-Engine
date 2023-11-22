package view.board;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import model.GameInfo;

public class PromotionGraphic {
    private static double mouseX = 0;
    private static double mouseY = 0;

    private static Canvas promotionCanvas = new Canvas(GameInfo.getBoardLength(), GameInfo.getBoardLength());
    private static GraphicsContext promotionCanvasContext = promotionCanvas.getGraphicsContext2D();

    public static Canvas getPromotionCanvas() {
        return promotionCanvas;
    }

    public static void clearPromotionCanvas() {
        promotionCanvasContext.clearRect(0, 0, GameInfo.getBoardLength(), GameInfo.getBoardLength());
    }

    public static void setMouseCoordinates(double passedMouseX, double passedMouseY) {
        mouseX = passedMouseX;
        mouseY = passedMouseY;
    }

    public static void updateWhenDragged(double passedMouseX, double passedMouseY) {
        mouseX = passedMouseX;
        mouseY = passedMouseY;

        // update visual
    }
}
