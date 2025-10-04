package ru.demo.tradeapp.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ru.demo.tradeapp.model.*;
import ru.demo.tradeapp.service.*;
import ru.demo.tradeapp.util.Manager;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import static ru.demo.tradeapp.util.Manager.MessageBox;

public class ProductEditViewController implements Initializable {
    boolean imageLoaded = false;
    @FXML
    private Button BtnCancel;
    @FXML
    private Button BtnLoadImage;
    @FXML
    private Button BtnSave;
    private CategoryService categoryService = new CategoryService();
    private ManufacturerService manufacturerService = new ManufacturerService();
    private SupplierService supplierService = new SupplierService();
    private UnittypeService unittypeService = new UnittypeService();
    private ProductService productService = new ProductService();
    @FXML
    private ComboBox<Category> ComboBoxCategory;

    @FXML
    private ComboBox<Manufacturer> ComboBoxManufacturer;

    @FXML
    private ComboBox<Supplier> ComboBoxSupplier;

    @FXML
    private ComboBox<Unittype> ComboBoxUnittype;
    @FXML
    private ImageView ImageViewPhoto;
    @FXML
    private TextArea TextAreaDescription;

    @FXML
    private TextField TextFieldArtikul;

    @FXML
    private TextField TextFieldCost;

    @FXML
    private TextField TextFieldCountInStock;

    @FXML
    private TextField TextFieldDiscountAmount;

    @FXML
    private TextField TextFieldDiscountAmountMax;

    @FXML
    private TextField TextFieldTitle;

    @FXML
    void BtnLoadImageAction(ActionEvent event) throws MalformedURLException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg")
        );
        Stage stage = (Stage) BtnLoadImage.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            String imageUrl = file.toURI().toURL().toExternalForm();
            ImageViewPhoto.setImage(new Image(imageUrl));
            imageLoaded = true;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ComboBoxCategory.setItems(FXCollections.observableArrayList(categoryService.findAll()));
        ComboBoxSupplier.setItems(FXCollections.observableArrayList(supplierService.findAll()));
        ComboBoxManufacturer.setItems(FXCollections.observableArrayList(manufacturerService.findAll()));
        ComboBoxUnittype.setItems(FXCollections.observableArrayList(unittypeService.findAll()));
        if (Manager.currentProduct != null) {
            TextFieldArtikul.setEditable(false);
            TextFieldArtikul.setText(Manager.currentProduct.getProductId());
            TextFieldTitle.setText(Manager.currentProduct.getTitle());
            TextAreaDescription.setText(Manager.currentProduct.getDescription());
            TextFieldCost.setText(String.format("%.2f", Manager.currentProduct.getCost()));
            TextFieldCountInStock.setText(Manager.currentProduct.getQuantityInStock().toString());
            TextFieldDiscountAmount.setText(Manager.currentProduct.getDiscountAmount().toString());
            TextFieldDiscountAmountMax.setText(Manager.currentProduct.getMaxDiscountAmount().toString());
            try {
                ImageViewPhoto.setImage(Manager.currentProduct.getPhoto());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ComboBoxCategory.setValue(Manager.currentProduct.getCategory());
            ComboBoxSupplier.setValue(Manager.currentProduct.getSupplier());
            ComboBoxManufacturer.setValue(Manager.currentProduct.getManufacturer());
            ComboBoxUnittype.setValue(Manager.currentProduct.getUnittype());
        } else {
            Manager.currentProduct = new Product();
        }
    }

    @FXML
    void BtnCancelAction(ActionEvent event) {
        Stage stage = (Stage) BtnCancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    void BtnSaveAction(ActionEvent event) throws IOException {
        String error = checkFields().toString();
        if (!error.isEmpty()) {
            MessageBox("Ошибка", "Заполните поля", error, Alert.AlertType.ERROR);
            return;
        }
        Manager.currentProduct.setTitle(TextFieldTitle.getText());
        Manager.currentProduct.setCategory(ComboBoxCategory.getValue());
        Manager.currentProduct.setSupplier(ComboBoxSupplier.getValue());
        Manager.currentProduct.setUnittype(ComboBoxUnittype.getValue());
        Manager.currentProduct.setManufacturer(ComboBoxManufacturer.getValue());
        if (imageLoaded) {
            Manager.currentProduct.setPhoto(ImageViewPhoto.getImage());
        }
        String number = TextFieldCost.getText();
        number = number.replace(',', '.');
        Manager.currentProduct.setCost(Double.parseDouble(number));
        Manager.currentProduct.setDiscountAmount(Integer.parseInt(TextFieldDiscountAmount.getText()));
        Manager.currentProduct.setMaxDiscountAmount(Integer.parseInt(TextFieldDiscountAmountMax.getText()));
        Manager.currentProduct.setQuantityInStock(Integer.parseInt(TextFieldCountInStock.getText()));
        if (Manager.currentProduct.getProductId() == null) {
            Manager.currentProduct.setProductId(TextFieldArtikul.getText());
            productService.save(Manager.currentProduct);
            MessageBox("Информация", "", "Данные сохранены успешно", Alert.AlertType.INFORMATION);
        } else {
            productService.update(Manager.currentProduct);
            MessageBox("Информация", "", "Данные обновлены успешно", Alert.AlertType.INFORMATION);
        }
    }

    StringBuilder checkFields() {
        StringBuilder error = new StringBuilder();
        if (TextFieldArtikul.getText().isEmpty()) {
            error.append("Укажите артикул товара\n");
        }
        if (TextFieldTitle.getText().isEmpty()) {
            error.append("Укажите название товара\n");
        }
        if (TextFieldCost.getText().isEmpty()) {
            error.append("Укажите стоимость товара\n");
        }
        if (ComboBoxCategory.getValue() == null) {
            error.append("Выберите категорию\n");
        }
        if (ComboBoxManufacturer.getValue() == null) {
            error.append("Выберите производителя\n");
        }
        if (ComboBoxSupplier.getValue() == null) {
            error.append("Выберите поставщика\n");
        }
        if (ComboBoxUnittype.getValue() == null) {
            error.append("Выберите единицу измерения\n");
        }

        if (!IsInteger(TextFieldDiscountAmount.getText())) {
            error.append("Действующая скидка должна быть целым числом в диапазоне от 0% до 100%\n");
        }
        if (IsInteger(TextFieldDiscountAmount.getText()) && (Integer.parseInt(TextFieldDiscountAmount.getText()) < 0 || Integer.parseInt(TextFieldDiscountAmount.getText()) > 100)) {
            error.append("Действующая скидка должна быть целым числом в диапазоне от 0% до 100%\n");
        }
        if (!IsInteger(TextFieldDiscountAmountMax.getText())) {
            error.append("Максимальная скидка должна быть целым числом в диапазоне от 0% до 100%\n");
        }
        if (IsInteger(TextFieldDiscountAmountMax.getText()) && (Integer.parseInt(TextFieldDiscountAmountMax.getText()) < 0 || Integer.parseInt(TextFieldDiscountAmountMax.getText()) > 100)) {
            error.append("Максимальная скидка должна быть целым числом в диапазоне от 0% до 100%\n");
        }
        if (IsInteger(TextFieldDiscountAmountMax.getText()) && IsInteger(TextFieldDiscountAmount.getText())) {
            int maxDiscount = Integer.parseInt(TextFieldDiscountAmountMax.getText());
            int discount = Integer.parseInt(TextFieldDiscountAmount.getText());
            if (discount > maxDiscount)
                error.append("Действующая скидка не может быть больше максимальной\n");
        }
        if (!IsInteger(TextFieldCountInStock.getText())) {
            error.append("Количество товара на складе должно быть целым числом\n");
        }
        if (IsInteger(TextFieldCountInStock.getText()) && Integer.parseInt(TextFieldCountInStock.getText()) < 0) {
            error.append("Количество товара на складе должно быть положительным целым числом\n");
        }

        if (!IsDouble(TextFieldCost.getText())) {
            error.append("Стоимость должна быть положительным числом\n");
        }
        if (IsDouble(TextFieldCost.getText()) && Double.parseDouble(TextFieldCost.getText().replace(',', '.')) < 0) {
            error.append("Стоимость должна быть положительным числом\n");
        }

        return error;
    }

    boolean IsInteger(String number) {
        if (number == null) {
            return false;
        }
        try {
            int d = Integer.parseInt(number);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    boolean IsDouble(String number) {
        if (number == null) {
            return false;
        }
        try {
            number = number.replace(',', '.');
            double d = Double.parseDouble(number);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

}
