package org.example.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import org.example.domain.Friendship;
import org.example.domain.Request;
import org.example.domain.User;
import org.example.service.Service;
import org.example.service.ServiceException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class UserController {

    public Button sendRequest;
    Service service;
    User owner;


    @FXML
    public Group FriendsGroup;
    @FXML
    public Group RequestGroup;
    @FXML
    public TableView<User> usersTab;
    public TableColumn<User,String> colUsername;
    ObservableList<User> modelUsers = FXCollections.observableArrayList();

    @FXML
    public TableView<User> friendsTab;

    @FXML
    public TableColumn<User,String> colFriends;
    ObservableList<User> friendsModel = FXCollections.observableArrayList();

    @FXML
    public TableView<User> requestTab;
    @FXML
    public TableColumn<User,String> colRequests;
    ObservableList<User> requestModel = FXCollections.observableArrayList();
    //Views
    @FXML
    public Button declineRequest;
    @FXML
    public Button acceptRequest;
    @FXML
    public Button deleteFriend;

    public TextField handleFiltre;

    /**
     * Populates tables with - users from database
     *                       - owner's friends
     *                       - owner's requests
     */

    private void populateTabels(){

        Predicate<User> notMe= user -> !user.getUserName().equals(owner.getUserName());

        var requestsList=service.getUsers().stream()
                .filter((el)-> ! service.getUserRequests(owner).stream().filter(e->!e.isStatus())
                        .filter(e->e.getUser1().equals(el.getId()))
                        .toList().isEmpty())
                .toList();
        requestModel.setAll(requestsList);

        Predicate<User> notRequested=user -> !requestsList.contains(user);

        var sentRequests=service.getRequests().stream()
                .filter(e->e.getUser1().equals(owner.getId()))
                .map(e->service.findUserById(e.getUser2())).toList();

        Predicate<User> alreadySent=user -> !sentRequests.contains(user);



        var friendsList=service.getFriendsOf(owner).stream()
                .map(e-> (e.getUser1().equals(owner.getId()))? e.getUser2(): e.getUser1())
                .map(e->service.findUserById(e))
                .toList();
        friendsModel.setAll(friendsList);
        Predicate<User> notFriends=user -> !friendsList.contains(user);

        modelUsers.setAll(service.getUsers().stream()
                .filter(notMe.and(notFriends).and(notRequested).and(alreadySent))
                .toList());
    }

    private void populateUsers(){
        var sentRequests=service.getRequests().stream()
                .filter(e->e.getUser1().equals(owner.getId()))
                .map(e->service.findUserById(e.getUser2())).toList();

        Predicate<User> notMe=user -> !user.getUserName().equals(owner.getUserName());
        Predicate<User> startW= e->e.getUserName().startsWith(handleFiltre.getText());
        Predicate<User> notFriends=user -> !friendsModel.stream().toList().contains(user);
        Predicate<User> alreadySent=user -> !sentRequests.contains(user);
        Predicate<User> notRequested=user -> !requestModel.stream().toList().contains(user);

        modelUsers.setAll(service.getUsers().stream()
                .filter(notMe.and(notFriends).and(notRequested).and(alreadySent).and(startW))
                .toList());
    }

    /**
     *
     */
    public void initialize(){
        colUsername.setCellValueFactory(new PropertyValueFactory<>("userName"));
        colRequests.setCellValueFactory(new PropertyValueFactory<>("userName"));
        colFriends.setCellValueFactory(new PropertyValueFactory<>("userName"));
        usersTab.setItems(modelUsers);
        requestTab.setItems(requestModel);
        friendsTab.setItems(friendsModel);

        handleFiltre.textProperty().addListener(o-> populateUsers());

    }
/*
    private void setUserData(){
        txtFieldFName.setText(owner.getFirstName());
        txtFieldLName.setText(owner.getLastName());
        txtFieldUsername.setText(owner.getUserName());
    }*/

    public void setService(Service service) {
        this.service=service;
        populateTabels();
    }


    public void setAccount(User acc) {
        this.owner=acc;
        //setUserData();
    }
    /*
    public void change(ActionEvent actionEvent) {
        var x=Choice.getSelectionModel().getSelectedItem();
        switch (x){
            case "Friends"->{
                RequestGroup.setVisible(false);
                FriendsGroup.setVisible(true);
            }
            case "Requests"-> {
                RequestGroup.setVisible(true);
                FriendsGroup.setVisible(false);
            }
            default -> {
                FriendsGroup.setVisible(false);
                RequestGroup.setVisible(false);
            }
        }
    }*/
    @FXML
    public void sendRequest(ActionEvent actionEvent) {
        var selected=usersTab.getSelectionModel().getSelectedItem();
        if(!usersTab.getSelectionModel().isEmpty()){
            var req = new Request<Integer>(owner.getId(),service.findUserByUserName(selected.getUserName()).getId());
            req.setDate(LocalDateTime.now());
            try {
                service.addRequest(req);
                populateTabels();
            } catch (ServiceException e) {
                {
                    Alert a = new Alert(Alert.AlertType.INFORMATION);
                    a.setTitle("Warning");
                    a.setContentText((e.getMessage()));
                    a.show();
                    return;
                }
            }
            Alert a=new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Request sent");
            a.setContentText("Request sent successfully!");
            a.show();
        } else{
            Alert a=new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("User not selected");
            a.setContentText("Select a user to sent the request");
            a.show();
        }
    }



    public void deleteFriend(ActionEvent actionEvent) {
        var user= friendsTab.getSelectionModel().getSelectedItem();
        if(user==null){
            Alert a=new Alert(Alert.AlertType.WARNING);
            a.setTitle("WARNING");
            a.setContentText("Unselected friend! Please select one to continue the action!");
            a.show();
            return;
        }
        /*
        var reqAux=service.findRequest(new Request<Integer>(user.getId(),owner.getId()));
        if(reqAux==null)
            reqAux=service.findRequest(new Request<Integer>(owner.getId(),user.getId()));
            */
        var friendship = new Friendship<>();
        var friendship1=new Friendship<UUID>(owner.getId(),user.getId());
        var found1 =service.findFriendship(friendship1);
        var friendship2=new Friendship<UUID>(user.getId(), owner.getId());
        var found2 =service.findFriendship(friendship2);
        if(friendship2 == null)
        {
            friendship.setUser1(owner.getId());
            friendship.setUser2(user.getId());
        }
        else {
            friendship.setUser1(user.getId());
            friendship.setUser1(owner.getId());
        }
        if(found1==null && found2 == null){
            Alert a=new Alert(Alert.AlertType.WARNING);
            a.setTitle("WARNING");
            a.setContentText("Nonexistent Friendship!");
            a.show();
            return;
        }
        try {

            service.deleteFriend(friendship);
            //service.deleteRequest(reqAux);
            Alert a=new Alert(Alert.AlertType.WARNING);
            a.setTitle("Succes");
            a.setContentText("Friendship deleted successfully");
            a.show();
            populateTabels();
        } catch (ServiceException e) {
            Alert a=new Alert(Alert.AlertType.WARNING);
            a.setTitle("WARNING");
            a.setContentText(e.getMessage());
            a.show();

        }
    }

    public void acceptRequest(ActionEvent actionEvent) {
        var user= requestTab.getSelectionModel().getSelectedItem();
        if(user==null){
            Alert a=new Alert(Alert.AlertType.WARNING);
            a.setTitle("WARNING");
            a.setContentText("Unselected Request!");
            a.show();
            return;
        }
        var aux=service.findRequest(new Request<Integer>(user.getId(),owner.getId()));
        var found=service.findRequest(aux);

        if(found==null){
            Alert a=new Alert(Alert.AlertType.WARNING);
            a.setTitle("WARNING");
            a.setContentText("Nonexistent Request!");
            a.show();
            return;
        }

        var friendship=new Friendship<UUID>(found.getUser1(),found.getUser2());
        try {
            friendship.setDate(LocalDateTime.now());
            service.addFriend(friendship);
            service.deleteRequest(found);
            Alert a=new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Success");
            a.setContentText("Friendship accepted");
            a.show();
            populateTabels();
        } catch (ServiceException e) {
            Alert a=new Alert(Alert.AlertType.WARNING);
            a.setTitle("WARNING");
            a.setContentText(e.getMessage());
            a.show();

        }
    }

    public void declineRequest(ActionEvent actionEvent) {
        var user= requestTab.getSelectionModel().getSelectedItem();
        if(user==null){
            Alert a=new Alert(Alert.AlertType.WARNING);
            a.setTitle("WARNING");
            a.setContentText("Unselected Request");
            a.show();
            return;
        }
        var aux=service.findRequest(new Request<Integer>(user.getId(),owner.getId()));
        var found=service.findRequest(aux);
        if(found==null){
            Alert a=new Alert(Alert.AlertType.WARNING);
            a.setTitle("WARNING");
            a.setContentText("Nonexistent Request!");
            a.show();
            return;
        }
        try {
            service.deleteRequest(found);
            Alert a=new Alert(Alert.AlertType.WARNING);
            a.setTitle("Success");
            a.setContentText("Request deleted");
            a.show();
            populateTabels();
        } catch (ServiceException e) {
            Alert a=new Alert(Alert.AlertType.WARNING);
            a.setTitle("WARNING");
            a.setContentText(e.getMessage());
            a.show();

        }
    }
}
