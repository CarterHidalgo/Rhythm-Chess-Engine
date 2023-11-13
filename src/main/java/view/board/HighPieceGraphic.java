package view.board;

import helper.Vec2;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import model.GameInfo;

public class HighPieceGraphic {    
    private static final float SPRITE_LENGTH = (float) 333.333;

    private static double mouseX = 0;
    private static double mouseY = 0;

    private static Vec2 textureCoords = new Vec2();
    private static Image piecesImage;

    static {
        try {
            piecesImage = new Image(HighPieceGraphic.class.getResourceAsStream("/images/pieces.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Canvas highPieceCanvas = new Canvas(GameInfo.getBoardLength(), GameInfo.getBoardLength());
    private static GraphicsContext highPieceCanvasContext = highPieceCanvas.getGraphicsContext2D();

    public static Canvas getPieceCanvas() {
        return highPieceCanvas;
    }

    public static void drawPiece(Vec2 passedTextureCoords, double passedMouseX, double passedMouseY) {
        clearPieceCanvas();

        textureCoords.setX(passedTextureCoords.getX());
        textureCoords.setY(passedTextureCoords.getY());

        mouseX = passedMouseX;
        mouseY = passedMouseY;

        drawPiece();
    }

    public static void drawPiece() {
        clearPieceCanvas();

        mouseX = Math.max(0, mouseX);
        mouseX = Math.min(GameInfo.getBoardLength(), mouseX);

        mouseY = Math.max(0, mouseY);
        mouseY = Math.min(GameInfo.getBoardLength(), mouseY);

        highPieceCanvasContext.drawImage(
            piecesImage, 
            textureCoords.getXAsInt(), 
            textureCoords.getYAsInt(), 
            SPRITE_LENGTH, 
            SPRITE_LENGTH,
            mouseX - (GameInfo.getSquareLength() / 2),
            mouseY - (GameInfo.getSquareLength() / 2),
            GameInfo.getSquareLength(),
            GameInfo.getSquareLength()
        );
    }
    
    public static void clearPieceCanvas() {
        highPieceCanvasContext.clearRect(0, 0, GameInfo.getBoardLength(), GameInfo.getBoardLength());
    }

    public static void setMouseCoordinates(double passedMouseX, double passedMouseY) {
        mouseX = passedMouseX;
        mouseY = passedMouseY;
    }

    public static void updateWhenDragged(double passedMouseX, double passedMouseY) {
        mouseX = passedMouseX;
        mouseY = passedMouseY;

        drawPiece();
    }
}
