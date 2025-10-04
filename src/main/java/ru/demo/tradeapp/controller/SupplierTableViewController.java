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
import ru.demo.tradeapp.model.Supplier;
import ru.demo.tradeapp.service.SupplierService;
import ru.demo.tradeapp.util.Manager;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class SupplierTableViewController implements Initializable {

    private SupplierService supplierService = new SupplierService();
    
    @FXML
    private Button BtnBack;

    @FXML
    private Button BtnAdd;

    @FXML
    private Button BtnDelete;

    @FXML
    private Button BtnUpdate;

    @FXML
    private TableColumn<Supplier, Long> TableColumnId;

    @FXML
    private TableColumn<Supplier, String> TableColumnTitle;

    @FXML
    private TableView<Supplier> TableViewSuppliers;

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
        Manager.currentSupplier = null;
        ShowEditSupplierWindow();
        updateTable();
    }

    @FXML
    void BtnDeleteAction(ActionEvent event) {
        Supplier supplier = TableViewSuppliers.getSelectionModel().getSelectedItem();
        if (supplier == null) {
            showAlert("Ошибка", "Выберите поставщика для удаления", Alert.AlertType.WARNING);
            return;
        }

        Optional<ButtonType> result = showConfirmDialog("Подтверждение удаления", 
            "Вы действительно хотите удалить поставщика " + supplier.getTitle() + "?");
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                supplierService.delete(supplier);
                updateTable();
                showAlert("Успех", "Поставщик успешно удален", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                showAlert("Ошибка", "Не удалось удалить поставщика: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    void BtnUpdateAction(ActionEvent event) {
        Supplier supplier = TableViewSuppliers.getSelectionModel().getSelectedItem();
        if (supplier == null) {
            showAlert("Ошибка", "Выберите поставщика для редактирования", Alert.AlertType.WARNING);
            return;
        }
        
        Manager.currentSupplier = supplier;
        ShowEditSupplierWindow();
        updateTable();
    }

    void ShowEditSupplierWindow() {
        Stage newWindow = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(TradeApp.class.getResource("supplier-edit-view.fxml"));

        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
            scene.getStylesheets().add("base-styles.css");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        newWindow.setTitle("Редактировать поставщика");
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
        List<Supplier> suppliers = supplierService.findAll();
        ObservableList<Supplier> observableSuppliers = FXCollections.observableArrayList(suppliers);
        TableViewSuppliers.setItems(observableSuppliers);
    }

    private void setCellValueFactories() {
        TableColumnId.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getSupplierId()).asObject());
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