package ru.demo.tradeapp.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.demo.tradeapp.TradeApp;
import ru.demo.tradeapp.model.PickupPoint;
import ru.demo.tradeapp.model.Unittype;
import ru.demo.tradeapp.service.PickupPointService;
import ru.demo.tradeapp.util.Manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class PickupPointTableViewController implements Initializable {

    @FXML
    private Button BtnAdd;

    @FXML
    private Button BtnDelete;

    @FXML
    private Button BtnEdit;
    
    @FXML
    private Button BtnBack;

    @FXML
    private ListView<PickupPoint> ListViewPickupPoints;

    private static PickupPointService pickupPointService = new PickupPointService();
    PickupPoint newPickupPoint;
    @FXML
    void BtnAddAction(ActionEvent event) {
        showEditDialog(null);
    }

    @FXML
    void BtnDeleteAction(ActionEvent event) {
        PickupPoint selectedPickupPoint = ListViewPickupPoints.getSelectionModel().getSelectedItem();
        if (selectedPickupPoint == null) {
            showAlert("Выберите пункт выдачи для удаления");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение удаления");
        alert.setHeaderText("Вы уверены, что хотите удалить пункт выдачи?");
        alert.setContentText("Адрес: " + selectedPickupPoint.getTitle());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            pickupPointService.delete(selectedPickupPoint);
            refreshList();
        }
    }

    @FXML
    void BtnEditAction(ActionEvent event) {
        PickupPoint selectedPickupPoint = ListViewPickupPoints.getSelectionModel().getSelectedItem();
        if (selectedPickupPoint == null) {
            showAlert("Выберите пункт выдачи для редактирования");
            return;
        }
        showEditDialog(selectedPickupPoint);
    }
    
    @FXML
    void BtnBackAction(ActionEvent event) {
        Manager.LoadSecondStageScene("main-view.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refreshList();
        ListViewPickupPoints.setCellFactory(lv -> new ListCell<PickupPoint>() {
            @Override
            protected void updateItem(PickupPoint item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.getTitle());
                }
            }
        });
    }

    @FXML
    private MenuItem MenuItemPrintToPDF;

    @FXML
    void BtnPrintToPDFAction(ActionEvent event) throws IOException, DocumentException {
        PrintCategoryToPDF(newPickupPoint);
    }

    public static void PrintCategoryToPDF(PickupPoint pickupPoint) throws FileNotFoundException, DocumentException {
        String FONT = "src/main/resources/fonts/arial.ttf";
        List<PickupPoint> categories = pickupPointService.findAll();

        FileChooser fileChooser = new FileChooser();

        //Show save file dialog
        File file = fileChooser.showSaveDialog(Manager.mainStage);
        // ЭТО ДЛЯ ТОГО ЧТОБЫ РАСПЕЧАТАТЬ ДАННЫЕ В ПДФ ФАЙЛ
        if (file != null) {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file));
            Font font = FontFactory.getFont(FONT, "cp1251", BaseFont.EMBEDDED, 10);
            document.open();
            document.add(new Paragraph("Список пунктов выдачи товаров", font));
            document.add(Chunk.NEWLINE);
            PdfPTable table = new PdfPTable(new float[]{10, 30});
            PdfPCell header = new PdfPCell();
            header.setBackgroundColor(BaseColor.LIGHT_GRAY);
            header.setBorderWidth(2);
            header.setPhrase(new Phrase("№", font));
            table.addCell(header);
            header.setPhrase(new Phrase("Пункты выдачи:", font));
            table.addCell(header);
            table.setWidthPercentage(100);

            int k = 1;
            for (PickupPoint item : categories) {
                table.addCell(String.valueOf(k));
                PdfPCell title = new PdfPCell();
                title.setPhrase(new Phrase(item.getTitle(), font));
                table.addCell(title);
                k++;
            }

            document.add(table);
            document.close();
        }
    }

    private void refreshList() {
        List<PickupPoint> pickupPoints = pickupPointService.findAll();
        ObservableList<PickupPoint> items = FXCollections.observableArrayList(pickupPoints);
        ListViewPickupPoints.setItems(items);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Предупреждение");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showEditDialog(PickupPoint pickupPoint) {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(TradeApp.class.getResource("pickuppoint-edit-view.fxml"));
        try {
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add("base-styles.css");
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(Manager.mainStage);

            PickupPointEditViewController controller = loader.getController();
            controller.setPickupPoint(pickupPoint);
            controller.setParentController(this);

            stage.showAndWait();
            refreshList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void updateList() {
        refreshList();
    }
}