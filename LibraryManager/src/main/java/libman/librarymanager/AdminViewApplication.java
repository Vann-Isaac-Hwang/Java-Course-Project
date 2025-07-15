package libman.librarymanager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminViewApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Initialize the admin view application window
        FXMLLoader fxmlLoader = new FXMLLoader(AdminViewApplication.class.getResource("admin-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Admin View - Library Manager");
        stage.setScene(scene);
        stage.show();
    }

    public static void begin() {
        launch();
    }
}
