package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.controller.LoginController;
import org.example.service.Service;

import java.io.IOException;

public class HelloApplication extends Application {

    Service service = new Service();

    @Override
    public void start(Stage primaryStage) throws IOException {

        //FXMLLoader fxmlLoader = new FXMLLoader();
        //fxmlLoader.setLocation(getClass().getResource("com/example/guiex1/views/login.fxml"));
       // FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("views/Login.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        LoginController ctrlLogIn =fxmlLoader.getController();
        ctrlLogIn.setService(service);
        primaryStage.setTitle("MySocialNetwork!");
        primaryStage.setScene(scene);

        primaryStage.show();

    }

    private void initView(Stage primaryStage) throws IOException {


    }
}