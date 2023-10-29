package base;

import javafx.application.Application;
import javafx.stage.Stage;
import view.ViewMain;

public class App extends Application {
    @Override
    public void start(Stage stage) {
        ViewMain applicationView = new ViewMain();

        stage.setTitle("Java ChessBot Application");
        stage.setResizable(false);;
        stage.setScene(applicationView.initView());
        stage.show();
    }
}