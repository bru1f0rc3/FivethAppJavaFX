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
import java.util.Comparator;
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
    private ComboBox<String> ComboboxSort;

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
    void TextFieldSearchAction(ActionEvent event) {

    }

    @FXML
    void ComboboxSortAction(ActionEvent event) {

    }

    private void updateSearch() {
        List<Product> products = productService.findAll();

        Category selectedCategory = CmboBoxProductType.getValue();
        if (selectedCategory != null && selectedCategory.getCategoryId() != 0) {
            products = products.stream()
                    .filter(product -> product.getCategory().getCategoryId().equals(selectedCategory.getCategoryId()))
                    .collect(Collectors.toList());
        }

        String query = TextFieldSearch.getText().toLowerCase().trim();
        if (!query.isEmpty()) {
            products = products.stream()
                    .filter(p -> p.getTitle().toLowerCase().contains(query))
                    .collect(Collectors.toList());
        }

        String sort = ComboboxSort.getValue();
        if (sort != null) {
            switch (sort) {
                case "По возрастанию":
                    products.sort(Comparator.comparing(Product::getCost));
                    break;
                case "По убыванию":
                    products.sort(Comparator.comparing(Product::getCost).reversed());
                    break;
                case "0%-9,99%":
                    products = products.stream()
                            .filter(product -> product.getDiscountAmount() >= 0 && product.getDiscountAmount() < 10)
                            .collect(Collectors.toList());
                    break;
                case "10%-14,99%":
                    products = products.stream()
                            .filter(product -> product.getDiscountAmount() >= 10 && product.getDiscountAmount() < 15)
                            .collect(Collectors.toList());
                    break;
                case "15% и более":
                    products = products.stream()
                            .filter(product -> product.getDiscountAmount() >= 15)
                            .collect(Collectors.toList());
                    break;
            }
        }

        ListViewProducts.setItems(FXCollections.observableArrayList(products));
        LabelInfo.setText(products.size() + " из " + (long) productService.findAll().size());
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LabelUser.setText(Manager.currentUser.getFirstName());
        List<Category> categoryList = categoryService.findAll();
        categoryList.add(0, new Category(0L, "Все"));
        ObservableList<Category> categories = FXCollections.observableArrayList(categoryList);
        CmboBoxProductType.setItems(categories);
        loadProducts(null);
        TextFieldSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            updateSearch();
        });

        CmboBoxProductType.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateSearch();
        });

        ComboboxSort.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateSearch();
        });
        ObservableList<String> items = FXCollections.observableArrayList("Все диапазоны",
                "0%-9,99%",
                "10%-14,99%",
                "15% и более",
                "По возрастанию",
                "По убыванию");
        ComboboxSort.setItems(items);
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
