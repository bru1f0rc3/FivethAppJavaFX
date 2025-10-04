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
import ru.demo.tradeapp.model.Unittype;
import ru.demo.tradeapp.service.UnittypeService;
import ru.demo.tradeapp.util.Manager;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class UnittypeTableViewController implements Initializable {

    private UnittypeService unittypeService = new UnittypeService();
    
    @FXML
    private Button BtnBack;

    @FXML
    private Button BtnAdd;

    @FXML
    private Button BtnDelete;

    @FXML
    private Button BtnUpdate;

    @FXML
    private TableColumn<Unittype, Long> TableColumnId;

    @FXML
    private TableColumn<Unittype, String> TableColumnTitle;

    @FXML
    private TableView<Unittype> TableViewUnittypes;

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
        Manager.currentUnittype = null;
        ShowEditUnittypeWindow();
        updateTable();
    }

    @FXML
    void BtnDeleteAction(ActionEvent event) {
        Unittype unittype = TableViewUnittypes.getSelectionModel().getSelectedItem();
        if (unittype == null) {
            showAlert("Ошибка", "Выберите единицу измерения для удаления", Alert.AlertType.WARNING);
            return;
        }

        Optional<ButtonType> result = showConfirmDialog("Подтверждение удаления", 
            "Вы действительно хотите удалить единицу измерения " + unittype.getTitle() + "?");
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                unittypeService.delete(unittype);
                updateTable();
                showAlert("Успех", "Единица измерения успешно удалена", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                showAlert("Ошибка", "Не удалось удалить единицу измерения: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    void BtnUpdateAction(ActionEvent event) {
        Unittype unittype = TableViewUnittypes.getSelectionModel().getSelectedItem();
        if (unittype == null) {
            showAlert("Ошибка", "Выберите единицу измерения для редактирования", Alert.AlertType.WARNING);
            return;
        }
        
        Manager.currentUnittype = unittype;
        ShowEditUnittypeWindow();
        updateTable();
    }

    void ShowEditUnittypeWindow() {
        Stage newWindow = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(TradeApp.class.getResource("unittype-edit-view.fxml"));

        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
            scene.getStylesheets().add("base-styles.css");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        newWindow.setTitle("Редактировать единицу измерения");
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
        Manager.secondStage.setMinWidth(600);
        Manager.secondStage.setMinHeight(400);
    }

    void updateTable() {
        List<Unittype> unittypes = unittypeService.findAll();
        ObservableList<Unittype> observableUnittypes = FXCollections.observableArrayList(unittypes);
        TableViewUnittypes.setItems(observableUnittypes);
    }

    private void setCellValueFactories() {
        TableColumnId.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getUnittypeId()).asObject());
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