package libman.librarymanager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class RecommendedBooksController {

    @FXML
    private TableView<Book> recommendedBooksTable;

    @FXML
    private TableColumn<Book, String> bookNameColumn;

    @FXML
    private TableColumn<Book, String> authorColumn;

    @FXML
    private TableColumn<Book, String> genreColumn;

    @FXML
    private TableColumn<Book, String> idColumn;

    @FXML
    private TableColumn<Book, String> statusColumn;

    private final ObservableList<Book> recommendedBooks = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // 设置列的单元格值工厂
        bookNameColumn.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // 绑定列宽比例
        bookNameColumn.prefWidthProperty().bind(recommendedBooksTable.widthProperty().multiply(0.3)); // 30%
        authorColumn.prefWidthProperty().bind(recommendedBooksTable.widthProperty().multiply(0.2));   // 20%
        genreColumn.prefWidthProperty().bind(recommendedBooksTable.widthProperty().multiply(0.2));    // 20%
        idColumn.prefWidthProperty().bind(recommendedBooksTable.widthProperty().multiply(0.15));      // 15%
        statusColumn.prefWidthProperty().bind(recommendedBooksTable.widthProperty().multiply(0.15)); // 15%

        // 加载推荐书籍
        loadRecommendedBooks();

        // 将推荐书籍绑定到表格
        recommendedBooksTable.setItems(recommendedBooks);
    }

    private void loadRecommendedBooks() {
        // 假设 getRecommendedBooks() 返回一个 Book 对象的列表
        recommendedBooks.addAll(Carrier.getRecommendedBooks("127.0.0.1", 12345, AMController.getCurrentUserID()));
    }

    public void handleRowSelection(MouseEvent mouseEvent) {
        Book selectedBook = recommendedBooksTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
//            System.out.println("Selected Book:");
//            System.out.println("Title: " + selectedBook.getBookName());
//            System.out.println("Author: " + selectedBook.getAuthor());
//            System.out.println("Genre: " + selectedBook.getGenre());
//            System.out.println("ID: " + selectedBook.getId());
//            System.out.println("Status: " + selectedBook.getStatus());
            // Update bottom labels
            AMController.TheBookName = selectedBook.getBookName();
            AMController.TheAuthor = selectedBook.getAuthor();
            AMController.TheStatus = selectedBook.getStatus();
            AMController.TheBookID = selectedBook.getId();
            AMController.flag.set(true);
        } else {
            System.out.println("No book selected.");
        }
    }
}