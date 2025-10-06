package ru.demo.tradeapp.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.demo.tradeapp.TradeApp;
import ru.demo.tradeapp.model.PickupPoint;
import ru.demo.tradeapp.service.PickupPointService;
import ru.demo.tradeapp.util.Manager;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class PickupPointTableViewController implements Initializable {

    @FXML
    private Button BtnAdd;

    @FXML
    private Button BtnDelete;

    @FXML
    private Button BtnEdit;
    
    @FXML
    private Button BtnBack;

    @FXML
    private ListView<PickupPoint> ListViewPickupPoints;

    private PickupPointService pickupPointService = new PickupPointService();

    @FXML
    void BtnAddAction(ActionEvent event) {
        showEditDialog(null);
    }

    @FXML
    void BtnDeleteAction(ActionEvent event) {
        PickupPoint selectedPickupPoint = ListViewPickupPoints.getSelectionModel().getSelectedItem();
        if (selectedPickupPoint == null) {
            showAlert("Выберите пункт выдачи для удаления");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение удаления");
        alert.setHeaderText("Вы уверены, что хотите удалить пункт выдачи?");
        alert.setContentText("Адрес: " + selectedPickupPoint.getTitle());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            pickupPointService.delete(selectedPickupPoint);
            refreshList();
        }
    }

    @FXML
    void BtnEditAction(ActionEvent event) {
        PickupPoint selectedPickupPoint = ListViewPickupPoints.getSelectionModel().getSelectedItem();
        if (selectedPickupPoint == null) {
            showAlert("Выберите пункт выдачи для редактирования");
            return;
        }
        showEditDialog(selectedPickupPoint);
    }
    
    @FXML
    void BtnBackAction(ActionEvent event) {
        Manager.LoadSecondStageScene("main-view.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refreshList();
        ListViewPickupPoints.setCellFactory(lv -> new ListCell<PickupPoint>() {
            @Override
            protected void updateItem(PickupPoint item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.getTitle());
                }
            }
        });
    }

    private void refreshList() {
        List<PickupPoint> pickupPoints = pickupPointService.findAll();
        ObservableList<PickupPoint> items = FXCollections.observableArrayList(pickupPoints);
        ListViewPickupPoints.setItems(items);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Предупреждение");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showEditDialog(PickupPoint pickupPoint) {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(TradeApp.class.getResource("pickuppoint-edit-view.fxml"));
        try {
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add("base-styles.css");
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(Manager.mainStage);

            PickupPointEditViewController controller = loader.getController();
            controller.setPickupPoint(pickupPoint);
            controller.setParentController(this);

            stage.showAndWait();
            refreshList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void updateList() {
        refreshList();
    }
}