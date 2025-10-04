package ru.demo.tradeapp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ru.demo.tradeapp.model.Role;
import ru.demo.tradeapp.model.User;
import ru.demo.tradeapp.service.RoleService;
import ru.demo.tradeapp.service.UserService;
import ru.demo.tradeapp.util.Manager;

import java.net.URL;
import java.util.ResourceBundle;

public class UserEditViewController implements Initializable {
    
    private UserService userService = new UserService();
    private RoleService roleService = new RoleService();
    
    @FXML
    private Button BtnCancel;
    
    @FXML
    private Button BtnSave;
    
    @FXML
    private TextField TextFieldUsername;
    
    @FXML
    private TextField TextFieldFirstName;
    
    @FXML
    private TextField TextFieldSecondName;
    
    @FXML
    private TextField TextFieldMiddleName;
    
    @FXML
    private PasswordField PasswordFieldPassword;
    
    @FXML
    private ComboBox<Role> ComboBoxRole;
    
    @FXML
    private Label LabelId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Заполняем комбо бокс ролями
        ComboBoxRole.setItems(javafx.collections.FXCollections.observableArrayList(roleService.findAll()));
        
        if (Manager.currentUserEdit != null) {
            // Редактирование существующего пользователя
            LabelId.setText(Manager.currentUserEdit.getUsername());
            TextFieldUsername.setText(Manager.currentUserEdit.getUsername());
            TextFieldFirstName.setText(Manager.currentUserEdit.getFirstName());
            TextFieldSecondName.setText(Manager.currentUserEdit.getSecondName());
            TextFieldMiddleName.setText(Manager.currentUserEdit.getMiddleName());
            PasswordFieldPassword.setText(Manager.currentUserEdit.getPassword());
            ComboBoxRole.setValue(Manager.currentUserEdit.getRoleId());
            
            // Блокируем изменение имени пользователя при редактировании
            TextFieldUsername.setEditable(false);
        } else {
            // Создание нового пользователя
            Manager.currentUserEdit = new User();
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
        
        Manager.currentUserEdit.setUsername(TextFieldUsername.getText());
        Manager.currentUserEdit.setFirstName(TextFieldFirstName.getText());
        Manager.currentUserEdit.setSecondName(TextFieldSecondName.getText());
        Manager.currentUserEdit.setMiddleName(TextFieldMiddleName.getText());
        Manager.currentUserEdit.setPassword(PasswordFieldPassword.getText());
        Manager.currentUserEdit.setRoleId(ComboBoxRole.getValue());
        
        try {
            if (Manager.currentUserEdit.getUsername() != null && 
                !Manager.currentUserEdit.getUsername().isEmpty()) {
                // Обновление существующего пользователя
                userService.update(Manager.currentUserEdit);
                showAlert("Успех", "Пользователь успешно обновлен", Alert.AlertType.INFORMATION);
            } else {
                // Создание нового пользователя
                userService.save(Manager.currentUserEdit);
                showAlert("Успех", "Пользователь успешно создан", Alert.AlertType.INFORMATION);
            }
            Stage stage = (Stage) BtnSave.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            showAlert("Ошибка", "Не удалось сохранить пользователя: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private String checkFields() {
        StringBuilder error = new StringBuilder();
        if (TextFieldUsername.getText().isEmpty()) {
            error.append("Укажите имя пользователя\n");
        }
        if (TextFieldFirstName.getText().isEmpty()) {
            error.append("Укажите имя\n");
        }
        if (TextFieldSecondName.getText().isEmpty()) {
            error.append("Укажите фамилию\n");
        }
        if (PasswordFieldPassword.getText().isEmpty()) {
            error.append("Укажите пароль\n");
        }
        if (ComboBoxRole.getValue() == null) {
            error.append("Выберите роль\n");
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