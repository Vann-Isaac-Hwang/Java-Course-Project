package libman.librarymanager;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.Map;

import javafx.scene.control.Alert;

public class AMController {

    @FXML
    public Label welcomeLabel;
    @FXML
    private TextField searchBar;

    @FXML
    private Button libraryButton, browseButton, radioButton, searchButton;

    @FXML
    private Button borrowButton, returnButton;

    @FXML
    private Label bookTitle;
    @FXML
    private Label authorName;
    @FXML
    private Label bookStatus;

    public static String TheBookName = "", TheAuthor = "", TheStatus = "", TheBookID="";

    @FXML
    private ProgressBar progressBar;

    @FXML
    private StackPane mainContent;

    // Static flag with a property to listen for changes
    public static final BooleanProperty flag = new SimpleBooleanProperty(false);

    public static User CurrentUser;
    public String currentView = "home-view";

    @FXML
    private void initialize() {
        // Add a listener to the flag to trigger updateBottomInfo when it changes
        flag.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                updateBottomInfo();
                // Reset the flag to false after updating
                flag.set(false);
            }
        });
        CurrentUser = LoginController.getCurrentUser();
        welcomeLabel.setText("Welcome! " + CurrentUser.getUsername());


        // 获取用户未归还的书籍
        Map<String, Integer> unreturnedBooks = Carrier.getUserUnreturnedBooks("127.0.0.1", 12345, CurrentUser.getID());
        StringBuilder overdueBooks = new StringBuilder();
        for (Map.Entry<String, Integer> entry : unreturnedBooks.entrySet()) {
            int overdueDays = entry.getValue();
            if (overdueDays > 10) {
                // 获取书籍名称
                String bookID = entry.getKey();
                String[] bookInfo = Carrier.getBookInfoByID("127.0.0.1", 12345, bookID);
                if (bookInfo.length > 1) {
                    overdueBooks.append("- ").append(bookInfo[1]).append(" (").append(overdueDays).append(" days overdue)\n");
                }
            }
        }

        if (overdueBooks.length() > 0) {
            // 弹窗显示超过 10 天的书籍
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Overdue Books Reminder");
            alert.setHeaderText("You have overdue books!");
            alert.setContentText("The following books are overdue for more than 10 days:\n" + overdueBooks);
            alert.showAndWait();
        }
    }

    @FXML
    protected void onHomeButtonClick() {
        mainContent.getChildren().clear();
        loadView("home-view");
        currentView = "home-view";
    }

    @FXML
    protected void onLibraryButtonClick() {
        mainContent.getChildren().clear();
        loadView("library-view");
        currentView = "library-view";
    }

    @FXML
    protected void onMyListButtonClick() {
        mainContent.getChildren().clear();
        loadView("recommended-books-view");
        currentView = "recommended-books-view";
    }

    @FXML
    protected void onRecordButtonClick() {
        mainContent.getChildren().clear();
        loadView("record-view");
        currentView = "record-view";
    }

    private void loadView(String viewName) {
        try {
            String fxmlFile = viewName + ".fxml";
            URL fxmlUrl = getClass().getResource(fxmlFile);
            if (fxmlUrl == null) {
                throw new RuntimeException("FXML file not found: " + fxmlFile);
            }
            Node view = FXMLLoader.load(fxmlUrl);
            mainContent.getChildren().clear();
            mainContent.getChildren().add(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onProfileButtonClick(ActionEvent actionEvent) {
    }

    public void updateBottomInfo() {
        this.bookTitle.setText(TheBookName);
        this.authorName.setText(TheAuthor);
        this.bookStatus.setText(TheStatus);
    }

    public void onBorrowButtonClick(ActionEvent actionEvent) {
        // Logic for borrowing a book
        if (!TheBookID.isEmpty() && !TheAuthor.isEmpty()) {
            // Simulate borrowing process
            System.out.println("Borrowing book: " + TheBookID + " by " + TheAuthor);
            String[] info = Carrier.updateBookStatusRequest("127.0.0.1", 12345, TheBookID, "Borrowed", CurrentUser.getID());
            // Update status
            TheStatus = "Borrowed";
            updateBottomInfo();
            if (info[1].equals("Update successful")) {
                System.out.println("Book status updated successfully.");
                // Show popup alert when successfully borrowed
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("The book \"" + TheBookName + "\" has been successfully borrowed!");
                alert.showAndWait();
            } else {
                System.out.println("Failed to update book status: " + info[1]);
                // Show popup alert when failed to borrow
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Failed to borrow the book \"" + TheBookName + "\". The book has been borrowed.");
                alert.showAndWait();
            }

        } else {
            System.out.println("No book selected to borrow.");
        }
        loadView(currentView);
    }

    public void onReturnButtonClick(ActionEvent actionEvent) {
        // Logic for returning a book
        if (!TheBookID.isEmpty() && !TheAuthor.isEmpty()) {
            // Simulate returning process
            System.out.println("Returning book: " + TheBookID + " by " + TheAuthor);
            String[] info = Carrier.updateBookStatusRequest("127.0.0.1", 12345, TheBookID, "Available", CurrentUser.getID());
            // Update status
            TheStatus = "Available";
            updateBottomInfo();
            if (info[1].equals("Update successful")) {
                System.out.println("Book status updated successfully.");
                // Show popup alert when successfully returned
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("The book \"" + TheBookName + "\" has been successfully returned!");
                alert.showAndWait();
            } else {
                System.out.println("Failed to update book status: " + info[1]);
                // Show popup alert when failed to return
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Failed to return the book \"" + TheBookName + "\". The book has not been borrowed.");
                alert.showAndWait();
            }
        } else {
            System.out.println("No book selected to return.");
        }
        loadView(currentView);
    }

    public static String getCurrentUserID() {
        return CurrentUser.getID();
    }
}