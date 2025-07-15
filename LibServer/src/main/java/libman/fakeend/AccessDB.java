package libman.fakeend;

import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class AccessDB {
    private static final String DB_URL = "jdbc:sqlite:libman.db";

    public AccessDB() {
        // 初始化数据库连接并创建表
        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            if (connection != null) {
                System.out.println("Connected to SQLite database.");
                createTables(connection);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean addAdmin(String username, String password, String phoneNumber) {
        String insertSQL = "INSERT INTO Admins (Username, Password, PhoneNumber) VALUES (?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, phoneNumber);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteAdmin(String adminID) {
        String deleteSQL = "DELETE FROM Admins WHERE ID = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setString(1, adminID);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void printAllAdmins() {
        String query = "SELECT ID, Username, Password, PhoneNumber FROM Admins";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String username = resultSet.getString("Username");
                String password = resultSet.getString("Password");
                String phoneNumber = resultSet.getString("PhoneNumber");

                System.out.println("ID: " + id + ", Username: " + username + ", Password: " + password + ", PhoneNumber: " + phoneNumber);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getUserIDByUsername(String username) {
        String query = "SELECT ID FROM Users WHERE Username = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return String.valueOf(resultSet.getInt("ID"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getAdminIDByUsername(String username) {
        String query = "SELECT ID FROM Admins WHERE Username = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return String.valueOf(resultSet.getInt("ID"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void createTables(Connection connection) {
        String createUserTableSQL = """
            CREATE TABLE IF NOT EXISTS Users (
                ID INTEGER PRIMARY KEY AUTOINCREMENT,
                Username TEXT NOT NULL UNIQUE,
                Password TEXT NOT NULL,
                PhoneNumber TEXT
            );
            """;

        String createAdminTableSQL = """
            CREATE TABLE IF NOT EXISTS Admins (
                ID INTEGER PRIMARY KEY AUTOINCREMENT,
                Username TEXT NOT NULL UNIQUE,
                Password TEXT NOT NULL,
                PhoneNumber TEXT
            );
            """;

        String createBooksTableSQL = """
            CREATE TABLE IF NOT EXISTS Books (
                ID INTEGER PRIMARY KEY AUTOINCREMENT,
                BookName TEXT NOT NULL,
                Author TEXT NOT NULL,
                Genre TEXT NOT NULL,
                BookID TEXT NOT NULL UNIQUE,
                Status TEXT NOT NULL DEFAULT 'Available'
            );
            """;

        String createBorrowingRecordsTableSQL = """
            CREATE TABLE IF NOT EXISTS BorrowingRecords (
                ID INTEGER PRIMARY KEY AUTOINCREMENT,
                Borrower TEXT NOT NULL,
                Action TEXT NOT NULL CHECK (Action IN ('Borrow', 'Return')),
                BookID TEXT NOT NULL,
                Timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (BookID) REFERENCES Books(BookID)
            );
            """;

        try (Statement statement = connection.createStatement()) {
            statement.execute(createUserTableSQL);
            statement.execute(createAdminTableSQL);
            statement.execute(createBooksTableSQL);
            statement.execute(createBorrowingRecordsTableSQL);
            System.out.println("Tables created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Add borrower TESt
        String alterBooksTableSQL = """
    ALTER TABLE Books ADD COLUMN Borrower TEXT DEFAULT NULL;
""";

        try (Statement statement = connection.createStatement()) {
            statement.execute(alterBooksTableSQL);
            System.out.println("Borrower column added to Books table.");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Add banned status to user TESt
        String alterUsersTableSQL = """
    ALTER TABLE Users ADD COLUMN IsBanned INTEGER DEFAULT 0;
""";

        try (Statement statement = connection.createStatement()) {
            statement.execute(alterUsersTableSQL);
            System.out.println("IsBanned column added to Users table.");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static boolean banUser(String userID) {
        String updateSQL = "UPDATE Users SET IsBanned = 1 WHERE ID = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            preparedStatement.setString(1, userID);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean unbanUser(String userID) {
        String updateSQL = "UPDATE Users SET IsBanned = 0 WHERE ID = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            preparedStatement.setString(1, userID);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean checkUserBannedStatus(String userID) {
        String query = "SELECT IsBanned FROM Users WHERE ID = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, userID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("IsBanned") == 1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean addBook(String bookName, String author, String genre, String bookID) {
        String insertSQL = "INSERT INTO Books (BookName, Author, Genre, BookID, Status) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, bookName);
            preparedStatement.setString(2, author);
            preparedStatement.setString(3, genre);
            preparedStatement.setString(4, bookID);
            preparedStatement.setString(5, "Available"); // Set initial status to 'Available'
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean setBookStatus(String bookID, String status, String userID) {
        String checkStatusSQL = "SELECT Status FROM Books WHERE BookID = ?";
        String updateSQL = "UPDATE Books SET Status = ?, Borrower = ? WHERE BookID = ?";
        String insertRecordSQL = "INSERT INTO BorrowingRecords (Borrower, Action, BookID) VALUES (?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            // Check current status of the book
            String currentStatus;
            try (PreparedStatement checkStatement = connection.prepareStatement(checkStatusSQL)) {
                checkStatement.setString(1, bookID);
                try (ResultSet resultSet = checkStatement.executeQuery()) {
                    if (resultSet.next()) {
                        currentStatus = resultSet.getString("Status");
                    } else {
                        // Book not found
                        return false;
                    }
                }
            }

            // Validate the action based on the current status
            if ("Borrowed".equalsIgnoreCase(currentStatus) && "Borrowed".equalsIgnoreCase(status)) {
                return false; // Book is already borrowed
            }
            if ("Available".equalsIgnoreCase(currentStatus) && "Available".equalsIgnoreCase(status)) {
                return false; // Book is already available
            }

            // Start a transaction
            connection.setAutoCommit(false);

            // Update book status and borrower
            try (PreparedStatement updateStatement = connection.prepareStatement(updateSQL)) {
                updateStatement.setString(1, status);
                updateStatement.setString(2, "Available".equalsIgnoreCase(status) ? null : userID);
                updateStatement.setString(3, bookID);
                int rowsAffected = updateStatement.executeUpdate();
                if (rowsAffected == 0) {
                    connection.rollback();
                    return false;
                }
            }

            // Determine action based on status
            String action = "Available".equalsIgnoreCase(status) ? "Return" : "Borrow";

            // Insert borrowing record
            try (PreparedStatement insertStatement = connection.prepareStatement(insertRecordSQL)) {
                insertStatement.setString(1, userID);
                insertStatement.setString(2, action);
                insertStatement.setString(3, bookID);
                int rowsInserted = insertStatement.executeUpdate();
                if (rowsInserted == 0) {
                    connection.rollback();
                    return false;
                }
            }

            // Commit transaction
            connection.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getBookInfoByID(String bookID) {
        String query = "SELECT BookName, Author, Genre, BookID, Status FROM Books WHERE BookID = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, bookID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("BookName") + "/" +
                           resultSet.getString("Author") + "/" +
                           resultSet.getString("Genre") + "/" +
                           resultSet.getString("BookID") + "/" +
                           resultSet.getString("Status");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "NotFound";
    }

    public static String getAllBooks() {
        StringBuilder booksData = new StringBuilder();
        String query = "SELECT BookName, Author, Genre, BookID, Status FROM Books";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                booksData.append(resultSet.getString("BookName")).append("/")
                        .append(resultSet.getString("Author")).append("/")
                        .append(resultSet.getString("Genre")).append("/")
                        .append(resultSet.getString("BookID")).append("/")
                        .append(resultSet.getString("Status")).append(";");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Remove trailing semicolon if data exists
        if (booksData.length() > 0) {
            booksData.setLength(booksData.length() - 1);
        }

        return booksData.toString();
    }

    public static boolean updateBookInfo(String bookID, String newBookName, String newAuthor, String newGenre) {
        String updateSQL = "UPDATE Books SET BookName = ?, Author = ?, Genre = ? WHERE BookID = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            preparedStatement.setString(1, newBookName);
            preparedStatement.setString(2, newAuthor);
            preparedStatement.setString(3, newGenre);
            preparedStatement.setString(4, bookID);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean addBook(String bookName, String author, String genre) {
        String getMaxBookIDSQL = "SELECT MAX(CAST(BookID AS INTEGER)) AS MaxBookID FROM Books";
        String insertSQL = "INSERT INTO Books (BookName, Author, Genre, BookID, Status) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            // Get the maximum bookID
            int newBookID = 1; // Default to 1 if no books exist
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(getMaxBookIDSQL)) {
                if (resultSet.next()) {
                    newBookID = resultSet.getInt("MaxBookID") + 1;
                }
            }

            // Insert the new book
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
                preparedStatement.setString(1, bookName);
                preparedStatement.setString(2, author);
                preparedStatement.setString(3, genre);
                preparedStatement.setString(4, String.valueOf(newBookID)); // Set new bookID
                preparedStatement.setString(5, "Available"); // Set initial status to 'Available'
                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteBookBybookID(String bookID) {
        String deleteSQL = "DELETE FROM Books WHERE BookID = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setString(1, bookID);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getAllRecords() {
        StringBuilder recordsData = new StringBuilder();
        String query = "SELECT ID, Borrower, Action, BookID, Timestamp FROM BorrowingRecords";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                recordsData.append(resultSet.getInt("ID")).append("/")
                        .append(resultSet.getString("Borrower")).append("/")
                        .append(resultSet.getString("Action")).append("/")
                        .append(resultSet.getString("BookID")).append("/")
                        .append(resultSet.getString("Timestamp")).append(";");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Remove trailing semicolon if data exists
        if (recordsData.length() > 0) {
            recordsData.setLength(recordsData.length() - 1);
        }

        return recordsData.toString();
    }

    public static String getAllUsers() {
        StringBuilder usersData = new StringBuilder();
        String query = "SELECT ID, Username, Password, PhoneNumber, IsBanned FROM Users";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                usersData.append(resultSet.getInt("ID")).append("/")
                         .append(resultSet.getString("Username")).append("/")
                         .append(resultSet.getString("Password")).append("/")
                         .append(resultSet.getString("PhoneNumber")).append("/")
                         .append(resultSet.getInt("IsBanned")).append(";");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Remove trailing semicolon if data exists
        if (usersData.length() > 0) {
            usersData.setLength(usersData.length() - 1);
        }

        return usersData.toString();
    }

    public static boolean addRecord(String borrowerID, String action, String bookID) {
        String insertSQL = "INSERT INTO BorrowingRecords (Borrower, Action, BookID) VALUES (?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, borrowerID);
            preparedStatement.setString(2, action);
            preparedStatement.setString(3, bookID);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteRecordByID(int recordID) {
        String deleteSQL = "DELETE FROM BorrowingRecords WHERE ID = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, recordID);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateRecord(int recordID, String borrowerID, String action, String bookID) {
        String updateSQL = "UPDATE BorrowingRecords SET Borrower = ?, Action = ?, BookID = ? WHERE ID = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            preparedStatement.setString(1, borrowerID);
            preparedStatement.setString(2, action);
            preparedStatement.setString(3, bookID);
            preparedStatement.setInt(4, recordID);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean checkUsernameExistence(String username) {
        String query = "SELECT COUNT(*) FROM Users WHERE Username = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean registerUser(String username, String password) {
        String insertSQL = "INSERT INTO Users (Username, Password) VALUES (?, ?)";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean loginVerifyUser(String username, String password) {
        String query = "SELECT COUNT(*) FROM Users WHERE Username = ? AND Password = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean loginVerifyAdmin(String username, String password) {
        String query = "SELECT COUNT(*) FROM Admins WHERE Username = ? AND Password = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Below shows code for book recommender--------------------------------------------------
    // Method to get book rankings by genre
    public static Map<String, List<String>> getBookRankingsByGenre() {
        Map<String, List<String>> genreRankings = new HashMap<>();
        String query = """
        SELECT Books.Genre, Books.BookID, COUNT(*) AS BorrowCount
        FROM BorrowingRecords
        JOIN Books ON BorrowingRecords.BookID = Books.BookID
        GROUP BY Books.Genre, Books.BookID
        ORDER BY Books.Genre, BorrowCount DESC
    """;

        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String genre = resultSet.getString("Genre");
                String bookID = resultSet.getString("BookID");
                genreRankings.computeIfAbsent(genre, k -> new ArrayList<>()).add(bookID);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Output
        for (Map.Entry<String, List<String>> entry : genreRankings.entrySet()) {
            System.out.println("Genre: " + entry.getKey() + ", Books: " + entry.getValue());
        }
        return genreRankings;
    }

    public static List<String> recommendBooksForUser(String userID) {
        Map<String, Integer> userGenreCounts = new HashMap<>();
        Set<String> userBorrowedBooks = new HashSet<>();
        Map<String, List<String>> genreRankings = getBookRankingsByGenre();

        // Step 1: Get user's borrowing history
        String userHistoryQuery = """
            SELECT Books.Genre, BorrowingRecords.BookID
            FROM BorrowingRecords
            JOIN Books ON BorrowingRecords.BookID = Books.BookID
            WHERE BorrowingRecords.Borrower = ?
        """;

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(userHistoryQuery)) {
            preparedStatement.setString(1, userID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String genre = resultSet.getString("Genre");
                    String bookID = resultSet.getString("BookID");
                    userGenreCounts.put(genre, userGenreCounts.getOrDefault(genre, 0) + 1);
                    userBorrowedBooks.add(bookID);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Step 2: Find top 3 genres the user borrows
        List<String> topGenres = userGenreCounts.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // Step 3: Recommend books from top genres
        List<String> recommendations = new ArrayList<>();
        int[] booksToRecommend = {3, 2, 1}; // Number of books to recommend for each genre

        for (int i = 0; i < topGenres.size(); i++) {
            String genre = topGenres.get(i);
            List<String> rankedBooks = genreRankings.getOrDefault(genre, Collections.emptyList());

            // Find books the user has not borrowed
            List<String> unborrowedBooks = rankedBooks.stream()
                    .filter(bookID -> !userBorrowedBooks.contains(bookID))
                    .collect(Collectors.toList());

            // Add recommendations
            recommendations.addAll(unborrowedBooks.stream()
                    .limit(booksToRecommend[i])
                    .collect(Collectors.toList()));

            // If not enough unborrowed books, add already borrowed ones
            if (recommendations.size() < booksToRecommend[i]) {
                recommendations.addAll(rankedBooks.stream()
                        .limit(booksToRecommend[i] - recommendations.size())
                        .collect(Collectors.toList()));
            }
        }

        // Step 4: Fill recommendations to 6 books if needed
        if (recommendations.size() < 6) {
            String allBooksQuery = "SELECT BookID FROM Books";
            List<String> allBooks = new ArrayList<>();

            try (Connection connection = DriverManager.getConnection(DB_URL);
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(allBooksQuery)) {
                while (resultSet.next()) {
                    allBooks.add(resultSet.getString("BookID"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Filter out already recommended and borrowed books
            List<String> additionalBooks = allBooks.stream()
                    .filter(bookID -> !recommendations.contains(bookID) && !userBorrowedBooks.contains(bookID))
                    .collect(Collectors.toList());

            // Randomly select books to fill the list
            Collections.shuffle(additionalBooks);
            recommendations.addAll(additionalBooks.stream()
                    .limit(6 - recommendations.size())
                    .collect(Collectors.toList()));

            // If still not enough, include already borrowed books
            if (recommendations.size() < 6) {
                List<String> remainingBooks = allBooks.stream()
                        .filter(bookID -> !recommendations.contains(bookID))
                        .collect(Collectors.toList());
                Collections.shuffle(remainingBooks);
                recommendations.addAll(remainingBooks.stream()
                        .limit(6 - recommendations.size())
                        .collect(Collectors.toList()));
            }
        }

        return recommendations;
    }

    public static Map<String, Integer> getUserUnreturnedBooks(String userID) {
        Map<String, Integer> unreturnedBooks = new HashMap<>();
        String query = """
            SELECT b.BookID, br.Timestamp AS BorrowTimestamp
            FROM Books b
            JOIN BorrowingRecords br ON b.BookID = br.BookID
            WHERE b.Borrower = ? AND br.Action = 'Borrow'
        """;

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, userID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String bookID = resultSet.getString("BookID");
                    Timestamp borrowTimestamp = resultSet.getTimestamp("BorrowTimestamp");

                    // Calculate the number of days borrowed
                    long millisBorrowed = System.currentTimeMillis() - borrowTimestamp.getTime();
                    int daysBorrowed = (int) Math.max(1, Math.ceil(millisBorrowed / (1000.0 * 60 * 60 * 24)));

                    unreturnedBooks.put(bookID, daysBorrowed);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return unreturnedBooks;
    }

    public static void main(String[] args) {
        new AccessDB();
    }
}