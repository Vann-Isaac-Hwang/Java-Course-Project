package libman.librarymanager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginController {
    public Label errorLabel;
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private CheckBox adminCheckBox;

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    private static User currentUser = null;

    public void onLoginButtonClicked(ActionEvent actionEvent) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        boolean isAdmin = adminCheckBox.isSelected();

        // Clear fields after login attempt
        passwordField.clear();
        isAdmin = adminCheckBox.isSelected();

        String[] tokens = LoginClient.LoginRequest(username, password, isAdmin);

        // Handle login response
        String bannedMessage = "User is banned";
        if (tokens[1].equals("Login successful")) {
            errorLabel.setText("");
            if (isAdmin) {
                try {
                    String[] GetAdminIDwithUsernameResponse = LoginClient.GetAdminIDwithUsername(username);
                    if (GetAdminIDwithUsernameResponse[1].equals("NotFound")) {
                        errorLabel.setText("Admin user not found, please register first.");
                        return;
                    }
                    String userID = GetAdminIDwithUsernameResponse[1];
                    currentUser = new User(username, password, userID);
                    Stage stage = (Stage) loginButton.getScene().getWindow();
                    AdminViewApplication app = new AdminViewApplication();
                    app.start(stage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    String[] GetUserIDwithUsernameResponse = LoginClient.GetUserIDwithUsername(username);
                    if (GetUserIDwithUsernameResponse[1].equals("NotFound")) {
                        errorLabel.setText("User not found, please register first.");
                        return;
                    }
                    String userID = GetUserIDwithUsernameResponse[1];
                    currentUser = new User(username, password, userID);
                    Stage stage = (Stage) loginButton.getScene().getWindow();
                    AMApplication app = new AMApplication();
                    app.start(stage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (tokens[1].equals(bannedMessage)) {
            errorLabel.setText("Your account is banned. Please contact support.");
        } else {
            errorLabel.setText(tokens[1] + ", please try again.");
        }
    }

    public void onRegisterButtonClicked(ActionEvent actionEvent) {
        // Open the register window
        try {
            Stage stage = (Stage) registerButton.getScene().getWindow();
            RegisterApplication app = new RegisterApplication();
            app.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static User getCurrentUser() {
        return currentUser;
    }
}