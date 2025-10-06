package ru.demo.tradeapp.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import ru.demo.tradeapp.model.*;
import ru.demo.tradeapp.service.OrderProductService;
import ru.demo.tradeapp.service.OrderService;
import ru.demo.tradeapp.service.PickupPointService;
import ru.demo.tradeapp.service.StatusService;
import ru.demo.tradeapp.util.Item;
import ru.demo.tradeapp.util.Manager;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static ru.demo.tradeapp.util.Manager.*;

public class OrderViewController implements Initializable {


    PickupPointService pickupPointService = new PickupPointService();
    StatusService statusService = new StatusService();
    OrderService orderService = new OrderService();
    OrderProductService orderProductService = new OrderProductService();
    Order newOrder;
    @FXML
    private BorderPane BorderPaneMainFrame;

    @FXML
    private Button BtnDelete;

    @FXML
    private Button BtnOk;

    @FXML
    private ComboBox<PickupPoint> ComboBoxPickupPoint;

    @FXML
    private ComboBox<Status> ComboStatus;

    @FXML
    private DatePicker DatePickerOrderCreateDate;

    @FXML
    private DatePicker DatePickerOrderDeliveryDate;

    @FXML
    private Label LabelBasketInfo;

    @FXML
    private Label LabelOrderGetCode;

    @FXML
    private Label LabelOrderNumber;

    @FXML
    private Label LabelUser;

    @FXML
    private ListView<Item> ListViewProducts;

    @FXML
    void BtnDeleteAction(ActionEvent event) {
        Item item = ListViewProducts.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Удаление");
        alert.setHeaderText("Вы действительно хотите удалить товар " + item.getProduct().getTitle() + " из корзины?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            mainBasket.deleteProductFromBasket(item.getProduct());
            loadProducts();
        }
    }

    @FXML
    void BtnOkAction(ActionEvent event) {
        String error = checkFields().toString();
        if (!error.isEmpty()) {
            MessageBox("Ошибка", "Заполните поля", error, Alert.AlertType.ERROR);
            return;
        }
        newOrder.setUser(currentUser);
        newOrder.setStatus(ComboStatus.getValue());
        newOrder.setPickupPoint(ComboBoxPickupPoint.getValue());

        Set<OrderProduct> orderProducts = new HashSet<>();
        for (Item item : mainBasket.getBasket().values()) {
            OrderProduct orderProduct = new OrderProduct(newOrder, item.getProduct(), Long.valueOf(item.getCount()));
            orderProducts.add(orderProduct);
        }

        orderService.save(newOrder);



//       for (OrderProduct orderProduct :orderProducts) {
//            orderProductService.save(orderProduct);}


        MessageBox("Информация", "", "Данные сохранены успешно", Alert.AlertType.INFORMATION);

    }

    @FXML
    void ComboBoxPickupPointAction(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ComboStatus.setValue(new Status(1L, "Новый"));
        LabelUser.setText("Вы вошли как " + currentUser.getSecondName() + " " + Manager.currentUser.getFirstName());
        ComboBoxPickupPoint.setItems(FXCollections.observableArrayList(pickupPointService.findAll()));
        ComboStatus.setItems(FXCollections.observableArrayList(statusService.findAll()));
        newOrder = CreateNewOrder();
        LabelOrderNumber.setText("Заказ №" + newOrder.getOrderId() + " на имя " + currentUser.getSecondName() + " " + currentUser.getFirstName());
        DatePickerOrderCreateDate.setValue(newOrder.getCreateDate());
        DatePickerOrderDeliveryDate.setValue(newOrder.getDeliveryDate());
        LabelOrderGetCode.setText("Код выдачи: " + newOrder.getGetCode());
        loadProducts();
    }

    public void loadProducts() {
        ListViewProducts.getItems().clear();

        for (Map.Entry<Product, Item> entry : Manager.mainBasket.getBasket().entrySet()) {
            ListViewProducts.getItems().add(entry.getValue());
        }
        ListViewProducts.setCellFactory(lv -> new OrderCell());
        LabelBasketInfo.setText("Общая сумма заказа: " + Manager.mainBasket.getTotalCost() + ". Общий размер скидки: " + mainBasket.getTotalDiscount() + "%");
    }

    public Order CreateNewOrder() {
        Order order = new Order();

        Optional<Order> maxOrder = orderService.findAll().stream().max(Comparator.comparing(Order::getOrderId));
        if (maxOrder.isPresent())
            order.setOrderId(maxOrder.get().getOrderId() + 1);
        else {
            order.setOrderId(1L);
        }
        order.setCreateDate(LocalDate.now());
        if (Manager.mainBasket.isOnStock())
            order.setDeliveryDate(order.getCreateDate().plusDays(3));
        else
            order.setDeliveryDate(order.getCreateDate().plusDays(6));

        Random rnd = new Random();
        order.setGetCode(rnd.nextInt(100, 1000));
        return order;
    }

    StringBuilder checkFields() {
        StringBuilder error = new StringBuilder();

        if (ComboStatus.getValue() == null) {
            error.append("Выберите статус\n");
        }
        if (ComboBoxPickupPoint.getValue() == null) {
            error.append("Выберите пункт выдачи\n");
        }


        return error;
    }

}
