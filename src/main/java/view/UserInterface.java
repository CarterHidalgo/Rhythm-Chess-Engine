package view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class UserInterface {
    private final int UI_WIDTH = 400;
    private final int UI_HEIGHT = 800;

    private Canvas uiCanvas = new Canvas(UI_WIDTH, UI_HEIGHT);
    private GraphicsContext gc = uiCanvas.getGraphicsContext2D();

    private Color backgroundColor = Color.rgb(56, 52, 60);

    public Canvas getUICanvas() {
        gc.setFill(backgroundColor);
        gc.fillRect(0, 0, uiCanvas.getWidth(), uiCanvas.getHeight());

        return uiCanvas;
    }
}
