package libman.librarymanager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminViewController {

    public Button updateBookButton;
    public TextField bookNameField;
    public TextField authorField;
    public TextField genreField;
    @FXML
    private TableView<Record> recordTable;

    @FXML
    private TableColumn<Record, String> recordIDColumn;

    @FXML
    public TableColumn<Record, String> userIDColumn;

    @FXML
    private TableColumn<Record, String> actionColumn;

    @FXML
    private TableColumn<Record, String> bookIDColumn;

    @FXML
    private TableColumn<Record, String> bookNameColumn;

    @FXML
    private TableColumn<Record, String> timeColumn;

    @FXML
    private TableView<Book> booksTable;

    @FXML
    private TableColumn<Book, String> Books_bookNameColumn;

    @FXML
    private TableColumn<Book, String> Books_authorColumn;

    @FXML
    private TableColumn<Book, String> Books_genreColumn;

    @FXML
    private TableColumn<Book, String> Books_idColumn;

    @FXML
    private TableColumn<Book, String> Books_statusColumn;

    private final ObservableList<Record> recordList = FXCollections.observableArrayList();
    private final ObservableList<Book> bookList = FXCollections.observableArrayList();

    @FXML
    private TableView<Book> popularBooksTable;

    @FXML
    private TableColumn<Book, String> p_bookNameColumn;
    @FXML
    private TableColumn<Book, String> p_authorColumn;
    @FXML
    private TableColumn<Book, String> p_genreColumn;
    @FXML
    private TableColumn<Book, String> p_idColumn;
    @FXML
    private TableColumn<Book, String> p_statusColumn;


    @FXML
    private TableColumn<Book, Integer> popularBookBorrowCountColumn;

    @FXML
    private PieChart genreBorrowingPieChart;

    String[][] AllRecords;
    private final ObservableList<Book> popularBooksList = FXCollections.observableArrayList();
    private final ObservableList<PieChart.Data> genreBorrowingData = FXCollections.observableArrayList();

    @FXML
    private TableView<UserExtended> userTable;

    @FXML
    private TableColumn<UserExtended, String> Users_userIDColumn;

    @FXML
    private TableColumn<UserExtended, String> Users_usernameColumn;

    @FXML
    private TableColumn<UserExtended, String> Users_passwordColumn;

    @FXML
    private TableColumn<UserExtended, String> Users_isBannedColumn;

    private final ObservableList<UserExtended> userList = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        // Ensure AllRecords is initialized
        if (AllRecords == null) {
            AllRecords = Carrier.AllRecordsRX("127.0.0.1", 12345); // Fetch records from Carrier
        }
        // Initialize the popular books table
        initializePopularBooksTable();
        loadPopularBooks();
        // Initialize the genre borrowing pie chart
        loadGenreBorrowingData();

        // Initialize the record page
        refreshRecordsPage();
        // Initialize the books page
        refreshBooksPage();

        setColumnWidths();

        // Users page TEST
        // Bind columns to UserExtended properties
        Users_userIDColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        Users_usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        Users_passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        Users_isBannedColumn.setCellValueFactory(new PropertyValueFactory<>("IsBanned"));

        // Load user data into the table
        loadUsers();
    }


    private void initializePopularBooksTable() {
        p_bookNameColumn.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        p_authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        p_genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        p_idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        p_statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        popularBooksTable.setItems(popularBooksList);
    }

    private void loadPopularBooks() {
        if (AllRecords == null) {
            System.out.println("AllRecords is null. Cannot load popular books.");
            return;
        }

        // Map to store borrow count for each book ID
        Map<String, Integer> borrowCountMap = new HashMap<>();

        // Calculate borrow count for each book ID from AllRecords
        for (String[] record : AllRecords) {
            if (record.length > 3) {
                String bookID = record[3];
                borrowCountMap.put(bookID, borrowCountMap.getOrDefault(bookID, 0) + 1);
            }
        }

        // Sort book IDs by borrow count in descending order
        List<String> topBookIDs = borrowCountMap.entrySet()
                .stream()
                .sorted((entry1, entry2) -> Integer.compare(entry2.getValue(), entry1.getValue()))
                .map(Map.Entry::getKey)
                .limit(5) // Get top 5 book IDs
                .toList();

        // Clear the existing list
        popularBooksList.clear();

        // Fetch book information for the top 5 book IDs and add to the list
        for (String bookID : topBookIDs) {
            String[] bookInfo = Carrier.getBookInfoByID("127.0.0.1", 12345, bookID);
            if (bookInfo.length == 6) {
                System.out.println("Adding popular book: " + bookInfo[0] + " by " + bookInfo[1] + " with ID: " + bookID);
                popularBooksList.add(new Book(bookInfo[1], bookInfo[2], bookInfo[3], bookInfo[4], bookInfo[5]));
            }
        }

        // Bind columns to Book properties
        p_bookNameColumn.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        p_authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        p_genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        p_idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        p_statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Refresh the table
        popularBooksTable.setItems(popularBooksList);
        popularBooksTable.refresh();
    }

    private void loadGenreBorrowingData() {
        // Map to store borrow count for each genre
        Map<String, Integer> genreBorrowingMap = new HashMap<>();

        // Calculate borrow count for each genre from AllRecords
        for (String[] record : AllRecords) {
            if (record.length > 3) {
                String bookID = record[3];
                String[] bookInfo = Carrier.getBookInfoByID("127.0.0.1", 12345, bookID);
                if (bookInfo.length > 2) {
                    String genre = bookInfo[3]; // Extract genre from bookInfo
                    genreBorrowingMap.put(genre, genreBorrowingMap.getOrDefault(genre, 0) + 1);
                }
            }
        }

        // Clear the existing data
        genreBorrowingData.clear();

        // Add genre data to the pie chart
        for (Map.Entry<String, Integer> entry : genreBorrowingMap.entrySet()) {
            genreBorrowingData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }
        genreBorrowingPieChart.setData(genreBorrowingData);
    }

    private void refreshBooksPage() {
        // Clear the existing books
        bookList.clear();

        // Bind columns to Book properties
        Books_bookNameColumn.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        Books_authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        Books_genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        Books_idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        Books_statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Set the ObservableList to the TableView
        booksTable.setItems(bookList);

        // Load books data
        loadBooks();
    }

    private void loadBooks() {
        String[][] AllBookInfo = Carrier.AllBooksRX("127.0.0.1", 12345);
        for (String[] bookInfo : AllBookInfo) {
            if (bookInfo.length == 5) {
                bookList.add(new Book(bookInfo[0], bookInfo[1], bookInfo[2], bookInfo[3], bookInfo[4]));
            }
        }
    }

    private void refreshRecordsPage() {
        // Clear the existing records
        recordList.clear();
        // Bind columns to Record properties
        recordIDColumn.setCellValueFactory(new PropertyValueFactory<>("recordID"));
        userIDColumn.setCellValueFactory(new PropertyValueFactory<>("userID"));
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("action"));
        bookIDColumn.setCellValueFactory(new PropertyValueFactory<>("bookID"));
        bookNameColumn.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        // Set the ObservableList to the TableView
        recordTable.setItems(recordList);
        loadRecords();
    }

    private void loadRecords() {
        AllRecords = Carrier.AllRecordsRX("127.0.0.1", 12345);
        for (String[] recordInfo : AllRecords) {
            if (recordInfo.length == 5) {
                String[] BookInfo = Carrier.getBookInfoByID("127.0.0.1", 12345, recordInfo[3]);
                String ThisBookName = "NotFound";
                ThisBookName = BookInfo[1];
                // Add the record with book name
                recordList.add(new RecordAdmin(recordInfo[0], recordInfo[1], recordInfo[2], recordInfo[3], ThisBookName, recordInfo[4]));
            }
        }
    }

    private void loadUsers() {
        // Fetch all user data from the server
        String[][] allUserInfo = Carrier.AllUsersRX("127.0.0.1", 12345);

        // Clear the existing user list
        userList.clear();

        // Populate the user list with data
        for (String[] userInfo : allUserInfo) {
            System.out.println("Length of userInfo: " + userInfo.length);

            if (userInfo.length == 5) {
                userList.add(new UserExtended(
                        userInfo[0], // ID
                        userInfo[1], // Username
                        userInfo[2], // Password
                        userInfo[4]  // IsBanned
                ));
            }
        }

        // Bind the user list to the TableView
        userTable.setItems(userList);
    }

    public void onUpdateBookClicked(ActionEvent actionEvent) {
        // Get the selected book from the table
        Book selectedBook = booksTable.getSelectionModel().getSelectedItem();

        if (selectedBook == null) {
            System.out.println("No book selected for update.");
            return;
        }

        // Get input values
        String bookID = selectedBook.getId();
        String bookName = bookNameField.getText();
        String author = authorField.getText();
        String genre = genreField.getText();

        // Validate inputs
        if (bookName.isEmpty() || author.isEmpty() || genre.isEmpty()) {
            System.out.println("All fields must be filled.");
            return;
        }

        // Call the updateBookInfoRequest method
        boolean success = Carrier.updateBookInfoRequest("127.0.0.1", 12345, bookID, bookName, author, genre);

        // Display result
        if (success) {
            System.out.println("Book information updated successfully.");
            refreshBooksPage(); // Refresh the table to reflect changes
        } else {
            System.out.println("Failed to update book information.");
        }
    }

    public void onAddBookClicked(ActionEvent actionEvent) {
        // Get input values
        String bookName = bookNameField.getText();
        String author = authorField.getText();
        String genre = genreField.getText();

        // Validate inputs
        if (bookName.isEmpty() || author.isEmpty() || genre.isEmpty()) {
            System.out.println("All fields must be filled.");
            return;
        }

        // Call the addBook method
        boolean success = Carrier.addBook("127.0.0.1", 12345, bookName, author, genre);

        // Display result
        if (success) {
            System.out.println("Book added successfully.");
            refreshBooksPage(); // Refresh the table to reflect changes
        } else {
            System.out.println("Failed to add book.");
        }
    }


    @FXML
    private Button deleteBookButton;

    public void onDeleteBookClicked(ActionEvent actionEvent) {
        // Get the selected book from the table
        Book selectedBook = booksTable.getSelectionModel().getSelectedItem();

        if (selectedBook == null) {
            System.out.println("No book selected for deletion.");
            return;
        }

        // Get the book ID
        String bookID = selectedBook.getId();

        // Call the deleteBookBybookID method
        boolean success = Carrier.deleteBookBybookID("127.0.0.1", 12345, bookID);

        // Display result
        if (success) {
            System.out.println("Book deleted successfully.");
            refreshBooksPage(); // Refresh the table to reflect changes
        } else {
            System.out.println("Failed to delete book.");
        }
    }

    private void setColumnWidths() {
        // Popular Books Table
        p_bookNameColumn.prefWidthProperty().bind(popularBooksTable.widthProperty().multiply(0.3));
        p_authorColumn.prefWidthProperty().bind(popularBooksTable.widthProperty().multiply(0.2));
        p_genreColumn.prefWidthProperty().bind(popularBooksTable.widthProperty().multiply(0.2));
        p_idColumn.prefWidthProperty().bind(popularBooksTable.widthProperty().multiply(0.15));
        p_statusColumn.prefWidthProperty().bind(popularBooksTable.widthProperty().multiply(0.15));

        // Books Table
        Books_bookNameColumn.prefWidthProperty().bind(booksTable.widthProperty().multiply(0.3));
        Books_authorColumn.prefWidthProperty().bind(booksTable.widthProperty().multiply(0.2));
        Books_genreColumn.prefWidthProperty().bind(booksTable.widthProperty().multiply(0.2));
        Books_idColumn.prefWidthProperty().bind(booksTable.widthProperty().multiply(0.15));
        Books_statusColumn.prefWidthProperty().bind(booksTable.widthProperty().multiply(0.15));

        // Records Table
        recordIDColumn.prefWidthProperty().bind(recordTable.widthProperty().multiply(0.2));
        userIDColumn.prefWidthProperty().bind(recordTable.widthProperty().multiply(0.1));
        actionColumn.prefWidthProperty().bind(recordTable.widthProperty().multiply(0.2));
        bookIDColumn.prefWidthProperty().bind(recordTable.widthProperty().multiply(0.1));
        bookNameColumn.prefWidthProperty().bind(recordTable.widthProperty().multiply(0.2));
        timeColumn.prefWidthProperty().bind(recordTable.widthProperty().multiply(0.2));
    }

    public void onBanUserClicked(ActionEvent actionEvent) {
        Carrier.banUser("127.0.0.1", 12345, getSelectedUser().getID());
        // 重新加载用户数据
        loadUsers();

        // 刷新用户表
        userTable.refresh();
    }

    public void onUnbanUserClicked(ActionEvent actionEvent) {
        Carrier.unbanUser("127.0.0.1", 12345, getSelectedUser().getID());
        // 重新加载用户数据
        loadUsers();

        // 刷新用户表
        userTable.refresh();
    }

    @FXML
    private UserExtended getSelectedUser() {
        // 获取选中的用户
        UserExtended selectedUser = userTable.getSelectionModel().getSelectedItem();

        if (selectedUser == null) {
            System.out.println("No user selected.");
        }

        return selectedUser;
    }
}