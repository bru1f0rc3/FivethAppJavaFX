package ru.demo.tradeapp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ru.demo.tradeapp.model.Manufacturer;
import ru.demo.tradeapp.service.ManufacturerService;
import ru.demo.tradeapp.util.Manager;

import java.net.URL;
import java.util.ResourceBundle;

public class ManufacturerEditViewController implements Initializable {
    
    private ManufacturerService manufacturerService = new ManufacturerService();
    
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
        if (Manager.currentManufacturer != null) {
            // Редактирование существующего производителя
            LabelId.setText(Manager.currentManufacturer.getManufacturerId().toString());
            TextFieldTitle.setText(Manager.currentManufacturer.getTitle());
        } else {
            // Создание нового производителя
            Manager.currentManufacturer = new Manufacturer();
            LabelId.setText("Новый");
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
        
        Manager.currentManufacturer.setTitle(TextFieldTitle.getText());
        
        try {
            if (Manager.currentManufacturer.getManufacturerId() != null && 
                Manager.currentManufacturer.getManufacturerId() > 0) {
                // Обновление существующего производителя
                manufacturerService.update(Manager.currentManufacturer);
                showAlert("Успех", "Производитель успешно обновлен", Alert.AlertType.INFORMATION);
            } else {
                // Создание нового производителя
                manufacturerService.save(Manager.currentManufacturer);
                showAlert("Успех", "Производитель успешно создан", Alert.AlertType.INFORMATION);
            }
            Stage stage = (Stage) BtnSave.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            showAlert("Ошибка", "Не удалось сохранить производителя: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private String checkFields() {
        StringBuilder error = new StringBuilder();
        if (TextFieldTitle.getText().isEmpty()) {
            error.append("Укажите название производителя\n");
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