package ru.demo.tradeapp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.demo.tradeapp.model.PickupPoint;
import ru.demo.tradeapp.service.PickupPointService;

import java.net.URL;
import java.util.ResourceBundle;

public class PickupPointEditViewController implements Initializable {

    @FXML
    private Button BtnCancel;

    @FXML
    private Button BtnSave;

    @FXML
    private TextField TextFieldAddress;

    private PickupPointService pickupPointService = new PickupPointService();
    private PickupPoint pickupPoint;
    private PickupPointTableViewController parentController;

    @FXML
    void BtnCancelAction(ActionEvent event) {
        Stage stage = (Stage) BtnCancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    void BtnSaveAction(ActionEvent event) {
        if (TextFieldAddress.getText().isEmpty()) {
            showAlert("Адрес не может быть пустым");
            return;
        }

        boolean isNew = (pickupPoint == null);
        if (isNew) {
            pickupPoint = new PickupPoint();
        }

        pickupPoint.setTitle(TextFieldAddress.getText());

        if (isNew) {
            pickupPointService.save(pickupPoint);
        } else {
            pickupPointService.update(pickupPoint);
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

    public void setPickupPoint(PickupPoint pickupPoint) {
        this.pickupPoint = pickupPoint;
        if (pickupPoint != null) {
            TextFieldAddress.setText(pickupPoint.getTitle());
        }
    }

    public void setParentController(PickupPointTableViewController parentController) {
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