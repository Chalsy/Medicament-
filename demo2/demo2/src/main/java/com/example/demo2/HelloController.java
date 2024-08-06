package com.example.demo2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.SortEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    private TextField nomcomp;
    @FXML
    private TextField prixcomp;
    @FXML
    private TextField qtecomp;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
    @FXML
    public void ajouteraction(ActionEvent actionEvent) {
    }
    @FXML
    public void supprimeraction(ActionEvent actionEvent) {
    }
    @FXML
    public void rechercheraction(ActionEvent actionEvent) {
    }
    @FXML
    public void afficheraction(ActionEvent actionEvent) {
    }
    @FXML
    public void Actioncomp(SortEvent<TableView> tableViewSortEvent) {
    }
}