package view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class BoardOverlayGraphic {
    private final int BOARD_LENGTH = 800;
    private final int SQUARE_LENGTH = 100;

    private final Color highlightColor = Color.rgb(10, 100, 10, 0.4);

    private Canvas overlayCanvas = new Canvas(BOARD_LENGTH, BOARD_LENGTH);
    private GraphicsContext overlayContext = overlayCanvas.getGraphicsContext2D();

    public Canvas getBoardOverlayCanvas() {
        return overlayCanvas;
    }

    public void drawHighlightSquare(double x, double y) {
        x = ((int)x / 100) * 100;
        y = ((int)y / 100) * 100;

        clearOverlayCanvas();

        overlayContext.setFill(highlightColor);
        overlayContext.fillRect(x, y, SQUARE_LENGTH, SQUARE_LENGTH);
    }

    private void clearOverlayCanvas() {
        overlayContext.clearRect(0, 0, BOARD_LENGTH, BOARD_LENGTH);
    }
}
