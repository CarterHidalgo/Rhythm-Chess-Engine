package model;

import view.board.BoardOverlayGraphic;

public class Promotion {
    private static boolean active = false;

    public static void setActive(boolean arg) {
        active = arg;
    }

    public static boolean isActive() {
        return active;
    }

    public static void initPromotion() {
        active = true;
        BoardOverlayGraphic.clearOverlayCanvas();
        // draw promotion graphic thingy
    }
}
