package ru.demo.tradeapp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ru.demo.tradeapp.model.Unittype;
import ru.demo.tradeapp.service.UnittypeService;
import ru.demo.tradeapp.util.Manager;

import java.net.URL;
import java.util.ResourceBundle;

public class UnittypeEditViewController implements Initializable {
    
    private UnittypeService unittypeService = new UnittypeService();
    
    @FXML
    private Button BtnCancel;
    
    @FXML
    private Button BtnSave;
    
    @FXML
    private TextField TextFieldTitle;
    
    @FXML
    private Label LabelId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (Manager.currentUnittype != null) {
            LabelId.setText(Manager.currentUnittype.getUnittypeId().toString());
            TextFieldTitle.setText(Manager.currentUnittype.getTitle());
        } else {
            Manager.currentUnittype = new Unittype();
            LabelId.setText("Новая");
        }
    }

    @FXML
    void BtnCancelAction(ActionEvent event) {
        Stage stage = (Stage) BtnCancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    void BtnSaveAction(ActionEvent event) {
        String error = checkFields();
        if (!error.isEmpty()) {
            showAlert("Ошибка", error, Alert.AlertType.ERROR);
            return;
        }
        
        Manager.currentUnittype.setTitle(TextFieldTitle.getText());
        
        try {
            if (Manager.currentUnittype.getUnittypeId() != null && 
                Manager.currentUnittype.getUnittypeId() > 0) {
                unittypeService.update(Manager.currentUnittype);
                showAlert("Успех", "Единица измерения успешно обновлена", Alert.AlertType.INFORMATION);
            } else {
                unittypeService.save(Manager.currentUnittype);
                showAlert("Успех", "Единица измерения успешно создана", Alert.AlertType.INFORMATION);
            }
            Stage stage = (Stage) BtnSave.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            showAlert("Ошибка", "Не удалось сохранить единицу измерения: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private String checkFields() {
        StringBuilder error = new StringBuilder();
        if (TextFieldTitle.getText().isEmpty()) {
            error.append("Укажите название единицы измерения\n");
        }
        return error.toString();
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}