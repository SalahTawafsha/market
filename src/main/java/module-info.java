module com.example.market {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires java.sql;

    opens com.example.market to javafx.graphics, javafx.fxml, javafx.base;
}