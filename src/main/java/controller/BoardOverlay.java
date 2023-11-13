package controller;

import javafx.scene.canvas.Canvas;
import model.GameInfo;
import view.board.BoardOverlayGraphic;

public class BoardOverlay {
    private static Canvas overlayCatch = new Canvas(GameInfo.getBoardLength(), GameInfo.getBoardLength());

    public static Canvas getBoardOverlayEventCanvas() {
        return overlayCatch;
    }

    public static void setupOverlayMouseEvent(BoardOverlayGraphic overlayGraphic) {
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

    }
}
