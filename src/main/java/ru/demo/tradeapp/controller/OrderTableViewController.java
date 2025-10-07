package ru.demo.tradeapp.controller;

import com.itextpdf.text.DocumentException;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.InputMethodEvent;
import ru.demo.tradeapp.model.Category;
import ru.demo.tradeapp.model.Order;
import ru.demo.tradeapp.model.PickupPoint;
import ru.demo.tradeapp.model.Product;
import ru.demo.tradeapp.service.OrderService;
import ru.demo.tradeapp.service.PickupPointService;
import ru.demo.tradeapp.service.ProductService;
import ru.demo.tradeapp.util.Manager;

import java.io.FileNotFoundException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static ru.demo.tradeapp.util.Manager.*;

public class OrderTableViewController implements Initializable {
    private int itemsCount;
    private final OrderService orderService = new OrderService();
    private final PickupPointService pickupPointService = new PickupPointService();
    @FXML
    private ComboBox<PickupPoint> ComboBoxPickupPoint;

    @FXML
    private Label LabelInfo;

    @FXML
    private Label LabelUser;

    @FXML
    private MenuItem MenuItemAdd;

    @FXML
    private MenuItem MenuItemBack;

    @FXML
    private MenuItem MenuItemDelete;

    @FXML
    private MenuItem MenuItemUpdate;

    @FXML
    private TableColumn<Order, String> TableColumnCreateDate;

    @FXML
    private TableColumn<Order, String> TableColumnDeliveryDate;

    @FXML
    private TableColumn<Order, Integer> TableColumnGetCode;
    @FXML
    private MenuItem MenuItemPrintToPDF;
    @FXML
    private TableColumn<Order, Long> TableColumnId;

    @FXML
    private TableColumn<Order, String> TableColumnPickupPoint;

    @FXML
    private TableColumn<Order, String> TableColumnStatus;

    @FXML
    private TableColumn<Order, String> TableColumnUser;

    @FXML
    private TableView<Order> TableViewOrders;

    @FXML
    private TextField TextFieldSearch;

    @FXML
    void ComboBoxPickupPointAction(ActionEvent event) {
        filterData();
    }

    @FXML
    void MenuItemPrintToPDFAction(ActionEvent event) throws DocumentException, FileNotFoundException {
        Order order = TableViewOrders.getSelectionModel().getSelectedItem();

        if (order != null) {
            Manager.PrintOrderToPDF(order);
            MessageBox("Информация", "", "Данные сохранены успешно", Alert.AlertType.INFORMATION);return;
        }
    }
    @FXML
    void MenuItemAddAction(ActionEvent event) {

    }

    @FXML
    void MenuItemBackAction(ActionEvent event) {
        Manager.LoadSecondStageScene("main-view.fxml");
    }

    @FXML
    void MenuItemDeleteAction(ActionEvent event) {

    }

    @FXML
    void MenuItemUpdateAction(ActionEvent event) {

    }

    @FXML
    void TextFieldTextChanged(InputMethodEvent event) {

    }
    @FXML
    void TextFieldSearchAction(ActionEvent event) {
        filterData();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initController();
    }

    public void initController() {
        List<PickupPoint> pickupPointList = pickupPointService.findAll();
        pickupPointList.add(0, new PickupPoint(0L, "Все"));
        ObservableList<PickupPoint> pickupPoints = FXCollections.observableArrayList(pickupPointList);
        ComboBoxPickupPoint.setItems(pickupPoints);
        LabelUser.setText("Вы вошли как " + currentUser.getSecondName()+ " "+ Manager.currentUser.getFirstName());
        setCellValueFactories();
        filterData();
    }

    void filterData() {
        List<Order> orders = orderService.findAll();
        itemsCount = orders.size();

        String searchText = TextFieldSearch.getText();
        if (!searchText.isEmpty()) {
            orders = orders.stream().filter(product -> product.getOrderId().toString().contains(searchText.toLowerCase())).collect(Collectors.toList());
        }
        if (!ComboBoxPickupPoint.getSelectionModel().isEmpty()) {
            PickupPoint pickupPoint = ComboBoxPickupPoint.getValue();
            if (pickupPoint.getPickupPointId() != 0) {
                orders = orders.stream().filter(order -> order.getPickupPoint().getPickupPointId().equals(pickupPoint.getPickupPointId())).collect(Collectors.toList());
            }
        }
        TableViewOrders.getItems().clear();
        for (Order order : orders) {
            TableViewOrders.getItems().add(order);
        }

        int filteredItemsCount = orders.size();
        LabelInfo.setText("Всего записей " + filteredItemsCount + " из " + itemsCount);
    }

    private void setCellValueFactories() {

        TableColumnId.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getOrderId()).asObject());
        TableColumnStatus.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus().getTitle()));
        TableColumnPickupPoint.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPickupPoint().getTitle()));
        TableColumnCreateDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCreateDate().toString()));

        TableColumnDeliveryDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDeliveryDate().toString()));
        TableColumnGetCode.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getGetCode()).asObject());

        TableColumnUser.setCellValueFactory(cellData -> {
            if (cellData.getValue().getUser() != null)
                return new SimpleStringProperty(cellData.getValue().getUser().getFirstName());
            else
                return new SimpleStringProperty("");
        });
    }
}
