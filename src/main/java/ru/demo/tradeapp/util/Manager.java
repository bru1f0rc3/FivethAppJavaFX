package ru.demo.tradeapp.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import ru.demo.tradeapp.TradeApp;
import ru.demo.tradeapp.controller.MainWindowController;
import ru.demo.tradeapp.model.*;
import ru.demo.tradeapp.service.OrderService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class Manager {
    public static Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
    public static OrderService orderService = new OrderService();

    public static MainWindowController mainWindowController;
    public static User currentUser = null;
    public static Stage mainStage;
    public static Stage secondStage;
    public static Stage currentStage;
    public static Product currentProduct;
    public static Category currentCategory;
    public static Supplier currentSupplier;
    public static Manufacturer currentManufacturer;
    public static Unittype currentUnittype;
    public static User currentUserEdit;

    public static Basket mainBasket = new Basket();

    public static void ShowPopup() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Закрыть приложение");
        alert.setHeaderText("Вы хотите выйти из приложения?");
        alert.setContentText("Все несохраненные данные, будут утеряны");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Platform.exit();
        }
    }

    public static void ShowErrorMessageBox(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    public static void MessageBox(String title, String header, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();

    }

    public static Optional<ButtonType> ShowConfirmPopup() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Удаление");
        alert.setHeaderText("Вы действительно хотите удалить запись?");
        alert.setContentText("Также будут удалены все зависимые от этой записи данные");
        Optional<ButtonType> result = alert.showAndWait();
        return result;
    }

    public static void LoadSecondStageScene(String fxmlFileName)
    {

        FXMLLoader fxmlLoader = new FXMLLoader(TradeApp.class.getResource(fxmlFileName));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), screenSize.getWidth(), screenSize.getHeight());
            scene.getStylesheets().add("base-styles.css");
            Manager.secondStage.setScene(scene);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void LoadOrderScene(String fxmlFileName)
    {

    }

    public static void PrintOrderToPDF(Order order) throws FileNotFoundException, DocumentException {
        String FONT = "src/main/resources/fonts/arial.ttf";
        List<OrderProduct> orderProducts = orderService.findOne(order.getOrderId()).getOrderProducts();


        FileChooser fileChooser = new FileChooser();

        //Set extension filter for text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.PDF)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(mainStage);

        if (file != null) {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file));
            Font font = FontFactory.getFont(FONT, "cp1251", BaseFont.EMBEDDED, 10);
            document.open();
            document.add(new Paragraph("Заказ №: " + order.getOrderId(), font));
            if (order.getUser() != null)
                document.add(new Paragraph("на имя " + order.getUser().getSecondName() + " " + order.getUser().getFirstName(), font));
            document.add(new Paragraph("Дата заказа: " + order.getCreateDate(), font));
            document.add(new Paragraph("Дата получения заказа: " + order.getDeliveryDate(), font));
            document.add(new Paragraph("Пункт выдачи заказа: " + order.getPickupPoint().getTitle(), font));
            document.add(new Paragraph("Код получения: " + order.getGetCode(), font));
            document.add(Chunk.NEWLINE);
            PdfPTable table = new PdfPTable(new float[]{10, 30, 12, 12, 12, 12, 12});
            PdfPCell header = new PdfPCell();
            header.setBackgroundColor(BaseColor.LIGHT_GRAY);
            header.setBorderWidth(2);
            header.setPhrase(new Phrase("№", font));
            table.addCell(header);
            header.setPhrase(new Phrase("Наименование товара", font));
            table.addCell(header);
            header.setPhrase(new Phrase("Количество", font));
            table.addCell(header);
            header.setPhrase(new Phrase("Стоимость без скидки", font));
            table.addCell(header);
            header.setPhrase(new Phrase("Скидка", font));
            table.addCell(header);
            header.setPhrase(new Phrase("Стоимость с учетом скидки", font));
            table.addCell(header);
            header.setPhrase(new Phrase("ИТОГО:", font));
            table.addCell(header);
            table.setWidthPercentage(100);

            int k = 1;
            for (OrderProduct item : orderProducts) {
                table.addCell(String.valueOf(k));
                PdfPCell title = new PdfPCell();
                title.setPhrase(new Phrase(item.getProduct().getTitle(), font));
                table.addCell(title);
                table.addCell(String.valueOf(item.getCount()));
                table.addCell(String.format("%.2f", item.getProduct().getCost()));
                table.addCell(item.getProduct().getDiscountAmount() + "%");
                table.addCell(String.format(String.format("%.2f", item.getProduct().getPriceWithDiscount())));
                table.addCell(String.format("%.2f", item.getProduct().getPriceWithDiscount() * item.getCount()));
                k++;
            }

            document.add(table);
            document.add(new Paragraph("Общая сумма заказа: " + String.format("%.2f руб.", order.getTotalCostWithDiscount()), font));
            document.add(new Paragraph("Общий размер скидки: " + String.format("%.2f", order.getTotalDiscount()) + "%", font));
            document.close();
        }
    }
}