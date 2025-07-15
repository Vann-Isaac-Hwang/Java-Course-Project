package libman.librarymanager;

public class Book {
    private final String bookName;
    private final String author;
    private final String genre;
    private final String id;
    private final String status;

    public Book(String bookName, String author, String genre, String id, String status) {
        this.bookName = bookName;
        this.author = author;
        this.genre = genre;
        this.id = id;
        this.status = status; // Default status
    }

    public String getBookName() {
        return bookName;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }
}