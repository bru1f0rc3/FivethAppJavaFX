package ru.demo.tradeapp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.demo.tradeapp.model.Role;
import ru.demo.tradeapp.service.RoleService;

import java.net.URL;
import java.util.ResourceBundle;

public class RoleEditViewController implements Initializable {

    @FXML
    private Button BtnCancel;

    @FXML
    private Button BtnSave;

    @FXML
    private TextField TextFieldTitle;

    private RoleService roleService = new RoleService();
    private Role role;
    private RoleTableViewController parentController;

    @FXML
    void BtnCancelAction(ActionEvent event) {
        Stage stage = (Stage) BtnCancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    void BtnSaveAction(ActionEvent event) {
        if (TextFieldTitle.getText().isEmpty()) {
            showAlert("Название роли не может быть пустым");
            return;
        }

        boolean isNew = (role == null);
        if (isNew) {
            role = new Role();
        }

        role.setTitle(TextFieldTitle.getText());

        if (isNew) {
            roleService.save(role);
        } else {
            roleService.update(role);
        }

        if (parentController != null) {
            parentController.updateList();
        }

        Stage stage = (Stage) BtnSave.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Инициализация
    }

    public void setRole(Role role) {
        this.role = role;
        if (role != null) {
            TextFieldTitle.setText(role.getTitle());
        }
    }

    public void setParentController(RoleTableViewController parentController) {
        this.parentController = parentController;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Предупреждение");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}