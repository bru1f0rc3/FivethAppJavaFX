package ru.demo.tradeapp.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import ru.demo.tradeapp.TradeApp;
import ru.demo.tradeapp.model.Order;
import ru.demo.tradeapp.model.Product;
import ru.demo.tradeapp.util.Item;

import java.io.IOException;
import java.util.Map;

public class OrderCell extends ListCell<Item> {

    private final Parent root;
    private OrderCellController controller;

    public OrderCell() {
        try {
            FXMLLoader loader = new FXMLLoader(TradeApp.class.getResource("ordercell-view.fxml"));
            root = loader.load();
            root.getStylesheets().add("base-styles.css");
            controller = loader.getController();
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    @Override
    protected void updateItem(Item item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setGraphic(null);
        } else {
            try {
                controller.setItem(item);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            setGraphic(root);
        }
    }
}
