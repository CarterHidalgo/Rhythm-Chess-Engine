package view;

import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import model.GameInfo;
import view.board.BoardGraphic;
import view.board.PromotionGraphic;
// import view.ui.UserInterface;

public class ViewMain {
    private static final int APP_WIDTH = 800;
    private static final int APP_HEIGHT = 800;

    private static HBox root = new HBox();
    // private static UserInterface ui = new UserInterface();

    public static Scene initView() {
        StackPane boardStack = BoardGraphic.getFullBoardStack();
        
        // add UI elements when ready
        // Canvas uiCanvas = ui.getUICanvas();
        
        root.getChildren().addAll(boardStack);

        Scene scene = new Scene(root, APP_WIDTH, APP_HEIGHT);

        return scene;
    }

    public static void updateViewMain() {
        if(GameInfo.getGameState() == "promote") {
            PromotionGraphic.updatePromotionGraphic();
        }
    }
}
