package libman.fakeend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Map;

class ClientHandler implements Runnable {
    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // 读取客户端发送的登录请求
            String request = in.readLine();
            System.out.println("Received: " + request);

            // 简单验证逻辑
            String[] credentials = request.split(":");
            if (credentials[0].equals("Login"))
            {
                String username = credentials[1];
                String password = credentials[2];
                boolean isAdmin = Boolean.parseBoolean(credentials[3]);

                String successMessage = "Login successful";
                String failureMessage = "Invalid username or password";
                String bannedMessage = "User is banned";
                String ReMessage = failureMessage;
                String usernameMessage = username;
                String userIDMessage = "114514";

                if (!isAdmin) {
                    String userID = AccessDB.getUserIDByUsername(username);
                    if (userID != null && AccessDB.checkUserBannedStatus(userID)) {
                        ReMessage = bannedMessage;
                    } else if (AccessDB.loginVerifyUser(username, password)) {
                        ReMessage = successMessage;
                    }
                } else {
                    if (AccessDB.loginVerifyAdmin(username, password)) {
                        ReMessage = successMessage;
                    }
                }
                out.println("Login" + ":" + ReMessage + ":" + usernameMessage + ":" + userIDMessage);
            }
            else if (credentials[0].equals("Register")) {
                String username = credentials[1];
                String password = credentials[2];

                // 简单注册逻辑，假设注册成功
                String successMessage = "Registration successful";
                String failureMessage = "Registration failed";

                // 这里可以添加检查用户名是否已存在的逻辑
                if (AccessDB.registerUser(username, password)) {
                    out.println("Register" + ":" + successMessage);
                } else {
                    out.println("Register" + ":" + failureMessage);
                }
            }
            else if (credentials[0].equals("CheckUsername")) {
                String username = credentials[1];

                // 假设用户名已存在
                if (AccessDB.checkUsernameExistence(username)) {
                    out.println("CheckUsername" + ":Exist");
                } else {
                    out.println("CheckUsername" + ":NotExist");
                }
            }
            else if (credentials[0].equals("AllBooksRX")) {
                String booksData = AccessDB.getAllBooks();
                System.out.println("Sending all books data: " + booksData); // TEST
                out.print(booksData);
            }
            else if (credentials[0].equals("AllRecordsRX")) {
                String recordsData = AccessDB.getAllRecords();
                System.out.println("Sending all records data: " + recordsData); // TEST
                out.print(recordsData);
            }
            else if (credentials[0].equals("AllUsersRX")) {
                String usersData = AccessDB.getAllUsers();
                System.out.println("Sending all users data: " + usersData); // TEST
                out.print(usersData);
            }
            else if (credentials[0].equals("RecommendationsRX")) {
                String userID = credentials[1];
                List<String> RecBooksID = AccessDB.recommendBooksForUser(userID);
                String responseMessage = String.join("/", RecBooksID);
                out.print(responseMessage);
            }
            else if (credentials[0].equals("UpdateBookStatus")) {
                String BookID = credentials[1];
                String status = credentials[2];
                String userID = credentials[3];
                boolean success = AccessDB.setBookStatus(BookID, status, userID);
                String responseMessage = success ? "Update successful" : "Update failed";
                out.println("UpdateBookStatus" + ":" + responseMessage);
            }
            else if (credentials[0].equals("GetUserIDbyUsername")) {
                String username = credentials[1];
                String userID = AccessDB.getUserIDByUsername(username);
                if (userID != null) {
                    out.println("GetUserIDbyUsername" + ":" + userID);
                } else {
                    out.println("GetUserIDbyUsername" + ":NotFound");
                }
            }
            else if (credentials[0].equals("GetAdminIDbyUsername")) {
                String username = credentials[1];
                String userID = AccessDB.getAdminIDByUsername(username);
                if (userID != null) {
                    out.println("GetAdminIDbyUsername" + ":" + userID);
                } else {
                    out.println("GetAdminIDbyUsername" + ":NotFound");
                }
            }
            else if (credentials[0].equals("GetBookInfoByID")) {
                String bookID = credentials[1];
                String bookInfo = AccessDB.getBookInfoByID(bookID);
                if (bookInfo.equals("NotFound")) {
                    out.println("GetBookInfoByID" + "/NotFound");
                } else {
                    out.println("GetBookInfoByID" + "/" + bookInfo);
                }
            }
            else if (credentials[0].equals("GetUserUnreturnedBooks")) {
                String userID = credentials[1];
                Map<String, Integer> unreturnedBooks = AccessDB.getUserUnreturnedBooks(userID);
                System.out.println("Unreturned books number for user " + userID + ": " + unreturnedBooks.size()); // TEST
                StringBuilder response = new StringBuilder("GetUserUnreturnedBooks");
                for (Map.Entry<String, Integer> entry : unreturnedBooks.entrySet()) {
                    response.append("/").append(entry.getKey()).append(":").append(entry.getValue());
                }
                out.println(response.toString());
                System.out.println("Sent unreturned books for user " + userID + ": " + response.toString()); // TEST
            }
            else if (credentials[0].equals("UpdateBookInfo")) {
                String bookID = credentials[1];
                String newBookName = credentials[2];
                String newAuthor = credentials[3];
                String newGenre = credentials[4];

                boolean success = AccessDB.updateBookInfo(bookID, newBookName, newAuthor, newGenre);
                String responseMessage = success ? "Success" : "Failed";
                out.println("UpdateBookInfo" + ":" + responseMessage);
            }
            else if (credentials[0].equals("AddBook")) {
                String bookName = credentials[1];
                String author = credentials[2];
                String genre = credentials[3];

                boolean success = AccessDB.addBook(bookName, author, genre);
                String responseMessage = success ? "Success" : "Failed";
                out.println("AddBook" + ":" + responseMessage);
            }
            else if (credentials[0].equals("DeleteBookBybookID")) {
                String bookID = credentials[1];

                boolean success = AccessDB.deleteBookBybookID(bookID);
                String responseMessage = success ? "Success" : "Failed";
                out.println("DeleteBookBybookID" + ":" + responseMessage);
            }
            else if (credentials[0].equals("BanUser")) {
                String userID = credentials[1];
                boolean success = AccessDB.banUser(userID);
                String responseMessage = success ? "Success" : "Failed";
                out.println("BanUser" + ":" + responseMessage);
            }
            else if (credentials[0].equals("UnbanUser")) {
                String userID = credentials[1];
                boolean success = AccessDB.unbanUser(userID);
                String responseMessage = success ? "Success" : "Failed";
                out.println("UnbanUser" + ":" + responseMessage);
            }
            else if (credentials[0].equals("CheckUserBanned")) {
                String userID = credentials[1];
                boolean isBanned = AccessDB.checkUserBannedStatus(userID);
                String responseMessage = isBanned ? "Banned" : "Unbanned";
                out.println("CheckUserBanned" + ":" + responseMessage);
            }
            else {
                out.println("Unknown request");
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}