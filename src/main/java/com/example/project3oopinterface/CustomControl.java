package com.example.project3oopinterface;

import javafx.scene.layout.Pane;
import javafx.scene.control.Label;

public class CustomControl extends Pane {
    private ClickHandler clickHandler;
    private String text;

    public CustomControl(String text) {
        setStyle("-fx-background-color: lightgrey;");
        this.text = text;
        setPrefSize(100, 100);
        Label label = new Label(text);
        label.setLayoutX(20);
        label.setLayoutY(40);
        getChildren().add(label);

        setOnMouseClicked(e -> {
            if (clickHandler != null) {
                clickHandler.handle();
            }
        });
    }

    public void setClickHandler(ClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }

    public String getText() {
        return text;
    }

    public interface ClickHandler {
        void handle();
    }
}
