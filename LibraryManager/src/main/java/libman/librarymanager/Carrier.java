package libman.librarymanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Carrier {
    public static String[][] AllBooksRX(String serverAddress, int serverPort) {
        String[] tokensGroups = new String[0];
        String[][] tokens = new String[0][];
        try (Socket socket = new Socket(serverAddress, serverPort);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // 发送获取所有书籍信息请求
            out.println("AllBooksRX");

            // 接收后端返回的结果
            String response = in.readLine();
            System.out.println("Server response: " + response); // TEST

            tokensGroups = response.split(";");

            // 将每个书籍信息分割成单独的字段
            tokens = new String[tokensGroups.length][];
            for (int i = 0; i < tokensGroups.length; i++) {
                tokens[i] = tokensGroups[i].split("/");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        // Print all the books in tokens
        for (String[] book : tokens) {
            for (String detail : book) {
                System.out.print(detail + " ");
            }
            System.out.println();
        }
        return tokens;
    }

    public static String[][] AllRecordsRX(String serverAddress, int serverPort) {
        String[] tokensGroups = new String[0];
        String[][] tokens = new String[0][];
        try (Socket socket = new Socket(serverAddress, serverPort);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // 发送获取所有借阅记录请求
            out.println("AllRecordsRX");

            // 接收后端返回的结果
            String response = in.readLine();
            System.out.println("Server response: " + response); // TEST

            tokensGroups = response.split(";");

            // 将每个借阅记录信息分割成单独的字段
            tokens = new String[tokensGroups.length][];
            for (int i = 0; i < tokensGroups.length; i++) {
                tokens[i] = tokensGroups[i].split("/");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        // Print all the records in tokens
        for (String[] record : tokens) {
            for (String detail : record) {
                System.out.print(detail + " ");
            }
            System.out.println();
        }
        return tokens;
    }

    public static String[][] AllUsersRX(String serverAddress, int serverPort) {
        String[] tokensGroups = new String[0];
        String[][] tokens = new String[0][];
        try (Socket socket = new Socket(serverAddress, serverPort);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // 发送获取所有用户信息请求
            out.println("AllUsersRX");

            // 接收后端返回的结果
            String response = in.readLine();
            System.out.println("Server response: " + response); // TEST

            tokensGroups = response.split(";");

            // 将每个用户信息分割成单独的字段
            tokens = new String[tokensGroups.length][];
            for (int i = 0; i < tokensGroups.length; i++) {
                tokens[i] = tokensGroups[i].split("/");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        // Print all the users in tokens
        for (String[] user : tokens) {
            for (String detail : user) {
                System.out.print(detail + " ");
            }
            System.out.println();
        }
        return tokens;
    }

    public static boolean banUser(String serverAddress, int serverPort, String userID) {
        boolean success = false;
        try (Socket socket = new Socket(serverAddress, serverPort);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Send the ban user request
            out.println("BanUser:" + userID);

            // Receive the server's response
            String response = in.readLine();
            System.out.println("Server response: " + response); // Debugging output

            // Check if the response indicates success
            String[] tokens = response.split(":");
            success = tokens.length > 1 && "Success".equals(tokens[1]);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }

    public static boolean unbanUser(String serverAddress, int serverPort, String userID) {
        boolean success = false;
        try (Socket socket = new Socket(serverAddress, serverPort);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Send the unban user request
            out.println("UnbanUser:" + userID);

            // Receive the server's response
            String response = in.readLine();
            System.out.println("Server response: " + response); // Debugging output

            // Check if the response indicates success
            String[] tokens = response.split(":");
            success = tokens.length > 1 && "Success".equals(tokens[1]);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }

    public static boolean updateBookInfoRequest(String serverAddress, int serverPort, String bookID, String bookName, String author, String genre) {
        boolean success = false;
        try (Socket socket = new Socket(serverAddress, serverPort);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // 发送更新书籍信息请求
            out.println("UpdateBookInfo" + ":" + bookID + ":" + bookName + ":" + author + ":" + genre);

            // 接收后端返回的结果
            String response = in.readLine();
            System.out.println("Server response: " + response); // TEST
            String tokens[] = response.split(":");
            // 检查返回的结果是否为"Success"
            success = "Success".equals(tokens[1]);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }

    public static boolean addBook(String serverAddress, int serverPort, String bookName, String author, String genre) {
        boolean success = false;
        try (Socket socket = new Socket(serverAddress, serverPort);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Send the add book request
            out.println("AddBook:" + bookName + ":" + author + ":" + genre);

            // Receive the server's response
            String response = in.readLine();
            System.out.println("Server response: " + response); // Debugging output

            // Check if the response indicates success
            String[] tokens = response.split(":");
            success = "Success".equals(tokens[1]);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }

    public static boolean deleteBookBybookID(String serverAddress, int serverPort, String bookID) {
        boolean success = false;
        try (Socket socket = new Socket(serverAddress, serverPort);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Send the delete book request
            out.println("DeleteBookBybookID:" + bookID);

            // Receive the server's response
            String response = in.readLine();
            System.out.println("Server response: " + response); // Debugging output

            // Check if the response indicates success
            String[] tokens = response.split(":");
            success = "Success".equals(tokens[1]);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }

    public static String[] updateBookStatusRequest(String serverAddress, int serverPort, String bookID, String status, String userID) {
        String[] tokens = new String[2];
        try (Socket socket = new Socket(serverAddress, serverPort);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // 发送更新书籍状态请求
            out.println("UpdateBookStatus" + ":" + bookID + ":" + status + ":" + userID);

            // 接收后端返回的结果
            String response = in.readLine();
            System.out.println("Server response: " + response); // TEST
            tokens = response.split(":");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return tokens;
    }

    public static String[] getBookInfoByID(String serverAddress, int serverPort, String bookID) {
        String[] tokens = new String[6];
        try (Socket socket = new Socket(serverAddress, serverPort);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // 发送获取书籍信息请求
            out.println("GetBookInfoByID" + ":" + bookID);

            // 接收后端返回的结果
            String response = in.readLine();
            System.out.println("Server response: " + response); // TEST
            tokens = response.split("/");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return tokens;
    }

    public static List<Book> getRecommendedBooks(String serverAddress, int serverPort, String userID) { // TEST

        List<Book> recommendedBooks = new ArrayList<>();
        String[] tokens = new String[6];
        try (Socket socket = new Socket(serverAddress, serverPort);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // 发送获取书籍信息请求
            out.println("RecommendationsRX" + ":" + userID);

            // 接收后端返回的结果
            String response = in.readLine();
            System.out.println("Server response: " + response); // TEST
            tokens = response.split("/");

            // 根据每本书的ID，用getBookInfoByID找到每本书的详细信息，添加到list
            recommendedBooks = new ArrayList<>();
            for (String token : tokens) {
                String[] bookInfo = getBookInfoByID(serverAddress, serverPort, token);
                if (bookInfo.length == 6) {
                    recommendedBooks.add(new Book(bookInfo[1], bookInfo[2], bookInfo[3], bookInfo[4], bookInfo[5]));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        //print tokens
        for (String token : tokens) {
            System.out.print(token + " ");
        }
        return recommendedBooks;
//                // 模拟推荐书籍数据
//        return List.of(
//                new Book("The Catcher in the Rye", "J.D. Salinger", "Fiction", "101", "Available"),
//                new Book("Pride and Prejudice", "Jane Austen", "Romance", "102", "Available"),
//                new Book("The Hobbit", "J.R.R. Tolkien", "Fantasy", "103", "Checked Out")
//        );
    }

    public static Map<String, Integer> getUserUnreturnedBooks(String serverAddress, int serverPort, String userID) {
        Map<String, Integer> userUnreturnedBooks = null;
        try (Socket socket = new Socket(serverAddress, serverPort);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // 发送获取用户未归还书籍请求
            out.println("GetUserUnreturnedBooks" + ":" + userID);

            // 接收后端返回的结果
            String response = in.readLine();
            System.out.println("Server response: " + response); // TEST

            // 解析返回的JSON格式数据
            userUnreturnedBooks = parseUserUnreturnedBooks(response);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return userUnreturnedBooks;
    }

    private static Map<String, Integer> parseUserUnreturnedBooks(String response) {
    Map<String, Integer> userUnreturnedBooks = new HashMap<>();

    // Check if the response is not null or empty
    if (response != null && !response.isEmpty()) {
        // Split the response by "/" to separate the header and book data
        String[] parts = response.split("/");

        // Ensure the response starts with the correct header
        if (parts.length > 1 && "GetUserUnreturnedBooks".equals(parts[0])) {
            // Iterate through the book data
            for (int i = 1; i < parts.length; i++) {
                // Split each book data by ":" to separate BookID and days
                String[] bookData = parts[i].split(":");
                if (bookData.length == 2) {
                    String bookID = bookData[0];
                    try {
                        int days = Integer.parseInt(bookData[1]);
                        userUnreturnedBooks.put(bookID, days);
                    } catch (NumberFormatException e) {
                        e.printStackTrace(); // Log invalid number format
                    }
                }
            }
        }
    }

    return userUnreturnedBooks;
}

}
