package ru.demo.tradeapp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.demo.tradeapp.model.Status;
import ru.demo.tradeapp.service.StatusService;

import java.net.URL;
import java.util.ResourceBundle;

public class StatusEditViewController implements Initializable {

    @FXML
    private Button BtnCancel;

    @FXML
    private Button BtnSave;

    @FXML
    private TextField TextFieldTitle;

    private StatusService statusService = new StatusService();
    private Status status;
    private StatusTableViewController parentController;

    @FXML
    void BtnCancelAction(ActionEvent event) {
        Stage stage = (Stage) BtnCancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    void BtnSaveAction(ActionEvent event) {
        if (TextFieldTitle.getText().isEmpty()) {
            showAlert("Название статуса не может быть пустым");
            return;
        }

        boolean isNew = (status == null);
        if (isNew) {
            status = new Status();
        }

        status.setTitle(TextFieldTitle.getText());

        if (isNew) {
            statusService.save(status);
        } else {
            statusService.update(status);
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

    public void setStatus(Status status) {
        this.status = status;
        if (status != null) {
            TextFieldTitle.setText(status.getTitle());
        }
    }

    public void setParentController(StatusTableViewController parentController) {
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