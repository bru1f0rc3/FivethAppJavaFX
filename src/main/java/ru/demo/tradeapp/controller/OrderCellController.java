package ru.demo.tradeapp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import ru.demo.tradeapp.model.Product;
import ru.demo.tradeapp.util.Item;

import java.io.IOException;
import java.util.Map;

public class OrderCellController {

    @FXML
    private AnchorPane CellAnchorPane;

    @FXML
    private ImageView ImageViewPhoto;

    @FXML
    private Label LabelBasePrice;

    @FXML
    private Label LabelCountInBasket;

    @FXML
    private Label LabelCountInStock;

    @FXML
    private Label LabelDescription;

    @FXML
    private Label LabelInfo;

    @FXML
    private Label LabelManufacturer;

    @FXML
    private Label LabelPercent;

    @FXML
    private Label LabelPriceWithDiscount;

    @FXML
    private Label LabelTitle;

    @FXML
    void AddProductInBasket(ActionEvent event) {

    }

    public void setItem(Item item) throws IOException {
        Product product = item.getProduct();
        ImageViewPhoto.setImage(product.getPhoto());
        LabelPercent.setText(product.getDiscountAmount().toString() + "%");
        LabelDescription.setText(product.getDescription());
        LabelTitle.setText(product.getTitle());
        LabelManufacturer.setText("Производитель: " + product.getManufacturer().getTitle());

        LabelCountInBasket.setText("Количество: " + item.getCount());
        LabelCountInStock.setText("В наличии на складе: " + product.getQuantityInStock());

        LabelInfo.setText("Итого: " + String.format("%.2f", item.getTotal()) + " руб.");
        if (product.getDiscountAmount() >= 15) {
            CellAnchorPane.setStyle("-fx-background-color: #7fff00;");
        } else {
            CellAnchorPane.setStyle("-fx-background-color: #fff;");
        }

        LabelBasePrice.setText(String.format("%.2f", product.getCost()) + " руб.");
        LabelPriceWithDiscount.setVisible(false);
        LabelBasePrice.setStyle("-fx-text-fill: #000000;");
        if (product.getDiscountAmount() > 0) {
            LabelPriceWithDiscount.setText(String.format("%.2f", product.getPriceWithDiscount()) + " руб.");
            LabelPriceWithDiscount.setVisible(true);
            LabelPriceWithDiscount.setStyle("-fx-text-fill: #0000FF;");
            LabelBasePrice.setStyle("-fx-text-fill: #FF0000;");
        }

    }



}
