package libman.librarymanager;

public class RecordAdmin extends Record {
    private final String userID;
    public RecordAdmin(String recordID, String userID, String action, String bookID, String bookName, String time) {
        super(recordID, action, bookID, bookName, time);
        this.userID = userID;
    }
    public String getUserID() {
        return userID;
    }
}