package libman.fakeend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LibServer {
    // Define a fixed thread pool size (adjust based on your system capabilities)
    private static final int THREAD_POOL_SIZE = 10;

    public static void main(String[] args) {
        if (args.length == 0 || args[0].equals("-h")) {
            System.out.println("Usage:");
            System.out.println("  -work                     Run the server normally.");
            System.out.println("  -addAdmin <username> <password> <phonenumber>  Add a new admin.");
            System.out.println("  -deleteAdmin <adminID>    Delete an admin by ID.");
            System.out.println("  -listAllAdmin             List all admins.");
            System.out.println("  -initDB                   Initialize the database.");
            System.out.println("  -addExampleBooks          Add example books to the database.");
            System.out.println("  -h                        Show this help message.");
            return;
        }

        switch (args[0]) {
            case "-work":
                runServer();
                break;

            case "-addAdmin":
                if (args.length != 4) {
                    System.out.println("Error: Invalid arguments for -addAdmin. Usage: -addAdmin <username> <password> <phonenumber>");
                    return;
                }
                String username = args[1];
                String password = args[2];
                String phoneNumber = args[3];
                if (AccessDB.addAdmin(username, password, phoneNumber)) {
                    System.out.println("Admin added successfully.");
                } else {
                    System.out.println("Failed to add admin.");
                }
                break;

            case "-deleteAdmin":
                if (args.length != 2) {
                    System.out.println("Error: Invalid arguments for -deleteAdmin. Usage: -deleteAdmin <adminID>");
                    return;
                }
                String adminID = args[1];
                if (AccessDB.deleteAdmin(adminID)) {
                    System.out.println("Admin deleted successfully.");
                } else {
                    System.out.println("Failed to delete admin.");
                }
                break;

            case "-listAllAdmin":
                AccessDB.printAllAdmins();
                break;

            case "-initDB":
                new AccessDB(); // Initialize the database
                System.out.println("Database initialized successfully.");
                break;

            case "-addExampleBooks":
                AddExampleBooks();
                System.out.println("Example books added successfully.");
                break;

            default:
                System.out.println("Error: Unknown command. Use -h for help.");
        }
    }

    private static void runServer() {
        int port = 12345; // Listening port
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                executorService.submit(new ClientHandler(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }

    public static void AddExampleBooks() {
        // 原始书籍
        AccessDB.addBook("The Great Gatsby", "F. Scott Fitzgerald", "Fiction", "1");
        AccessDB.addBook("1984", "George Orwell", "Dystopian", "2");
        AccessDB.addBook("To Kill a Mockingbird", "Harper Lee", "Classic", "3");
        AccessDB.addBook("The Catcher in the Rye", "J.D. Salinger", "Fiction", "4");
        AccessDB.addBook("Brave New World", "Aldous Huxley", "Dystopian", "5");
        AccessDB.addBook("Fahrenheit 451", "Ray Bradbury", "Dystopian", "6");
        AccessDB.addBook("Jane Eyre", "Charlotte Brontë", "Classic", "7");

        // 经典文学
        AccessDB.addBook("Pride and Prejudice", "Jane Austen", "Classic", "8");
        AccessDB.addBook("War and Peace", "Leo Tolstoy", "Classic", "9");
        AccessDB.addBook("Anna Karenina", "Leo Tolstoy", "Classic", "10");
        AccessDB.addBook("Les Misérables", "Victor Hugo", "Classic", "11");
        AccessDB.addBook("The Brothers Karamazov", "Fyodor Dostoevsky", "Philosophical", "12");
        AccessDB.addBook("Don Quixote", "Miguel de Cervantes", "Adventure", "13");
        AccessDB.addBook("Wuthering Heights", "Emily Brontë", "Gothic", "14");
        AccessDB.addBook("The Odyssey", "Homer", "Epic", "15");

        // 科幻/奇幻
        AccessDB.addBook("The Hobbit", "J.R.R. Tolkien", "Fantasy", "16");
        AccessDB.addBook("Dune", "Frank Herbert", "Science Fiction", "17");
        AccessDB.addBook("The Handmaid's Tale", "Margaret Atwood", "Dystopian", "18");
        AccessDB.addBook("The Martian", "Andy Weir", "Science Fiction", "19");
        AccessDB.addBook("Project Hail Mary", "Andy Weir", "Science Fiction", "20");
        AccessDB.addBook("The Three-Body Problem", "Liu Cixin", "Science Fiction", "21");
        AccessDB.addBook("The Name of the Wind", "Patrick Rothfuss", "Fantasy", "22");
        AccessDB.addBook("Mistborn: The Final Empire", "Brandon Sanderson", "Fantasy", "23");

        // 悬疑/惊悚
        AccessDB.addBook("The Girl with the Dragon Tattoo", "Stieg Larsson", "Thriller", "24");
        AccessDB.addBook("Gone Girl", "Gillian Flynn", "Psychological Thriller", "25");
        AccessDB.addBook("The Silent Patient", "Alex Michaelides", "Psychological Thriller", "26");
        AccessDB.addBook("And Then There Were None", "Agatha Christie", "Mystery", "27");
        AccessDB.addBook("The Woman in the Window", "A.J. Finn", "Thriller", "28");

        // 人文社科
        AccessDB.addBook("Sapiens: A Brief History of Humankind", "Yuval Noah Harari", "History", "29");
        AccessDB.addBook("Atomic Habits", "James Clear", "Self-Help", "30");
        AccessDB.addBook("Thinking, Fast and Slow", "Daniel Kahneman", "Psychology", "31");
        AccessDB.addBook("The Power of Now", "Eckhart Tolle", "Spirituality", "32");
        AccessDB.addBook("Outliers", "Malcolm Gladwell", "Sociology", "33");

        // 现代文学
        AccessDB.addBook("The Alchemist", "Paulo Coelho", "Fiction", "34");
        AccessDB.addBook("The Kite Runner", "Khaled Hosseini", "Fiction", "35");
        AccessDB.addBook("Normal People", "Sally Rooney", "Contemporary", "36");
        AccessDB.addBook("Where the Crawdads Sing", "Delia Owens", "Mystery", "37");
        AccessDB.addBook("The Midnight Library", "Matt Haig", "Fantasy", "38");
        AccessDB.addBook("Klara and the Sun", "Kazuo Ishiguro", "Science Fiction", "39");

        // 其他
        AccessDB.addBook("The Hitchhiker's Guide to the Galaxy", "Douglas Adams", "Science Fiction", "40");
        AccessDB.addBook("Slaughterhouse-Five", "Kurt Vonnegut", "Satire", "41");
        AccessDB.addBook("Norwegian Wood", "Haruki Murakami", "Literary Fiction", "42");
        AccessDB.addBook("Crime and Punishment", "Fyodor Dostoevsky", "Philosophical", "43");
        AccessDB.addBook("The Picture of Dorian Gray", "Oscar Wilde", "Gothic", "44");
    }
}