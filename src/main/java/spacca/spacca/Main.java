package spacca.spacca;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
        Scene homescreen = new Scene(fxmlLoader.load());
        stage.setTitle("Spacca");
       // Imposta lo stile della finestra senza barra del titolo
        //stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(true);
        stage.setScene(homescreen);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
