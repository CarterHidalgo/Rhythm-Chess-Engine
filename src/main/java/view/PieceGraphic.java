package view;

import helper.Vec2;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class PieceGraphic {
    /*
     * Piece ennumeration order is based on the order in pieces.png to make it easier to set
     * the image coordinates in setImageCoords()
     * 
     * None: 0
     * King: 1
     * Queen: 2
     * Bishop: 3
     * Knight: 4
     * Rook: 5
     * Pawn: 6
     * 
     * White: 8
     * Black: 16
     */

    private final int BOARD_LENGTH = 800;
    private final int SQUARE_LENGTH = 100;
    
    private final float SPRITE_LENGTH = (float) 333.333;
    
    private Image piecesImage = new Image(getClass().getResourceAsStream("/images/pieces.png"));
    private Canvas pieceCanvas = new Canvas(BOARD_LENGTH,BOARD_LENGTH);
    private GraphicsContext pieceCanvasContext = pieceCanvas.getGraphicsContext2D();
    private Vec2 textureCoords = new Vec2();

    public Canvas getPieceCanvas() {
        return pieceCanvas;
    }

    public void addPieceToStack(int ID, Vec2 index, int orientation) {
        setImageCoords(ID);

        int drawX, drawY;

        if(orientation == 0) {
            drawX = ((index.getXAsInt()-1) * SQUARE_LENGTH);
            drawY = ((index.getYAsInt()-1) * SQUARE_LENGTH);
        } else {
            drawX = ((8-index.getXAsInt()) * SQUARE_LENGTH);
            drawY = ((8-index.getYAsInt()) * SQUARE_LENGTH);
        }

        pieceCanvasContext.drawImage(
            piecesImage, 
            textureCoords.getXAsInt(), 
            textureCoords.getYAsInt(), 
            SPRITE_LENGTH, 
            SPRITE_LENGTH,
            drawX,
            drawY,
            SQUARE_LENGTH,
            SQUARE_LENGTH
        );
    }

    private void setImageCoords(int ID) {
        int pieceValue = (ID & 0b111) - 1;
        int pieceColor = (ID >> 4) & 0b11;

        textureCoords.setX(pieceValue * SPRITE_LENGTH);
        textureCoords.setY(pieceColor * SPRITE_LENGTH);
    }
}
