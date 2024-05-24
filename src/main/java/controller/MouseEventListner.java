package controller;

import javafx.scene.canvas.Canvas;
import model.GameInfo;

public class MouseEventListner {
    private static Canvas overlayCatch = new Canvas(GameInfo.getBoardLength(), GameInfo.getBoardLength());

    public static Canvas getBoardOverlayEventCanvas() {
        return overlayCatch;
    }

    public static void setupCatchMouseEvent() {
        overlayCatch.setOnMousePressed(event -> {
            BoardMouseHandler.handleMousePressed((float) event.getX(), (float) event.getY());
        });
        
        overlayCatch.setOnMouseDragged(event -> {
            BoardMouseHandler.handleMouseDragged((float) event.getX(), (float) event.getY());
        });

        overlayCatch.setOnMouseReleased(event -> {
            BoardMouseHandler.handleMouseReleased((float) event.getX(), (float) event.getY());
        });

        overlayCatch.setOnMouseClicked(event -> {
            BoardMouseHandler.handleMouseClicked((float) event.getX(), (float) event.getY());
        });

        overlayCatch.setOnMouseMoved(event -> {
            BoardMouseHandler.handleMouseMoved((float) event.getX(), (float) event.getY());
        });
    }
}