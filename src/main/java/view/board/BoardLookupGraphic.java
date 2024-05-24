package view.board;

import helper.Bit;
import helper.Vec2;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.BoardLookup;
import model.GameInfo;

public class BoardLookupGraphic {
    private static Canvas boardLookupCanvas = new Canvas(GameInfo.getBoardLength(), GameInfo.getBoardLength());
    private static GraphicsContext boardLookupContext = boardLookupCanvas.getGraphicsContext2D();

    private static boolean easyRead = true;
    private static String pieceString, byteString;

    public static Canvas getBoardLookupCanvas() {
        return boardLookupCanvas;
    }

    public static void drawBoardLookupGraphic() {
        clearBoardLookupCanvas();
        Vec2 position = new Vec2();

        for(byte bitIndex = 0; bitIndex < 64; bitIndex++) {
            position.setX((bitIndex % 8) * GameInfo.getSquareLength());
            position.setY(GameInfo.getBoardLength() - (((bitIndex / 8) + 1) * GameInfo.getSquareLength()));
            
            if(easyRead) {
                pieceString = BoardLookup.getPieceByBitIndex(bitIndex);
            } else {
                byteString = Bit.toPaddedBinaryString(BoardLookup.getByteCodeByBitIndex(bitIndex), 4);
            }

            boardLookupContext.setFill(Color.BLACK);
            boardLookupContext.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
            boardLookupContext.fillText(((easyRead) ? pieceString : byteString), position.getX() + 5, position.getY() + GameInfo.getSquareLength() - 2);
        }
    }

    private static void clearBoardLookupCanvas() {
        boardLookupContext.clearRect(0, 0, GameInfo.getBoardLength(), GameInfo.getBoardLength());
    }
}