package ru.demo.tradeapp.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TitledPane;
import ru.demo.tradeapp.util.Manager;

import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {

    @FXML
    private TitledPane TitledPaneHeader;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TitledPaneHeader.setText(Manager.currentUser.getFirstName());
        // Manager.currentUser.getFirstName()
    }
}