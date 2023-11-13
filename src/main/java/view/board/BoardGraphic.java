package view.board;

import helper.Vec2;
import controller.BoardOverlay;
import helper.Convert;
import helper.FEN;
import model.Bitboard;
import model.GameInfo;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class BoardGraphic {
    private static StackPane boardStack = new StackPane();
    private static Canvas boardCanvas = new Canvas(GameInfo.getBoardLength(), GameInfo.getBoardLength());
    private static BoardOverlayGraphic overlayGraphic = new BoardOverlayGraphic();
    private static GraphicsContext boardCanvasContext = boardCanvas.getGraphicsContext2D();
    
    private static Color blackColor = Color.rgb(180, 140, 100);
    private static Color whiteColor = Color.rgb(244, 220, 180);
    
    private static boolean initialized = false;

    private static String currentFEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    private static String[] fields;

    public static StackPane getFullBoardStack() {
        if(!initialized) {
            drawBoardGraphicByBitboard();
        }

        boardStack.getChildren().addAll(
            getBoardCanvas(), 
            BoardOverlayGraphic.getBoardOverlayCanvas(),
            PieceGraphic.getPieceCanvas(), 
            HighPieceGraphic.getPieceCanvas(),
            BitboardGraphic.getBoardOverlayCanvas(),
            BoardOverlay.getBoardOverlayEventCanvas()
        );
        BoardOverlay.setupOverlayMouseEvent(overlayGraphic);

        return boardStack;
    }
    
    public static void drawBoardGraphicByFEN(String setFEN) {
        currentFEN = setFEN;

        fields = currentFEN.split("\\s+");
        
        int file = 0;
        int rank = 0;
        char currentChar;

        for(int i = 0; i < fields[0].length(); i++) {
            currentChar = fields[0].charAt(i);
            if((currentChar >= 'a' && currentChar <= 'z') || (currentChar >= 'A' && currentChar <= 'Z')) {
                PieceGraphic.addPieceToStack(FEN.charToPieceID(currentChar), new Vec2(file, rank));
                file++;
            } else if(currentChar == '/') {
                file = 0;
                rank++;
            } else {
                file += currentChar - '0';
            }
        }
    }

    public static void drawBoardGraphicByBitboard() {
        PieceGraphic.clearPieceCanvas();
        HighPieceGraphic.clearPieceCanvas();

        for(String currentKey : Bitboard.keys) {
            drawBitboardPieces(Bitboard.getBitboard(currentKey), currentKey);
        }
    }

    public static void pieceClickedByMouse(double mouseX, double mouseY) {
        BoardOverlayGraphic.drawHighlightSquare(mouseX, mouseY);
        PieceGraphic.clearByMouse(mouseX, mouseY);
        PieceGraphic.addFadedPieceToStack(mouseX, mouseY);
        PieceGraphic.initHighPiece(mouseX, mouseY);
    }

    private static void drawBitboardPieces(long bitboard, String currentKey) {
        int bitCount = Long.bitCount(bitboard);

        for(int i = 0; i < bitCount; i++) {
            int bitIndex = Long.numberOfTrailingZeros(bitboard);

            PieceGraphic.addPieceToStack(Bitboard.getPieceIDFromKey(currentKey), Convert.bitIndexToCorner(bitIndex));

            bitboard &= ~(1L << bitIndex);
        }
    }
    
    private static Canvas getBoardCanvas() {
        for(int file = 0; file < 8; file++) {
            for(int rank = 0; rank < 8; rank++) {
                boolean isWhite = (file + rank) % 2 == 0;
    
                Color squareColor = (isWhite) ? whiteColor : blackColor;
                Vec2 position = new Vec2(file * 100, rank * 100);
    
                drawSquare(position, squareColor);
            }
        }

        return boardCanvas;
    }

    private static void drawSquare(Vec2 position, Color color) {
        boardCanvasContext.setFill(color);
        boardCanvasContext.fillRect(position.getX(), position.getY(), 100, 100); 
    }
}
