package libman.librarymanager;

public class Record {
    private final String recordID;
    private final String action;
    private final String bookID;
    private final String bookName;
    private final String time;

    public Record(String recordID, String action, String bookID, String bookName, String time) {
        this.recordID = recordID;
        this.action = action;
        this.bookID = bookID;
        this.bookName = bookName;
        this.time = time;
    }

    public String getRecordID() {
        return recordID;
    }

    public String getAction() {
        return action;
    }

    public String getBookID() {
        return bookID;
    }

    public String getBookName() {
        return bookName;
    }

    public String getTime() {
        return time;
    }
}
