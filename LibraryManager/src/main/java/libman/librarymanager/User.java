package libman.librarymanager;

public class User {
    private final String username;
    private final String password;
    private final String ID; // Default ID

    public User(String username, String password, String ID) {
        this.username = username;
        this.password = password;
        this.ID = ID;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getID() {
        return ID;
    }
}
