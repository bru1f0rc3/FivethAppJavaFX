package ru.demo.tradeapp.controller;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import ru.demo.tradeapp.model.Product;
import javafx.scene.text.Text;

import java.io.IOException;

public class ListCellController {

    @FXML
    private ImageView ImageViewPhoto;

    @FXML
    private Label LabelDescription;

    @FXML
    private Label LabelManufacturer;

    @FXML
    private Label LabelPercent;

    @FXML
    private Label LabelTitle;

    @FXML
    private Label LabelPriceProduct;

    public void setProduct(Product product) throws IOException {
        ImageViewPhoto.setImage(product.getPhoto());
        LabelPercent.setText(product.getDiscountAmount().toString());

        if (product.getDiscountAmount() >= 15) {
            String oldPrice = product.getCost().toString();
            LabelPriceProduct.setStyle("-fx-background-color: #7fff00;");

            Text text = new Text(oldPrice);
            text.setStrikethrough(true);
            text.setFill(Color.BLACK);

            Group group = new Group(text);
            LabelPriceProduct.setGraphic(group);

            double discountValue = product.getCost() * (product.getDiscountAmount() / 100.0);
            double newPrice = product.getCost() - discountValue;
            LabelPriceProduct.setText(String.format("%.2f",newPrice));
        }
        else {
            LabelPriceProduct.setText(product.getCost().toString());
        }

        LabelDescription.setText(product.getDescription());
        LabelTitle.setText(product.getTitle());
        LabelManufacturer.setText(product.getManufacturer().getTitle());
    }
}

