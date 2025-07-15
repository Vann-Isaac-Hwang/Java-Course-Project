package libman.librarymanager;

import java.io.*;
import java.net.Socket;

public class LoginClient {
    public static String[] LoginRequest(String username, String password, boolean isAdmin) {
        String[] tokens = new String[4];
        String serverAddress = "127.0.0.1"; // 后端服务器地址
        int serverPort = 12345; // 后端服务器端口

        try (Socket socket = new Socket(serverAddress, serverPort);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // 发送登录请求，包含是否为管理员的信息
            out.println("Login" + ":" + username + ":" + password + ":" + isAdmin);

            // 接收后端返回的结果
            String response = in.readLine();
            System.out.println("Server response: " + response); // TEST
            tokens = response.split(":");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return tokens;
    }

    public static String[] RegisterRequest(String username, String password) {
        String[] tokens = new String[4];
        String serverAddress = "127.0.0.1"; // 后端服务器地址
        int serverPort = 12345; // 后端服务器端口

        try (Socket socket = new Socket(serverAddress, serverPort);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // 发送登录请求，包含是否为管理员的信息
            out.println("Register" + ":" + username + ":" + password);

            // 接收后端返回的结果
            String response = in.readLine();
            System.out.println("Server response: " + response); // TEST
            tokens = response.split(":");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return tokens;
    }

    public static String[] CheckUsernameExistence(String username) {
        String[] tokens = new String[2];
        String serverAddress = "127.0.0.1"; // 后端服务器地址
        int serverPort = 12345; // 后端服务器端口

        try (Socket socket = new Socket(serverAddress, serverPort);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // 发送登录请求，包含是否为管理员的信息
            out.println("CheckUsername" + ":" + username);

            // 接收后端返回的结果
            String response = in.readLine();
            System.out.println("Server response: " + response); // TEST
            if (response==null || response.isEmpty()) {
                System.out.println("Error: Received empty response from server.");
                return new String[]{"Error", "No response"};
            }
            tokens = response.split(":");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return tokens;
    }

    public static String[] GetUserIDwithUsername(String username) {
        String[] tokens = new String[2];
        String serverAddress = "127.0.0.1"; // 后端服务器地址
        int serverPort = 12345; // 后端服务器端口

        try (Socket socket = new Socket(serverAddress, serverPort);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // 发送登录请求，包含是否为管理员的信息
            out.println("GetUserIDbyUsername" + ":" + username);

            // 接收后端返回的结果
            String response = in.readLine();
            System.out.println("Server response: " + response); // TEST
            if (response==null || response.isEmpty()) {
                System.out.println("Error: Received empty response from server.");
                return new String[]{"Error", "No response"};
            }
            tokens = response.split(":");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return tokens;
    }

    public static String[] GetAdminIDwithUsername(String username) {
        String[] tokens = new String[2];
        String serverAddress = "127.0.0.1"; // Backend server address
        int serverPort = 12345; // Backend server port

        try (Socket socket = new Socket(serverAddress, serverPort);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Send request to get admin ID by username
            out.println("GetAdminIDbyUsername" + ":" + username);

            // Receive response from the server
            String response = in.readLine();
            System.out.println("Server response: " + response); // Debugging output
            if (response == null || response.isEmpty()) {
                System.out.println("Error: Received empty response from server.");
                return new String[]{"Error", "No response"};
            }
            tokens = response.split(":");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return tokens;
    }
}