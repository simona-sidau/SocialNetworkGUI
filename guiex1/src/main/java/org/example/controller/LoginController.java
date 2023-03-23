package org.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.HelloApplication;
import org.example.domain.User;
import org.example.service.Service;

import java.io.IOException;

public class LoginController {

    Service service;

    User owner;
    @FXML
    public TextField txtFieldPassword;
    @FXML
    public Button buttonLogin;
    @FXML
    public Text textUsername;
    @FXML
    public TextField txtFieldUsername;
    @FXML
    public Text textPassword;

    /**
     * Verifies in credentials are correct and launches userView
     * @param actionEvent
     * @throws IOException
     */
    public void login(ActionEvent actionEvent) throws IOException {

        var x = service.login(txtFieldUsername.getText(), txtFieldPassword.getText());
        if (x.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Exception");
            alert.setContentText("Something went wrong! Check username and password");
            alert.show();
            txtFieldPassword.clear();
            txtFieldUsername.clear();
        } else {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("views/User.fxml"));
            Scene scene = new Scene(loader.load());
            UserController ctrlUser = loader.getController();
            ctrlUser.setAccount(x.get());
            ctrlUser.setService(service);
            Stage stage = new Stage();
            stage.setTitle("MyProfile");
            stage.setScene(scene);
            stage.show();
            //" open next stage
            Node source = (Node) actionEvent.getSource();
            Stage crtStage = (Stage) source.getScene().getWindow();
            crtStage.close();
        }
    }

    /**
     * Sets service
     * @param s - Service entity
     */
    public void setService(Service s) {
        this.service = s;
    }


}