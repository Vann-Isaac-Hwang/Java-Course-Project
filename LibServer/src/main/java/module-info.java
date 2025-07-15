module libman.fakeend {
    requires javafx.controls;
    requires java.sql;


    opens libman.fakeend to javafx.fxml;
    exports libman.fakeend;
}