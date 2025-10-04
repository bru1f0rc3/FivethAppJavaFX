package ru.demo.tradeapp.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.demo.tradeapp.TradeApp;
import ru.demo.tradeapp.model.User;
import ru.demo.tradeapp.service.UserService;
import ru.demo.tradeapp.util.Manager;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class UserTableViewController implements Initializable {

    private UserService userService = new UserService();
    
    @FXML
    private Button BtnBack;

    @FXML
    private Button BtnAdd;

    @FXML
    private Button BtnDelete;

    @FXML
    private Button BtnUpdate;

    @FXML
    private TableColumn<User, String> TableColumnUsername;

    @FXML
    private TableColumn<User, String> TableColumnFirstName;

    @FXML
    private TableColumn<User, String> TableColumnSecondName;

    @FXML
    private TableColumn<User, String> TableColumnMiddleName;

    @FXML
    private TableColumn<User, String> TableColumnRole;

    @FXML
    private TableView<User> TableViewUsers;

    @FXML
    private Label LabelUser;

    @FXML
    void BtnBackAction(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(TradeApp.class.getResource("main-view.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
            scene.getStylesheets().add("base-styles.css");
            Manager.secondStage.setScene(scene);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void BtnAddAction(ActionEvent event) {
        Manager.currentUserEdit = null;
        ShowEditUserWindow();
        updateTable();
    }

    @FXML
    void BtnDeleteAction(ActionEvent event) {
        User user = TableViewUsers.getSelectionModel().getSelectedItem();
        if (user == null) {
            showAlert("Ошибка", "Выберите пользователя для удаления", Alert.AlertType.WARNING);
            return;
        }

        Optional<ButtonType> result = showConfirmDialog("Подтверждение удаления", 
            "Вы действительно хотите удалить пользователя " + user.getUsername() + "?");
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                userService.delete(user);
                updateTable();
                showAlert("Успех", "Пользователь успешно удален", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                showAlert("Ошибка", "Не удалось удалить пользователя: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    void BtnUpdateAction(ActionEvent event) {
        User user = TableViewUsers.getSelectionModel().getSelectedItem();
        if (user == null) {
            showAlert("Ошибка", "Выберите пользователя для редактирования", Alert.AlertType.WARNING);
            return;
        }
        
        Manager.currentUserEdit = user;
        ShowEditUserWindow();
        updateTable();
    }

    void ShowEditUserWindow() {
        Stage newWindow = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(TradeApp.class.getResource("user-edit-view.fxml"));

        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
            scene.getStylesheets().add("base-styles.css");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        newWindow.setTitle("Редактировать пользователя");
        newWindow.initOwner(Manager.secondStage);
        newWindow.initModality(Modality.WINDOW_MODAL);
        newWindow.setScene(scene);
        newWindow.setMinWidth(400);
        newWindow.setMinHeight(500);
        Manager.currentStage = newWindow;
        newWindow.showAndWait();
        Manager.currentStage = null;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initController();
    }

    public void initController() {
        LabelUser.setText(Manager.currentUser.getFirstName());
        setCellValueFactories();
        updateTable();

        Manager.secondStage.setMinWidth(800);
        Manager.secondStage.setMinHeight(600);
    }

    void updateTable() {
        List<User> users = userService.findAll();
        ObservableList<User> observableUsers = FXCollections.observableArrayList(users);
        TableViewUsers.setItems(observableUsers);
    }

    private void setCellValueFactories() {
        TableColumnUsername.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
        TableColumnFirstName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
        TableColumnSecondName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSecondName()));
        TableColumnMiddleName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMiddleName()));
        TableColumnRole.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRoleId().getTitle()));
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private Optional<ButtonType> showConfirmDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        return alert.showAndWait();
    }
}