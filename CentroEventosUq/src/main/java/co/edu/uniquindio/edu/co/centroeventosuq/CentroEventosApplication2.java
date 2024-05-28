package co.edu.uniquindio.edu.co.centroeventosuq;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CentroEventosApplication2 extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CentroEventosApplication2.class.getResource("InicioGeneral.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello2!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}