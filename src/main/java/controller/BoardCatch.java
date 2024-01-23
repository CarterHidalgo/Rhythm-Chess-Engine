package controller;

import javafx.scene.canvas.Canvas;
import model.GameInfo;

public class BoardCatch {
    private static Canvas overlayCatch = new Canvas(GameInfo.getBoardLength(), GameInfo.getBoardLength());

    public static Canvas getBoardOverlayEventCanvas() {
        return overlayCatch;
    }

    public static void setupCatchMouseEvent() {
        overlayCatch.setOnMousePressed(event -> {
            BoardMouseHandler.handleMousePressed(event.getX(), event.getY());
        });
        
        overlayCatch.setOnMouseDragged(event -> {
            BoardMouseHandler.handleMouseDragged(event.getX(), event.getY());
        });

        overlayCatch.setOnMouseReleased(event -> {
            BoardMouseHandler.handleMouseReleased(event.getX(), event.getY());
        });

        overlayCatch.setOnMouseClicked(event -> {
            BoardMouseHandler.handleMouseClicked(event.getX(), event.getY());
        });

        overlayCatch.setOnMouseMoved(event -> {
            BoardMouseHandler.handleMouseMoved(event.getX(), event.getY());
        });
    }
}
