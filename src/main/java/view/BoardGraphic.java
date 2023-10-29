package view;

import helper.Vec2;
import controller.BoardOverlay;
import helper.FEN;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class BoardGraphic {
    private final int BOARD_WIDTH = 800;
    private final int BOARD_HEIGHT = 800;

    private int viewSide = 0;

    private StackPane boardStack = new StackPane();
    private Canvas boardCanvas = new Canvas(BOARD_WIDTH, BOARD_HEIGHT);
    private BoardOverlayGraphic overlayGraphic = new BoardOverlayGraphic();
    private BoardOverlay overlayUI = new BoardOverlay();
    private PieceGraphic pieceGraphic = new PieceGraphic();
    private FEN FENHelper = new FEN();
    private GraphicsContext boardCanvasContext = boardCanvas.getGraphicsContext2D();
    
    private Color blackColor = Color.rgb(180, 140, 100);
    private Color whiteColor = Color.rgb(244, 220, 180);

    private String FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    // private String FEN = "2kr1bnr/ppq1p1pp/n2pb3/5p2/1p1PP3/2N2Q1N/P1P1BPPP/1RB2RK1 b - - 1 9";

    private String[] fields;

    public BoardGraphic() {
        setBoardGraphicByFEN(this.FEN);
    }

    public StackPane getFullBoardStack() {
        boardStack.getChildren().addAll(
            getBoardCanvas(), 
            overlayGraphic.getBoardOverlayCanvas(),
            pieceGraphic.getPieceCanvas(), 
            overlayUI.getBoardOverlayEventCanvas()
        );
        overlayUI.setupOverlayMouseEvent(overlayGraphic);
        // overlayGraphic.drawHighlightSquare(0, 0);

        return boardStack;
    }
    
    public void setBoardGraphicByFEN(String FEN) {
        setFEN(FEN);

        fields = FEN.split("\\s+");
        
        int file = 1;
        int rank = 1;
        char currentChar;

        for(int i = 0; i < fields[0].length(); i++) {
            currentChar = fields[0].charAt(i);
            if((currentChar >= 'a' && currentChar <= 'z') || (currentChar >= 'A' && currentChar <= 'Z')) {
                pieceGraphic.addPieceToStack(FENHelper.charToPieceID(currentChar), new Vec2(file, rank), viewSide);
                file++;
            } else if(currentChar == '/') {
                file = 1;
                rank++;
            } else {
                file += currentChar - '0';
            }
        }
    }

    public void setBoardView(int view) {
        viewSide = view;
    }
    
    private Canvas getBoardCanvas() {
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

    private void drawSquare(Vec2 position, Color color) {
        boardCanvasContext.setFill(color);
        boardCanvasContext.fillRect(position.getX(), position.getY(), 100, 100); 
    }
    
    private void setFEN(String FEN) {
        this.FEN = FEN;
    }
}
