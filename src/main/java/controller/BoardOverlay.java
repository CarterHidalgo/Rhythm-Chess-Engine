package controller;

import view.BoardOverlayGraphic;
import javafx.scene.canvas.Canvas;

public class BoardOverlay {
    private final int BOARD_LENGTH = 800;

    private Canvas overlayCatch = new Canvas(BOARD_LENGTH, BOARD_LENGTH);

    public Canvas getBoardOverlayEventCanvas() {
        return overlayCatch;
    }

    public void setupOverlayMouseEvent(BoardOverlayGraphic overlayGraphic) {
        overlayCatch.setOnMouseClicked(event -> {
            // System.out.println("clicked");
        });

        overlayCatch.setOnMouseDragged(event -> {
            // System.out.println("dragged");
        });

        overlayCatch.setOnMousePressed(event -> {
            // System.out.println("pressed");
            // check if conditions are right to draw a highlight
            overlayGraphic.drawHighlightSquare(event.getX(), event.getY());
        });
    }
}
