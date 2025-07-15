package libman.librarymanager;

public class UserExtended extends User {
    private String IsBanned;

    public UserExtended(String ID, String username, String password, String isBanned) {
        super(username, password, ID);
        this.IsBanned = isBanned;
    }

    public String getIsBanned() {
        return IsBanned;
    }
}
