package com.universae;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML
    private void login() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Por favor, completa todos los campos");
        } else {
            int userId = User.login(username, password);
            if (userId > 0) {
                try {
                    System.out.println("Attempting to load Medications.fxml");
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/universae/Medications.fxml"));
                    if (loader.getLocation() == null) {
                        System.out.println("Error: Medications.fxml not found");
                        errorLabel.setText("Error: No se encontró la pantalla de medicamentos");
                        return;
                    }
                    Scene scene = new Scene(loader.load(), 600, 400);
                    MedicationsController controller = loader.getController();
                    controller.setUserId(userId);
                    Stage stage = (Stage) usernameField.getScene().getWindow();
                    stage.setTitle("MediTrack - Gestión de Medicamentos");
                    stage.setScene(scene);
                    System.out.println("Medications screen loaded successfully");
                } catch (Exception e) {
                    System.out.println("Error loading Medications.fxml: " + e.getMessage());
                    e.printStackTrace();
                    errorLabel.setText("Error al cargar la pantalla de medicamentos: " + e.getMessage());
                }
            } else {
                errorLabel.setText("Usuario o contraseña incorrectos");
            }
        }
    }

    @FXML
    private void register() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Por favor, completa todos los campos");
        } else if (User.register(username, password)) {
            errorLabel.setText("Registro exitoso");
        } else {
            errorLabel.setText("Error: usuario ya existe o fallo en la base de datos");
        }
    }
}