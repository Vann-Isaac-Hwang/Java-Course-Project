package libman.librarymanager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class RegisterController {

    @FXML
    public Button cancelButton;
    public Button registerButton;
    public TextField usernameField;
    public PasswordField passwordField;
    public PasswordField confirmPasswordField;
    public Label errorLabel;

    // Close the
    @FXML
    protected void onCancelButtonClick() {
        System.out.println("Cancel button clicked, return to login window.");
        try {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            LoginApplication app = new LoginApplication();
            app.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onRegisterButtonClick(ActionEvent actionEvent) {
        // Get the input values from the fields
        String username = usernameField.getText();
        String password = passwordField.getText();;
        String confirmpassword = confirmPasswordField.getText();

        errorLabel.setStyle("-fx-text-fill: red;");
        // Check if the username is empty
        if (username.isEmpty()) {
            System.out.println("Username cannot be empty, please enter a username.");
            errorLabel.setText("Username cannot be empty, please enter a username.");
            return;
        }

        // Check if the password is empty
        if (password.isEmpty()) {
            System.out.println("Password cannot be empty, please enter a password.");
            errorLabel.setText("Password cannot be empty, please enter a password.");
            return;
        }

        // Check if the username is taken
        String[] tokensA = LoginClient.CheckUsernameExistence(username);
        if (tokensA[0].equals("Error") || tokensA[1].equals("Error")) {
            System.out.println("Error checking username existence, please try again.");
            errorLabel.setText("Error checking username existence, please try again.");
            return;
        }
        if (tokensA[1].equals("Exist")) {
            System.out.println("Username already exists, please choose another one.");
            errorLabel.setText("Username already exists, please choose another one.");
//            usernameField.clear();
            return;
        }
        else if (tokensA[1].equals("NotExist")) {
            System.out.println("Username is available.");

        }
        else {
            System.out.println("Unexpected response from server when checking username existence.");
            errorLabel.setText("Unexpected response from server when checking username existence.");
            return;
        }

        // Check the password
        if (!password.equals(confirmpassword)) {
            System.out.println("Passwords do not match, please try again.");
            errorLabel.setText("Passwords do not match, please try again.");
//            passwordField.clear();
//            confirmPasswordField.clear();
            return;
        }

        String[] tokensB = LoginClient.RegisterRequest(username, password);

        // Pretend that successful registration redirects to the login window, open login window
        if (tokensB[1].equals("Registration successful")) {
            System.out.println("Registration successful, redirecting to login window.");
            errorLabel.setStyle("-fx-text-fill: green;");
            errorLabel.setText("Registration successful!");
            passwordField.clear();
            confirmPasswordField.clear();
        } else {
            System.out.println("Registration failed, please try again.");
        }

    }
}
