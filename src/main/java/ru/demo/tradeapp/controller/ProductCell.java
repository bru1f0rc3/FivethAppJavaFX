package ru.demo.tradeapp.controller;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import ru.demo.tradeapp.TradeApp;
import ru.demo.tradeapp.model.Product;

public class ProductCell extends ListCell<Product> {

    private final Parent root ;
    private ListCellController controller ;

    public ProductCell() {
        try {
            FXMLLoader loader = new FXMLLoader(TradeApp.class.getResource("productcell-view.fxml"));
            root = loader.load();
            controller = loader.getController() ;
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    @Override
    protected void updateItem(Product product, boolean empty) {
        super.updateItem(product, empty);
        if (empty || product==null) {
            setGraphic(null);
        } else {
            try {
                controller.setProduct(product);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            setGraphic(root);
        }
    }
}
