package ru.demo.tradeapp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import ru.demo.tradeapp.model.Product;
import ru.demo.tradeapp.util.Manager;

import java.io.IOException;

import static ru.demo.tradeapp.util.Manager.*;

public class ListCellController {

    Product currentProduct;
    @FXML
    private ImageView ImageViewPhoto;
    @FXML
    private Label LabelDescription;
    @FXML
    private Label LabelManufacturer;
    @FXML
    private Label LabelBasePrice;
    @FXML
    private Label LabelPriceWithDiscount;
    @FXML
    private Label LabelPercent;
    @FXML
    private Label LabelTitle;
    @FXML
    private AnchorPane CellAnchorPane;

    @FXML
    void AddProductInBasket(ActionEvent event) {

        MessageBox("Информация", "Добавлен новый товар в корзину", currentProduct.getTitle(), Alert.AlertType.INFORMATION );
        Manager.mainBasket.addProductInBasket(currentProduct);
        if (mainBasket.getCount() > 0) {
            mainWindowController.BtnBasket.setVisible(true);
            mainWindowController.LabelBasketInfo.setVisible(true);
            mainWindowController.LabelBasketInfo.setText("В корзине " + mainBasket.getCount() + " товаров");
        }

    }

    public void setProduct(Product product) throws IOException {
        currentProduct = product;
        ImageViewPhoto.setImage(product.getPhoto());
        LabelPercent.setText(product.getDiscountAmount().toString() + "%");
        LabelDescription.setText(product.getDescription());
        LabelTitle.setText(product.getTitle());
        LabelManufacturer.setText("Производитель: " + product.getManufacturer().getTitle());
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
