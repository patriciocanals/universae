package com.universae;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;

    @FXML
    private void login() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Todos los campos deben ser obligatorios.");
        } else {
            int userId = User.login(username, password);
            if (userId > 0) {
                try {
                    System.out.println("intento de carga de la pantalla de medicamento");
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/universae/Medications.fxml"));
                    if (loader.getLocation() == null) {
                        System.out.println("no se enconto");
                        errorLabel.setText("no esta lapantalla de medicamento");
                        return;
                    }
                    Scene scene = new Scene(loader.load(), 600, 400);
                    MedicationsController controller = loader.getController();
                    controller.setUserId(userId);
                    Stage stage = (Stage) usernameField.getScene().getWindow();
                    stage.setTitle("MediTrack - Gestión de Medicamentos");
                    stage.setScene(scene);
                    System.out.println("cargado el loaded");
                } catch (Exception e) {
                    System.out.println("error en el loading " + e.getMessage());
                    e.printStackTrace();
                    errorLabel.setText("error en la pantalla de medicamwentos: " + e.getMessage());
                }
            } else {
                errorLabel.setText("User o Pass incorrectos");
            }
        }
    }

    @FXML
    private void register() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Todos los campos obligatorios.");
        } else if (User.register(username, password)) {
            errorLabel.setText("Registro exitoso");
        } else {
            errorLabel.setText("Usuario ya creado en la base de dato");
        }
    }
}