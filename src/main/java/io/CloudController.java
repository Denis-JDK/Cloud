package io;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CloudController implements Initializable {

    private static final int BUFFER_SIZE = 1024;
    private static final String clientDir = "client/";
    public TextField input;
    public TextArea output;
    private Network network;
    private byte [] buffer;

    public void sendCommand(ActionEvent actionEvent) throws IOException {
        String text = input.getText();
        input.clear();
        network.write(text);
    }

    // getFile getName -> file fileName fileSize fileBytes
    // послали в сеть        ответ сети

    public void initialize(URL location, ResourceBundle resources) {
        try {
            buffer = new byte[BUFFER_SIZE];       //получили буфер
            network = Network.get();       //получили сеточку
            new Thread(() -> {
                try {
                    while (true) {
                        String message = network.read(); // масседж получаем от сети
                        if (message.equals("file")){
                            String fileName = clientDir + network.read();
                            Long fileSize = network.readLong();
                            // BUFFER_SIZE = 10  (10 + 4 - 1) / 10
                            // fileSize = 4     (4 - 1) / 10 + 1
                            // важно чтоб не было нуля иначе не выполнится условие
                            // i < .. и поток не зайдет в цикл
                            try(FileOutputStream fos = new FileOutputStream(fileName)) {  //отправили. файл создастся в этой директории с именем которое у него было на сервере
                                for (int i = 0; i < (fileSize - 1) / BUFFER_SIZE + 1; i++) {
                                    int read = network.read(buffer);
                                    fos.write(buffer, 0, read);
                                }
                            }
                        }
                        Platform.runLater(() -> output.appendText(message));
                                if (message.equals("/quit")) {
                                    network.close();
                                    break;
                                }
                        }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

