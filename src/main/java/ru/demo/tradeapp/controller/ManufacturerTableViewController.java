package ru.demo.tradeapp.controller;

import javafx.beans.property.SimpleLongProperty;
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
import ru.demo.tradeapp.model.Manufacturer;
import ru.demo.tradeapp.service.ManufacturerService;
import ru.demo.tradeapp.util.Manager;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ManufacturerTableViewController implements Initializable {

    private ManufacturerService manufacturerService = new ManufacturerService();
    
    @FXML
    private Button BtnBack;

    @FXML
    private Button BtnAdd;

    @FXML
    private Button BtnDelete;

    @FXML
    private Button BtnUpdate;

    @FXML
    private TableColumn<Manufacturer, Long> TableColumnId;

    @FXML
    private TableColumn<Manufacturer, String> TableColumnTitle;

    @FXML
    private TableView<Manufacturer> TableViewManufacturers;

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
        Manager.currentManufacturer = null;
        ShowEditManufacturerWindow();
        updateTable();
    }

    @FXML
    void BtnDeleteAction(ActionEvent event) {
        Manufacturer manufacturer = TableViewManufacturers.getSelectionModel().getSelectedItem();
        if (manufacturer == null) {
            showAlert("Ошибка", "Выберите производителя для удаления", Alert.AlertType.WARNING);
            return;
        }

        Optional<ButtonType> result = showConfirmDialog("Подтверждение удаления", 
            "Вы действительно хотите удалить производителя " + manufacturer.getTitle() + "?");
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                manufacturerService.delete(manufacturer);
                updateTable();
                showAlert("Успех", "Производитель успешно удален", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                showAlert("Ошибка", "Не удалось удалить производителя: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    void BtnUpdateAction(ActionEvent event) {
        Manufacturer manufacturer = TableViewManufacturers.getSelectionModel().getSelectedItem();
        if (manufacturer == null) {
            showAlert("Ошибка", "Выберите производителя для редактирования", Alert.AlertType.WARNING);
            return;
        }
        
        Manager.currentManufacturer = manufacturer;
        ShowEditManufacturerWindow();
        updateTable();
    }

    void ShowEditManufacturerWindow() {
        Stage newWindow = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(TradeApp.class.getResource("manufacturer-edit-view.fxml"));

        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
            scene.getStylesheets().add("base-styles.css");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        newWindow.setTitle("Редактировать производителя");
        newWindow.initOwner(Manager.secondStage);
        newWindow.initModality(Modality.WINDOW_MODAL);
        newWindow.setScene(scene);
        newWindow.setMinWidth(400);
        newWindow.setMinHeight(300);
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
        
        // Установка минимального размера главного окна
        Manager.secondStage.setMinWidth(600);
        Manager.secondStage.setMinHeight(400);
    }

    void updateTable() {
        List<Manufacturer> manufacturers = manufacturerService.findAll();
        ObservableList<Manufacturer> observableManufacturers = FXCollections.observableArrayList(manufacturers);
        TableViewManufacturers.setItems(observableManufacturers);
    }

    private void setCellValueFactories() {
        TableColumnId.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getManufacturerId()).asObject());
        TableColumnTitle.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
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