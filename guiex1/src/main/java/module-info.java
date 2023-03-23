module org.example {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    //opens org.example to javafx.fxml;
    opens org.example to javafx.fxml;
    exports org.example.domain;
    exports org.example.controller;
    exports org.example.domain.validators;

    opens org.example.controller to javafx.fxml;
    exports org.example;
   // opens org.example to javafx.fxml;
}