package ru.demo.tradeapp.controller;


import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.InputMethodEvent;
import ru.demo.tradeapp.TradeApp;
import ru.demo.tradeapp.model.Category;
import ru.demo.tradeapp.model.Product;
import ru.demo.tradeapp.service.CategoryService;
import ru.demo.tradeapp.util.Manager;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static ru.demo.tradeapp.util.Manager.*;

public class CategoryTableViewController implements Initializable {

    @FXML
    private MenuItem MenuItemAdd;

    @FXML
    private MenuItem MenuItemBack;

    @FXML
    private MenuItem MenuItemDelete;


    @FXML
    private MenuItem MenuItemUpdate;


    @FXML
    private Label LabelInfo;

    @FXML
    private Label LabelUser;

    @FXML
    private TableColumn<Category, String> TableColumnId;

    @FXML
    private TableColumn<Category, String> TableColumnTitle;

    @FXML
    private TableView<Category> TableViewCategories;

    @FXML
    private TextField TextFieldSearch;
    private int itemsCount;
    private CategoryService categoryService = new CategoryService();


    @FXML
    void BtnBackAction(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(TradeApp.class.getResource("products-table-view.fxml"));

        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), screenSize.getWidth(), screenSize.getHeight());
            scene.getStylesheets().add("base-styles.css");
            Manager.secondStage.setMaximized(true);
            Manager.secondStage.setScene(scene);

            //Manager.mainStage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        LabelUser.setText("Вы вошли как " + currentUser.getSecondName()+ " "+ Manager.currentUser.getFirstName());
        setCellValueFactories();
        filterData();
    }

    void filterData() {
        List<Category> categories = categoryService.findAll();
        itemsCount = categories.size();

        String searchText = TextFieldSearch.getText();
        if (!searchText.isEmpty()) {
            categories = categories.stream().filter(product -> product.getTitle().toLowerCase().contains(searchText.toLowerCase())).collect(Collectors.toList());
        }
        TableViewCategories.getItems().clear();
        for (Category category : categories) {
            TableViewCategories.getItems().add(category);
        }

        int filteredItemsCount = categories.size();
        LabelInfo.setText("Всего записей " + filteredItemsCount + " из " + itemsCount);
    }

    private void setCellValueFactories() {

        TableColumnId.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategoryId().toString()));
        TableColumnTitle.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
    }

    @FXML
    void MenuItemAddAction(ActionEvent event) {
        Manager.currentProduct = null;
        //ShowEditProductWindow();
        filterData();
    }

    @FXML
    void MenuItemBackAction(ActionEvent event) {
        Manager.LoadSecondStageScene("products-table-view.fxml");
    }


    @FXML
    void MenuItemDeleteAction(ActionEvent event) {
        Category category = TableViewCategories.getSelectionModel().getSelectedItem();
        if (!category.getProducts().isEmpty()) {
            ShowErrorMessageBox("Ошибка целостности данных, у данного товара есть зависимые заказы");
            return;
        }

        Optional<ButtonType> result = ShowConfirmPopup();
        if (result.get() == ButtonType.OK) {
            categoryService.delete(category);
            filterData();
        }
    }


    @FXML
    void MenuItemUpdateAction(ActionEvent event) {
        Category category = TableViewCategories.getSelectionModel().getSelectedItem();
        // Manager.currentProduct = category;
        // ShowEditCategoryWindow();
        filterData();
    }

}
