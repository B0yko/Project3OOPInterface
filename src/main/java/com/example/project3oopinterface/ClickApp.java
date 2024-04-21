package com.example.project3oopinterface;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.text.Font;

public class ClickApp extends Application {

    private Label currentControlName;
    private CustomControl currentControl;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        CustomControl control = new CustomControl("My Control");
        control.setClickHandler(() -> System.out.println("Control clicked"));
        Pane root = new Pane();
        currentControlName = new Label("No control selected");
        currentControlName.setFont(Font.font("Arial", 24));
        currentControlName.setLayoutX(50);
        currentControlName.setLayoutY(40);
        root.getChildren().add(currentControlName);

        for (int i = 0; i < 7; i++) {
            CustomControl customControl = new CustomControl("Control " + i);
            customControl.setTranslateX(50 + i * 105);
            customControl.setTranslateY(200);
            customControl.setClickHandler(() -> {
                customControl.setStyle("-fx-background-color: red;");
                currentControlName.setText("Selected control: " + customControl.getText());
                if (currentControl != null) {
                    currentControl.setStyle("-fx-background-color: Lightgrey;");
                }
                currentControl = customControl;
            });
            root.getChildren().add(customControl);
        }

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Click App");
        primaryStage.show();
    }
}
