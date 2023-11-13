package view.board;

import helper.Convert;
import helper.Vec2;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import model.Bitboard;
import model.GameInfo;

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
    
    private static final float SPRITE_LENGTH = (float) 333.333;

    private static Image piecesImage;
    
    static {
        try {
            piecesImage = new Image(HighPieceGraphic.class.getResourceAsStream("/images/pieces.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Canvas pieceCanvas = new Canvas(GameInfo.getBoardLength(), GameInfo.getBoardLength());
    private static GraphicsContext pieceCanvasContext = pieceCanvas.getGraphicsContext2D();
    private static Vec2 textureCoords = new Vec2();

    public static Canvas getPieceCanvas() {
        return pieceCanvas;
    }

    public static void addPieceToStack(int ID, Vec2 cornerCoords) {
        setImageCoords(ID);

        pieceCanvasContext.drawImage(
            piecesImage, 
            textureCoords.getXAsInt(), 
            textureCoords.getYAsInt(), 
            SPRITE_LENGTH, 
            SPRITE_LENGTH,
            cornerCoords.getXAsInt(),
            cornerCoords.getYAsInt(),
            GameInfo.getSquareLength(),
            GameInfo.getSquareLength()
        );
    }

    public static void addFadedPieceToStack(double mouseX, double mouseY) {
        setImageCoords(
            Bitboard.getPieceIDFromKey(
                Bitboard.getKeyFromBitIndex(
                    Convert.mouseToBitIndex(mouseX, mouseY)
                )
            )
        );

        Vec2 cornerCoords = Convert.mouseToCorner(mouseX, mouseY);

        pieceCanvasContext.setGlobalAlpha(0.25);
        pieceCanvasContext.drawImage(
            piecesImage,
            textureCoords.getXAsInt(),
            textureCoords.getYAsInt(),
            SPRITE_LENGTH,
            SPRITE_LENGTH,
            cornerCoords.getXAsInt(),
            cornerCoords.getYAsInt(),
            GameInfo.getSquareLength(),
            GameInfo.getSquareLength()
        );
        pieceCanvasContext.setGlobalAlpha(1);
    }

    public static void initHighPiece(double mouseX, double mouseY) {
        HighPieceGraphic.drawPiece(textureCoords, mouseX, mouseY);
    }
    
    public static void clearPieceCanvas() {
        pieceCanvasContext.clearRect(0, 0, GameInfo.getBoardLength(), GameInfo.getBoardLength());
    }

    public static void clearByMouse(double mouseX, double mouseY) {
        Vec2 clearSquareUV = Convert.mouseToCorner(mouseX, mouseY);

        pieceCanvasContext.clearRect(
            clearSquareUV.getXAsInt(), 
            clearSquareUV.getYAsInt(), 
            GameInfo.getSquareLength(), 
            GameInfo.getSquareLength()
        );
    }

    private static void setImageCoords(int ID) {
        int pieceValue = (ID & 0b111) - 1;
        int pieceColor = (ID >> 4) & 0b11;

        textureCoords.setX(pieceValue * SPRITE_LENGTH);
        textureCoords.setY(pieceColor * SPRITE_LENGTH);
    }
}
