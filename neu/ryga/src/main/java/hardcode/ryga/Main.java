package hardcode.ryga;

import hardcode.ryga.controller.Controller;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
    	new Controller(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}