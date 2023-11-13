package view;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import view.board.BoardGraphic;
import view.ui.UserInterface;

public class ViewMain {
    private static final int APP_WIDTH = 1200;
    private static final int APP_HEIGHT = 800;

    private static HBox root = new HBox();
    private static UserInterface ui = new UserInterface();

    public static Scene initView() {
        StackPane boardStack = BoardGraphic.getFullBoardStack();
        Canvas uiCanvas = ui.getUICanvas();
        
        root.getChildren().addAll(boardStack, uiCanvas);

        Scene scene = new Scene(root, APP_WIDTH, APP_HEIGHT);

        return scene;
    }
}
