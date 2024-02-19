package view.board;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import model.GameInfo;

public class PromotionGraphic {
    private static final float SPRITE_LENGTH = (float) 333.333;

    private static Image piecesImage;
    static {
        try {
            piecesImage = new Image(HighPieceGraphic.class.getResourceAsStream("/images/pieces.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int[] whiteRim = {147, 147, 147};
    private static int[] whiteCenter = {210, 210, 210};
    private static int[] redRim = {206, 99, 36};
    private static int[] redCenter = {187, 148, 125};

    private static Color filterColor = Color.rgb(0, 0, 0, 0.6);
    private static Color rimColor = Color.rgb(147, 147, 147);
    private static Color centerColor = Color.rgb(210, 210, 210);
    private static Color[][] colors = {
        {Color.rgb(147, 147, 147), Color.rgb(210, 210, 210)},
        {Color.rgb(147, 147, 147), Color.rgb(210, 210, 210)},
        {Color.rgb(147, 147, 147), Color.rgb(210, 210, 210)},
        {Color.rgb(147, 147, 147), Color.rgb(210, 210, 210)},
    };

    private static Canvas promotionCanvas = new Canvas(GameInfo.getBoardLength(), GameInfo.getBoardLength());
    private static GraphicsContext promotionCanvasContext = promotionCanvas.getGraphicsContext2D();
    
    private static int[] corners = {100, 100, 100, 100};
    private static double[] imageScale = {80, 80, 80, 80};
    private static RadialGradient[] gradients = new RadialGradient[4];

    private static int centerX = 0;
    private static int leftX = 0;
    private static int section = 0;
    private static double animSpeed = 0.4;

    public static void setCardX(int x) {
        centerX = (x * GameInfo.getSquareLength()) + (GameInfo.getSquareLength() / 2);
        leftX = (x * GameInfo.getSquareLength());
    }

    public static int getSection() {
        return section;
    }

    public static Canvas getPromotionCanvas() {
        return promotionCanvas;
    }

    public static void clearPromotionCanvas() {
        promotionCanvasContext.clearRect(0, 0, GameInfo.getBoardLength(), GameInfo.getBoardLength());
    }

    public static void drawPromotionGraphic() {
        clearPromotionCanvas();

        promotionCanvasContext.setFill(filterColor);
        promotionCanvasContext.fillRect(0, 0, GameInfo.getBoardLength(), GameInfo.getBoardLength()); 

        for(int i = 0; i < 4; i++) {
            gradients[i] = new RadialGradient(
                0,
                0,
                centerX,
                (GameInfo.getSquareLength() / 2) + (GameInfo.getSquareLength() * i),
                GameInfo.getSquareLength() / 2,
                false,
                CycleMethod.NO_CYCLE,
                new Stop(0, colors[i][0]),
                new Stop(1, colors[i][1])
            );

            promotionCanvasContext.setFill(gradients[i]);
            promotionCanvasContext.fillRoundRect(
                leftX, 
                GameInfo.getSquareLength() * i,
                GameInfo.getSquareLength(),
                GameInfo.getSquareLength(),
                corners[i],
                corners[i]
            );

            double yOffset = 0;
            int textureOffset = 0;

            switch(i) {
                case 0:
                    yOffset = 0.5;
                    textureOffset = 1;
                break;
                case 1:
                    yOffset = 1.5;
                    textureOffset = 3;
                break;
                case 2:
                    yOffset = 2.5;
                    textureOffset = 4;
                break;
                case 3:
                    yOffset = 3.5;
                    textureOffset = 2;
                break;
            }

            promotionCanvasContext.save();
            promotionCanvasContext.translate(centerX, GameInfo.getSquareLength() * yOffset);
            promotionCanvasContext.drawImage(
                piecesImage,
                SPRITE_LENGTH * textureOffset,
                SPRITE_LENGTH * GameInfo.getSide(),
                SPRITE_LENGTH,
                SPRITE_LENGTH,
                -imageScale[i] / 2,
                -imageScale[i] / 2,
                imageScale[i],
                imageScale[i]
            );
            promotionCanvasContext.restore();
        }
    }

    public static void updatePromotionGraphic() {
        for(int i = 0; i < 4; i++) {
            if(i == section) {
                corners[i] -= Math.abs(0 - corners[i]) * animSpeed;
                
                // morph center to red
                colors[i][0] = Color.rgb(
                    (int) (colors[i][0].getRed() + (int) (redCenter[0] - colors[i][0].getRed() * animSpeed)),
                    (int) (colors[i][0].getGreen() + (int) (redCenter[1] - colors[i][0].getGreen() * animSpeed)), 
                    (int) (colors[i][0].getBlue() + (int) (redCenter[2] - colors[i][0].getBlue() * animSpeed))
                );
                colors[i][1] = Color.rgb(
                    (int) (colors[i][1].getRed() + (int) (redRim[0] - colors[i][1].getRed() * animSpeed)),
                    (int) (colors[i][1].getGreen() + (int) (redRim[1] - colors[i][1].getGreen() * animSpeed)), 
                    (int) (colors[i][1].getBlue() + (int) (redRim[2] - colors[i][1].getBlue() * animSpeed))
                );

                // scale image up
                imageScale[i] += Math.abs(100 - imageScale[i]) * animSpeed;

                continue;
            }

            corners[i] += Math.abs(100 - corners[i]) * animSpeed;

            // morph center to white
            colors[i][0] = Color.rgb(
                (int) (colors[i][0].getRed() + (int) (whiteCenter[0] - colors[i][0].getRed() * animSpeed)),
                    (int) (colors[i][0].getGreen() + (int) (whiteCenter[1] - colors[i][0].getGreen() * animSpeed)), 
                    (int) (colors[i][0].getBlue() + (int) (whiteCenter[2] - colors[i][0].getBlue() * animSpeed))
            );
            
            colors[i][1] = Color.rgb(
                (int) (colors[i][1].getRed() + (int) (whiteRim[0] - colors[i][1].getRed() * animSpeed)),
                (int) (colors[i][1].getGreen() + (int) (whiteRim[0] - colors[i][1].getGreen() * animSpeed)), 
                (int) (colors[i][1].getBlue() + (int) (whiteRim[0] - colors[i][1].getBlue() * animSpeed))
            );

            gradients[i] = new RadialGradient(
                0,
                0,
                centerX,
                (GameInfo.getSquareLength() / 2) + (GameInfo.getSquareLength() * i),
                GameInfo.getSquareLength() / 2,
                false,
                CycleMethod.NO_CYCLE,
                new Stop(0, centerColor),
                new Stop(1, rimColor)
            );

            imageScale[i] -= Math.abs(80 - imageScale[i]) * animSpeed;
        }

        drawPromotionGraphic();
    }

    public static void handleMouseOverCard(double mouseX, double mouseY) {
        if(
            mouseX > leftX &&
            mouseX < leftX + GameInfo.getSquareLength() && 
            mouseY > 0 &&
            mouseY < GameInfo.getSquareLength() * 4) 
        {
            section = (int) (mouseY / GameInfo.getSquareLength());
            return;
        }

        section = -1;
        return;
    }

    public static void resetPromotionGraphic() {
        section = 0;
        for(int i = 0; i < 4; i++) {
            corners[i] = 100;
            imageScale[i] = 80;
        }

        clearPromotionCanvas();
    }
}
