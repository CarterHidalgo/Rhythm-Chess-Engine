package view;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class ViewMain {
    private final int APP_WIDTH = 1200;
    private final int APP_HEIGHT = 800;

    private HBox root = new HBox();
    private BoardGraphic board = new BoardGraphic();
    private UserInterface ui = new UserInterface();

    public Scene initView() {
        StackPane boardStack = board.getFullBoardStack();
        Canvas uiCanvas = ui.getUICanvas();
        
        root.getChildren().addAll(boardStack, uiCanvas);

        Scene scene = new Scene(root, APP_WIDTH, APP_HEIGHT);

        return scene;
    }
}
