package base;

import helper.Debug;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;
import model.ModelMain;
import view.ViewMain;

public class App extends Application {
    @Override
    public void start(Stage stage) {
        ModelMain.initModel();

        if(!Debug.on("F1")) {
            stage.setTitle("Java ChessBot Application");
            stage.setResizable(false);
            stage.setScene(ViewMain.initView());
            stage.show();
        }

        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateAnimation();
            }
        };

        animationTimer.start();
    }

    private void updateAnimation() {
        // ANIMATION LOOP

        // Things to update: [UI, Board, Model]
        ViewMain.updateViewMain();
    }
}