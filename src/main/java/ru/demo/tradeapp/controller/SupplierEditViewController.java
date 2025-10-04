package ru.demo.tradeapp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ru.demo.tradeapp.model.Supplier;
import ru.demo.tradeapp.service.SupplierService;
import ru.demo.tradeapp.util.Manager;

import java.net.URL;
import java.util.ResourceBundle;

public class SupplierEditViewController implements Initializable {
    
    private SupplierService supplierService = new SupplierService();
    
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
        if (Manager.currentSupplier != null) {
            // Редактирование существующего поставщика
            LabelId.setText(Manager.currentSupplier.getSupplierId().toString());
            TextFieldTitle.setText(Manager.currentSupplier.getTitle());
        } else {
            // Создание нового поставщика
            Manager.currentSupplier = new Supplier();
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
        
        Manager.currentSupplier.setTitle(TextFieldTitle.getText());
        
        try {
            if (Manager.currentSupplier.getSupplierId() != null && 
                Manager.currentSupplier.getSupplierId() > 0) {
                // Обновление существующего поставщика
                supplierService.update(Manager.currentSupplier);
                showAlert("Успех", "Поставщик успешно обновлен", Alert.AlertType.INFORMATION);
            } else {
                // Создание нового поставщика
                supplierService.save(Manager.currentSupplier);
                showAlert("Успех", "Поставщик успешно создан", Alert.AlertType.INFORMATION);
            }
            Stage stage = (Stage) BtnSave.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            showAlert("Ошибка", "Не удалось сохранить поставщика: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private String checkFields() {
        StringBuilder error = new StringBuilder();
        if (TextFieldTitle.getText().isEmpty()) {
            error.append("Укажите название поставщика\n");
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