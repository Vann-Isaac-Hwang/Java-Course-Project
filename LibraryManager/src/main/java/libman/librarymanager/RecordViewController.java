package libman.librarymanager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class RecordViewController {

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Record> recordTable;

    @FXML
    private TableColumn<Record, String> recordIDColumn;

    @FXML
    private TableColumn<Record, String> actionColumn;

    @FXML
    private TableColumn<Record, String> bookIDColumn;

    @FXML
    private TableColumn<Record, String> bookNameColumn;

    @FXML
    private TableColumn<Record, String> timeColumn;

    private final ObservableList<Record> recordList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Set up the columns
        recordIDColumn.setCellValueFactory(new PropertyValueFactory<>("recordID"));
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("action"));
        bookIDColumn.setCellValueFactory(new PropertyValueFactory<>("bookID"));
        bookNameColumn.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));

        // Bind column width proportionally
        recordIDColumn.prefWidthProperty().bind(recordTable.widthProperty().multiply(0.15)); // 15%
        actionColumn.prefWidthProperty().bind(recordTable.widthProperty().multiply(0.1));   // 10%
        bookIDColumn.prefWidthProperty().bind(recordTable.widthProperty().multiply(0.1));   // 10%
        bookNameColumn.prefWidthProperty().bind(recordTable.widthProperty().multiply(0.3)); // 30%
        timeColumn.prefWidthProperty().bind(recordTable.widthProperty().multiply(0.35));    // 35%

        // Load sample data
        loadRecords();

        // Create a filtered list
        FilteredList<Record> filteredRecords = new FilteredList<>(recordList, r -> true);

        // Bind the filtered list to the table
        recordTable.setItems(filteredRecords);

        // Add a listener to the search field
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredRecords.setPredicate(record -> {
                // If the search field is empty, display all records
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare record fields with the search keyword
                String lowerCaseFilter = newValue.toLowerCase();
                return record.getRecordID().toLowerCase().contains(lowerCaseFilter)
                        || record.getAction().toLowerCase().contains(lowerCaseFilter)
                        || record.getBookID().toLowerCase().contains(lowerCaseFilter)
                        || record.getBookName().toLowerCase().contains(lowerCaseFilter)
                        || record.getTime().toLowerCase().contains(lowerCaseFilter);
            });
        });
    }

    private void loadRecords() {
        // Add sample data (replace with actual data retrieval logic if needed)
//        recordList.addAll(
//                new Record("1", "Borrow", "101", "The Great Gatsby", "2023-10-01"),
//                new Record("2", "Return", "102", "1984", "2023-10-02"),
//                new Record("3", "Borrow", "103", "To Kill a Mockingbird", "2023-10-03")
//        );
        String[][] AllRecords = Carrier.AllRecordsRX("127.0.0.1", 12345);
        for (String[] recordInfo : AllRecords) {
            if (recordInfo.length == 5) {
                String[] BookInfo = Carrier.getBookInfoByID("127.0.0.1", 12345, recordInfo[3]);
                String ThisBookName = "NotFound";
                ThisBookName = BookInfo[1];
                // Add the record with book name
                recordList.add(new Record(recordInfo[0], recordInfo[2], recordInfo[3], ThisBookName, recordInfo[4]));

            }
        }
    }
}