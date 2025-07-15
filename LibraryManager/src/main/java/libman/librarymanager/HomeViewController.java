package libman.librarymanager;

import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

import java.util.Map;

public class HomeViewController {
    @FXML
    public Label bookName1, bookName2, bookName3, bookName4, bookName5, bookName6,
            bookName7, bookName8, bookName9, bookName10, bookName11;
    @FXML
    public Label bookGenre1, bookGenre2, bookGenre3, bookGenre4, bookGenre5, bookGenre6,
            bookGenre7, bookGenre8, bookGenre9, bookGenre10, bookGenre11;
    @FXML
    public Button bookButton1, bookButton2, bookButton3, bookButton4, bookButton5, bookButton6,
            bookButton7, bookButton8, bookButton9, bookButton10, bookButton11;
    @FXML
    private ScrollPane bookScrollPane;
    @FXML
    private HBox customHBox;
    @FXML
    private ScrollPane bookScrollPane1; // 下部分的 ScrollPane
    @FXML
    private HBox customHBox1; // 下部分的 HBox

    private double scrollVelocity = 0;  // 滚动速度
    private final double DECELERATION = 0.95; // 减速度（0.95 = 平滑停止）
    private AnimationTimer scrollTimer;
    private final ObservableList<Book> recommendedBooks = FXCollections.observableArrayList();
    private final ObservableList<Book> unreturnedBooks = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // 在customHBox1中删除children，child名为thchild
//        customHBox1.getChildren().removeIf(child -> "bookButton8".equals(child.getId()));

        // 为上部分和下部分设置平滑滚动
        setupSmoothScrolling(bookScrollPane, customHBox);
        setupSmoothScrolling(bookScrollPane1, customHBox1);

        // TEST getUserUnreturnedBooks function in Carrier
        Map<String, Integer> UnreturnedBook = Carrier.getUserUnreturnedBooks("127.0.0.1", 12345, AMController.getCurrentUserID());
        int numUnreturnedBooks = UnreturnedBook.size();
        System.out.println("Number of unreturned books: " + numUnreturnedBooks);

        // 加载推荐书籍
        loadRecommendedBooks();

        // 将推荐书籍添加到 HBox 中的六个按钮
        for (int i = 0; i < recommendedBooks.size() && i < 6; i++) {
            Book book = recommendedBooks.get(i);
            // TEST, print book details
            System.out.println("Book " + (i + 1) + ": " + book.getBookName() + ", Author: " + book.getAuthor() +
                    ", Genre: " + book.getGenre() + ", ID: " + book.getId() + ", Status: " + book.getStatus());
            switch (i) {
                case 0 -> {
                    bookName1.setText(book.getBookName());
                    bookGenre1.setText(book.getGenre());
                }
                case 1 -> {
                    bookName2.setText(book.getBookName());
                    bookGenre2.setText(book.getGenre());
                }
                case 2 -> {
                    bookName3.setText(book.getBookName());
                    bookGenre3.setText(book.getGenre());
                }
                case 3 -> {
                    bookName4.setText(book.getBookName());
                    bookGenre4.setText(book.getGenre());
                }
                case 4 -> {
                    bookName5.setText(book.getBookName());
                    bookGenre5.setText(book.getGenre());
                }
                case 5 -> {
                    bookName6.setText(book.getBookName());
                    bookGenre6.setText(book.getGenre());
                }
            }
        }

        // 加载未归还书籍
        loadUnreturnedBooks();

        // 将未归还书籍添加到下部分的 HBox 中的按钮
        int num_unreturned = unreturnedBooks.size();
        for (int i = 0; i < num_unreturned && i < 5; i++) {
            Book book = unreturnedBooks.get(i);
            // TEST, print book details
            System.out.println("Unreturned Book " + (i + 1) + ": " + book.getBookName() + ", Author: " + book.getAuthor() +
                    ", Genre: " + book.getGenre() + ", ID: " + book.getId() + ", Status: " + book.getStatus());
            switch (i) {
                case 0 -> {
                    bookName7.setText(book.getBookName());
                    bookGenre7.setText(book.getGenre());
                }
                case 1 -> {
                    bookName8.setText(book.getBookName());
                    bookGenre8.setText(book.getGenre());
                }
                case 2 -> {
                    bookName9.setText(book.getBookName());
                    bookGenre9.setText(book.getGenre());
                }
                case 3 -> {
                    bookName10.setText(book.getBookName());
                    bookGenre10.setText(book.getGenre());
                }
                case 4 -> {
                    bookName11.setText(book.getBookName());
                    bookGenre11.setText(book.getGenre());
                }
            }
        }
        // 根据num_unreturned删除没用的button
        if (num_unreturned < 5) {
            customHBox1.getChildren().removeIf(child -> child instanceof Button && child.getId() != null && child.getId().startsWith("bookButton") && Integer.parseInt(child.getId().substring(10)) > num_unreturned + 6);
        }
    }

    private void setupSmoothScrolling(ScrollPane scrollPane, HBox hBox) {
        // 1. 确保 HBox 可以横向扩展
        hBox.setMinWidth(Region.USE_PREF_SIZE);
        hBox.setPrefWidth(Region.USE_COMPUTED_SIZE);

        // 2. 监听鼠标滚轮事件
        scrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
            if (event.getDeltaY() != 0) {
                // 根据滚轮速度设置初始滚动速度
                scrollVelocity = -event.getDeltaY() * 0.0008; // 调整系数控制灵敏度
                event.consume(); // 阻止默认滚动行为
            }
        });

        // 3. 惯性滚动动画
        scrollTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (Math.abs(scrollVelocity) > 0.001) { // 微小速度时停止
                    double newHvalue = scrollPane.getHvalue() + scrollVelocity;
                    // 限制在 [0, 1] 范围内
                    newHvalue = Math.max(0, Math.min(1, newHvalue));
                    scrollPane.setHvalue(newHvalue);
                    scrollVelocity *= DECELERATION; // 速度递减
                } else {
                    scrollVelocity = 0; // 停止
                }
            }
        };
        scrollTimer.start();
    }

    private void loadRecommendedBooks() {
        // 假设 getRecommendedBooks() 返回一个 Book 对象的列表
        recommendedBooks.addAll(Carrier.getRecommendedBooks("127.0.0.1", 12345, AMController.getCurrentUserID()));
    }

    private void loadUnreturnedBooks() {
        Map<String, Integer> UnreturnedBookIDAndDays
                = Carrier.getUserUnreturnedBooks("127.0.0.1", 12345, AMController.getCurrentUserID());
        unreturnedBooks.clear(); // Clear the previous list of unreturned books

        // Iterate over the unreturned book IDs and days
        for (Map.Entry<String, Integer> entry : UnreturnedBookIDAndDays.entrySet()) {
            String bookID = entry.getKey();
            int overdueDays = entry.getValue();

            // Retrieve book details by ID
            String[] bookInfo = Carrier.getBookInfoByID("127.0.0.1", 12345, bookID);

            // Ensure the bookInfo array has the expected length
            if (bookInfo.length == 6) {
                // Create a new Book object and add it to the unreturnedBooks list
                Book book = new Book(bookInfo[1], bookInfo[2], bookInfo[3], bookInfo[4], bookInfo[5]);
                unreturnedBooks.add(book);

                // Optionally, print the book details and overdue days for debugging
                System.out.println("Unreturned Book: " + book.getBookName() + ", Overdue Days: " + overdueDays);
            }
        }
    }

    public void onbookButton1Click(MouseEvent mouseEvent) {selectBookShown(0);}
    public void onbookButton2Click(MouseEvent mouseEvent) {selectBookShown(1);}
    public void onbookButton3Click(MouseEvent mouseEvent) {selectBookShown(2);}
    public void onbookButton4Click(MouseEvent mouseEvent) {selectBookShown(3);}
    public void onbookButton5Click(MouseEvent mouseEvent) {selectBookShown(4);}
    public void onbookButton6Click(MouseEvent mouseEvent) {selectBookShown(5);}

    public void onbookButton7Click(MouseEvent mouseEvent) {selectBookShown(6);}
    public void onbookButton8Click(MouseEvent mouseEvent) {selectBookShown(7);}
    public void onbookButton9Click(MouseEvent mouseEvent) {selectBookShown(8);}
    public void onbookButton10Click(MouseEvent mouseEvent) {selectBookShown(9);}
    public void onbookButton11Click(MouseEvent mouseEvent) {selectBookShown(10);}


    public void selectBookShown(int i) {
        Book selectedBook;
        if (i<6){
            selectedBook = recommendedBooks.get(i);
        }
        else {
            selectedBook = unreturnedBooks.get(i - 6);
        }
        if (selectedBook != null) {
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