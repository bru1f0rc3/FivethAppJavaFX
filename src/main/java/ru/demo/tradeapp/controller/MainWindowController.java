package ru.demo.tradeapp.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.event.ActionEvent;
import javafx.scene.control.*;

import ru.demo.tradeapp.model.Category;
import ru.demo.tradeapp.model.Product;
import ru.demo.tradeapp.service.CategoryService;
import ru.demo.tradeapp.service.ProductService;
import ru.demo.tradeapp.util.Manager;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MainWindowController implements Initializable {

    private CategoryService categoryService = new CategoryService();
    private ProductService productService = new ProductService();
    @FXML
    private Button BtnBack;
    @FXML
    private ListView<Product> ListViewProducts;
    @FXML
    private Button BtnProducts;

    @FXML
    private ComboBox<Category> CmboBoxProductType;

    @FXML
    private ComboBox<?> ComboboxSort;

    @FXML
    private Label LabelInfo;

    @FXML
    private Label LabelUser;

    @FXML
    private TextField TextFieldSearch;

    @FXML
    void BtnBackAction(ActionEvent event) {

    }

    @FXML
    void BtnProductsAction(ActionEvent event) {

    }

    @FXML
    void CmboBoxProductTypeAction(ActionEvent event) {
        Category category = CmboBoxProductType.getValue();
        if (category.getCategoryId() == 0) {
            loadProducts(null);
        } else {
            loadProducts(category);
        }
        System.out.println(category);
    }

    @FXML
    void ComboboxSortAction(ActionEvent event) {

    }

    @FXML
    void TextFieldSearchAction(ActionEvent event) {

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LabelUser.setText(Manager.currentUser.getFirstName());
        List<Category> categoryList = categoryService.findAll();
        categoryList.add(0, new Category(0L, "Все"));
        ObservableList<Category> categories = FXCollections.observableArrayList(categoryList);
        CmboBoxProductType.setItems(categories);
        loadProducts(null);

    }

    public void loadProducts(Category category) {
        ListViewProducts.getItems().clear();
        List<Product> products = productService.findAll();
        if (category != null) {
            products = products.stream().filter(product -> product.getCategory().getCategoryId().equals(category.getCategoryId())).collect(Collectors.toList());
        }
        for (Product product : products) {
            ListViewProducts.getItems().add(product);
        }
        ListViewProducts.setCellFactory(lv -> new ProductCell());
    }
}
