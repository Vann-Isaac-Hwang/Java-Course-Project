package libman.librarymanager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class LibraryViewController {

    @FXML
    private TableView<Book> bookTable;

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

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> searchTypeComboBox;

    private final ObservableList<Book> bookList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Set up the columns
        bookNameColumn.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Bind column width proportionally
        bookNameColumn.prefWidthProperty().bind(bookTable.widthProperty().multiply(0.3)); // 30%
        authorColumn.prefWidthProperty().bind(bookTable.widthProperty().multiply(0.2));   // 20%
        genreColumn.prefWidthProperty().bind(bookTable.widthProperty().multiply(0.2));    // 20%
        idColumn.prefWidthProperty().bind(bookTable.widthProperty().multiply(0.15));      // 15%
        statusColumn.prefWidthProperty().bind(bookTable.widthProperty().multiply(0.15));  // 15%

        // Load sample data
        loadBooks();

        // Initialize ComboBox options
        searchTypeComboBox.setItems(FXCollections.observableArrayList("Book Name", "Author", "Genre", "ID", "Status"));
        searchTypeComboBox.setValue("Book Name"); // Default selection

        // Create a filtered list
        FilteredList<Book> filteredBooks = new FilteredList<>(bookList, b -> true);

        // Bind the filtered list to the table
        bookTable.setItems(filteredBooks);

        // Add a listener to the search field
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            applyFilter(filteredBooks, newValue);
        });

        // Add a listener to the ComboBox
        searchTypeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            applyFilter(filteredBooks, searchField.getText());
        });

    }

    private void applyFilter(FilteredList<Book> filteredBooks, String filterText) {
        filteredBooks.setPredicate(book -> {
            // If the search field is empty, display all books
            if (filterText == null || filterText.isEmpty()) {
                return true;
            }

            // Get the selected search type
            String searchType = searchTypeComboBox.getValue();
            String lowerCaseFilter = filterText.toLowerCase();

            // Filter based on the selected search type
            switch (searchType) {
                case "Book Name":
                    return book.getBookName().toLowerCase().contains(lowerCaseFilter);
                case "Author":
                    return book.getAuthor().toLowerCase().contains(lowerCaseFilter);
                case "Genre":
                    return book.getGenre().toLowerCase().contains(lowerCaseFilter);
                case "ID":
                    return book.getId().toLowerCase().contains(lowerCaseFilter);
                case "Status":
                    return book.getStatus().toLowerCase().contains(lowerCaseFilter);
                default:
                    return false;
            }
        });
    }


    private void loadBooks() {
//        bookList.addAll(
//                new Book("The Great Gatsby", "F. Scott Fitzgerald", "Fiction", "1"),
//                new Book("1984", "George Orwell", "Dystopian", "2"),
//                new Book("To Kill a Mockingbird", "Harper Lee", "Classic", "3")
//        );
        String[][] AllBookInfo = Carrier.AllBooksRX("127.0.0.1", 12345);
        for (String[] bookInfo : AllBookInfo) {
            if (bookInfo.length == 5) {
                bookList.add(new Book(bookInfo[0], bookInfo[1], bookInfo[2], bookInfo[3], bookInfo[4]));
            }
        }
    }

    public void receiveAllBooks(String serverAddress, int serverPort) {
        try (Socket socket = new Socket(serverAddress, serverPort);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Receive data from server
            String response = in.readLine();
            System.out.println("Received from server: " + response); // TEST
            if (response != null) {
                String[] books = response.split(";");
                for (String book : books) {
                    String[] details = book.split("/");
                    System.out.println("Book Name: " + details[0]);
                    System.out.println("Author: " + details[1]);
                    System.out.println("Genre: " + details[2]);
                    System.out.println("ID: " + details[3]);
                    System.out.println("-----");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRowSelection() {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
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