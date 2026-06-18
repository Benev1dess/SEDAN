module SedanProject {
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires mysql.connector.j;

    exports br.edu.ufersa.sedan;

    opens br.edu.ufersa.sedan.controllers to javafx.fxml;
}