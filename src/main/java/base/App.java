package base;

import javafx.application.Application;
import javafx.stage.Stage;
import view.ViewMain;
import model.ModelMain;

public class App extends Application {
    @Override
    public void start(Stage stage) {
        ModelMain.initModel();

        stage.setTitle("Java ChessBot Application");
        stage.setResizable(false);
        stage.setScene(ViewMain.initView());
        stage.show();
    }
}