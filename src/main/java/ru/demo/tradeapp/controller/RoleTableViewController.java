package ru.demo.tradeapp.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.demo.tradeapp.TradeApp;
import ru.demo.tradeapp.model.Role;
import ru.demo.tradeapp.service.RoleService;
import ru.demo.tradeapp.util.Manager;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class RoleTableViewController implements Initializable {

    @FXML
    private Button BtnAdd;

    @FXML
    private Button BtnDelete;

    @FXML
    private Button BtnEdit;
    
    @FXML
    private Button BtnBack;

    @FXML
    private ListView<Role> ListViewRoles;

    private RoleService roleService = new RoleService();

    @FXML
    void BtnAddAction(ActionEvent event) {
        showEditDialog(null);
    }

    @FXML
    void BtnDeleteAction(ActionEvent event) {
        Role selectedRole = ListViewRoles.getSelectionModel().getSelectedItem();
        if (selectedRole == null) {
            showAlert("Выберите роль для удаления");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение удаления");
        alert.setHeaderText("Вы уверены, что хотите удалить роль?");
        alert.setContentText("Название: " + selectedRole.getTitle());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            roleService.delete(selectedRole);
            refreshList();
        }
    }

    @FXML
    void BtnEditAction(ActionEvent event) {
        Role selectedRole = ListViewRoles.getSelectionModel().getSelectedItem();
        if (selectedRole == null) {
            showAlert("Выберите роль для редактирования");
            return;
        }
        showEditDialog(selectedRole);
    }
    
    @FXML
    void BtnBackAction(ActionEvent event) {
        Manager.LoadSecondStageScene("main-view.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refreshList();
        ListViewRoles.setCellFactory(lv -> new ListCell<Role>() {
            @Override
            protected void updateItem(Role item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.getTitle());
                }
            }
        });
    }

    private void refreshList() {
        List<Role> roles = roleService.findAll();
        ObservableList<Role> items = FXCollections.observableArrayList(roles);
        ListViewRoles.setItems(items);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Предупреждение");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showEditDialog(Role role) {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(TradeApp.class.getResource("role-edit-view.fxml"));
        try {
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add("base-styles.css");
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(Manager.mainStage);

            RoleEditViewController controller = loader.getController();
            controller.setRole(role);
            controller.setParentController(this);

            stage.showAndWait();
            refreshList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void updateList() {
        refreshList();
    }
}