module com.example.project3oopinterface {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.project3oopinterface to javafx.fxml;
    exports com.example.project3oopinterface;
}