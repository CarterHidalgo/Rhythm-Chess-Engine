package view.board;

import controller.MouseEventListner;
import helper.Convert;
import helper.Debug;
import helper.Vec2;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.Bitboard;
import model.GameInfo;

public class BoardGraphic {
    private static StackPane boardStack = new StackPane();
    private static Canvas boardCanvas = new Canvas(GameInfo.getBoardLength(), GameInfo.getBoardLength());
    private static GraphicsContext boardCanvasContext = boardCanvas.getGraphicsContext2D();
    
    private static Color blackColor = Color.rgb(180, 140, 100);
    private static Color whiteColor = Color.rgb(244, 220, 180);
    
    private static boolean initialized = false;

    public static StackPane getFullBoardStack() {
        if(!initialized) {
            drawBoardGraphic();
        }

        boardStack.getChildren().addAll(
            getBoardCanvas(), 
            BoardOverlayGraphic.getBoardOverlayCanvas(),
            PieceGraphic.getPieceCanvas(), 
            HighPieceGraphic.getPieceCanvas(),
            BitboardGraphic.getBitboardCanvas(),
            BoardLookupGraphic.getBoardLookupCanvas(),
            PromotionGraphic.getPromotionCanvas(),
            MouseEventListner.getBoardOverlayEventCanvas()
        );
        MouseEventListner.setupCatchMouseEvent();

        return boardStack;
    }

    public static void drawBoardGraphic() {
        PieceGraphic.clearPieceCanvas();
        HighPieceGraphic.clearPieceCanvas();

        if(Debug.on("C3")) {
            BoardLookupGraphic.drawBoardLookupGraphic();
        }

        for(String currentKey : Bitboard.pieceKeys) {
            drawBitboardPieces(Bitboard.getBitboard(currentKey), currentKey);
        }
    }

    public static void pieceClickedByMouse(float mouseX, float mouseY) {
        BoardOverlayGraphic.drawHighlightSquare(mouseX, mouseY);
        PieceGraphic.clearByMouse(mouseX, mouseY);
        PieceGraphic.addFadedPieceToStack(mouseX, mouseY);
        PieceGraphic.initHighPiece(mouseX, mouseY);
    }

    private static void drawBitboardPieces(long bitboard, String currentKey) {
        int bitCount = Long.bitCount(bitboard);

        for(int i = 0; i < bitCount; i++) {
            byte bitIndex = (byte) Long.numberOfTrailingZeros(bitboard);

            PieceGraphic.addPieceToStack(Bitboard.getPieceIDFromKey(currentKey), Convert.bitIndexToCorner(bitIndex));

            bitboard &= ~(1L << bitIndex);
        }
    }
    
    private static Canvas getBoardCanvas() {
        byte bitIndex = 0;

        for(int rank = 7; rank > -1; rank--) {
            for(int file = 0; file < 8; file++) {
                boolean isWhite = (file + rank) % 2 == 0;
    
                Color squareColor = (isWhite) ? whiteColor : blackColor;
                Vec2 position = new Vec2(file * 100, rank * 100);
    
                drawSquare(position, squareColor, bitIndex++);
            }
        }

        return boardCanvas;
    }

    private static void drawSquare(Vec2 position, Color color, byte bitIndex) {
        boardCanvasContext.setFill(color);
        boardCanvasContext.fillRect(position.getX(), position.getY(), 100, 100); 

        if(Debug.on("C2")) {
            boardCanvasContext.setFill(Color.BLACK);
            boardCanvasContext.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            boardCanvasContext.fillText(Integer.toString(bitIndex), position.getX() + 5, position.getY() + 15);
        }
    }
}
