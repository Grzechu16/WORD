package word;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        Controller controller = new Controller();
        loader.setController(controller);
        controller.start();
        Parent root = loader.load(getClass().getResource("styles/mainWindow.fxml"));
        primaryStage.setTitle("WORD Kraków - Egzamin na prawo jazdy 2018");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
