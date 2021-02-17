package io;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CloudApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent parent = FXMLLoader.load(getClass().getResource("cloud_term.fxml"));
        primaryStage.setScene(new Scene(parent)); //Scene  некоторый фронт ожидания действий клиента
        primaryStage.setTitle("Cloud");
        primaryStage.setOnCloseRequest(r ->{
            try {
                Network.get().write("/quit");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }); //переопределение закрывашки
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
