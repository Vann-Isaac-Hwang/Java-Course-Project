package libman.librarymanager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AMApplication extends Application {

    LoginApplication loginApplicational = new LoginApplication();

    @Override
    public void start(Stage stage) throws IOException {
        // Initialize the main application window
        FXMLLoader fxmlLoader = new FXMLLoader(AMApplication.class.getResource("am-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Library Manager");
        stage.setScene(scene);
        stage.show();
    }

    public static void begin() {
        launch();
    }
}
