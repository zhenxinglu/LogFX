package org.logfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.IOException;

public class LogFx extends Application {
    private LogFxController controller;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LogFx.class.getResource("logFx.fxml"));
        Stage root = fxmlLoader.load();
        controller = fxmlLoader.getController();//the controller is only available after the fxml is loaded
        controller.initAfterConstruction(this);

        root.initOwner(stage);
        root.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();

        controller.saveState();
    }


    public static void main(String[] args) {
        launch();
    }
}