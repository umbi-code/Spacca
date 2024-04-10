package spacca.spacca;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
        Scene homescreen = new Scene(fxmlLoader.load());
        stage.setTitle("Spacca!");
        stage.setScene(homescreen);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
