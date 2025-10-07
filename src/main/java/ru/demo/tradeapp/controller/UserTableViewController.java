package ru.demo.tradeapp.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.beans.property.SimpleStringProperty;
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
import ru.demo.tradeapp.model.User;
import ru.demo.tradeapp.service.UserService;
import ru.demo.tradeapp.util.Manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class UserTableViewController implements Initializable {

    private static final UserService userService = new UserService();

    @FXML
    private Button BtnBack;
    @FXML
    private Button BtnAdd;
    @FXML
    private Button BtnDelete;
    @FXML
    private Button BtnUpdate;
    @FXML
    private TableColumn<User, String> TableColumnUsername;
    @FXML
    private TableColumn<User, String> TableColumnFirstName;
    @FXML
    private TableColumn<User, String> TableColumnSecondName;
    @FXML
    private TableColumn<User, String> TableColumnMiddleName;
    @FXML
    private TableColumn<User, String> TableColumnRole;
    @FXML
    private TableView<User> TableViewUsers;
    @FXML
    private Label LabelUser;
    @FXML
    private MenuItem MenuItemPrintToPDF;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initController();
    }

    public void initController() {
        if (Manager.currentUser != null) {
            LabelUser.setText(Manager.currentUser.getFirstName());
        }
        setCellValueFactories();
        updateTable();
        Manager.secondStage.setMinWidth(800);
        Manager.secondStage.setMinHeight(600);
    }

    private void setCellValueFactories() {
        TableColumnUsername.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
        TableColumnFirstName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
        TableColumnSecondName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSecondName()));
        TableColumnMiddleName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMiddleName()));
        TableColumnRole.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRole().getTitle()));
    }

    private void updateTable() {
        List<User> users = userService.findAll();
        ObservableList<User> observableUsers = FXCollections.observableArrayList(users);
        TableViewUsers.setItems(observableUsers);
    }

    @FXML
    void BtnBackAction(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(TradeApp.class.getResource("main-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            scene.getStylesheets().add("base-styles.css");
            Manager.secondStage.setScene(scene);
        } catch (IOException e) {
            showAlert("Ошибка", "Не удалось загрузить главное окно: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void BtnAddAction(ActionEvent event) {
        Manager.currentUserEdit = null;
        showEditUserWindow();
        updateTable();
    }

    @FXML
    void BtnDeleteAction(ActionEvent event) {
        User user = TableViewUsers.getSelectionModel().getSelectedItem();
        if (user == null) {
            showAlert("Ошибка", "Выберите пользователя для удаления", Alert.AlertType.WARNING);
            return;
        }
        Optional<ButtonType> result = showConfirmDialog("Подтверждение удаления",
                "Вы действительно хотите удалить пользователя " + user.getUsername() + "?");
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                userService.delete(user);
                updateTable();
                showAlert("Успех", "Пользователь успешно удалён", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                showAlert("Ошибка", "Не удалось удалить пользователя: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    void BtnUpdateAction(ActionEvent event) {
        User user = TableViewUsers.getSelectionModel().getSelectedItem();
        if (user == null) {
            showAlert("Ошибка", "Выберите пользователя для редактирования", Alert.AlertType.WARNING);
            return;
        }
        Manager.currentUserEdit = user;
        showEditUserWindow();
        updateTable();
    }

    private void showEditUserWindow() {
        try {
            Stage newWindow = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(TradeApp.class.getResource("user-edit-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            scene.getStylesheets().add("base-styles.css");
            newWindow.setTitle("Редактировать пользователя");
            newWindow.initOwner(Manager.secondStage);
            newWindow.initModality(Modality.WINDOW_MODAL);
            newWindow.setScene(scene);
            newWindow.setMinWidth(400);
            newWindow.setMinHeight(500);
            Manager.currentStage = newWindow;
            newWindow.showAndWait();
            Manager.currentStage = null;
        } catch (IOException e) {
            showAlert("Ошибка", "Не удалось открыть окно редактирования: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void BtnPrintToPDFAction(ActionEvent event) {
        try {
            exportUsersToPDF(Manager.mainStage);
            showAlert("Успех", "PDF успешно сохранён", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Ошибка", "Ошибка при сохранении PDF: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // метод экспорта (универсальный, не принимает User)
    public static void exportUsersToPDF(Stage parentStage) throws FileNotFoundException, DocumentException {
        String FONT = "src/main/resources/fonts/arial.ttf";
        List<User> users = userService.findAll();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf"));
        File file = fileChooser.showSaveDialog(parentStage);

        if (file != null) {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file));
            Font font = FontFactory.getFont(FONT, "cp1251", BaseFont.EMBEDDED, 10);
            document.open();
            document.add(new Paragraph("Список пользователей", font));
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(new float[] {4, 15, 15, 15, 20, 20});
            table.setWidthPercentage(100);

            String[] headers = {"№", "Юзернейм", "Имя", "Фамилия", "Отчество", "Наименование роли"};
            for (String col : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(col, font));
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell.setBorderWidth(2);
                table.addCell(cell);
            }

            int k = 1;
            for (User item : users) {
                PdfPCell title = new PdfPCell();
                table.addCell(String.valueOf(k));
                title.setPhrase(new Phrase(item.getUsername(), font));
                table.addCell(title);
                title.setPhrase(new Phrase(item.getFirstName(), font));
                table.addCell(title);
                title.setPhrase(new Phrase(item.getSecondName(), font));
                table.addCell(title);
                title.setPhrase(new Phrase(item.getMiddleName(), font));
                table.addCell(title);
                title.setPhrase(new Phrase(item.getRole().getTitle(), font));
                table.addCell(title);
                k++;
            }
            document.add(table);
            document.close();
        }
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private Optional<ButtonType> showConfirmDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        return alert.showAndWait();
    }
}
