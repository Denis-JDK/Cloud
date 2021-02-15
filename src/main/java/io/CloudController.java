package io;


import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class CloudController {
    public TextField input;
    public TextArea output;

    public void sendCommand(ActionEvent actionEvent) {
        String text = input.getText();
        input.clear();
        output.appendText(text + "/n");
    }
}

