package com.universae;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MedicationsController {
    @FXML private TextField nameField;
    @FXML private TextField doseField;
    @FXML private TextField scheduleField;
    @FXML private TableView<Medication> medicationsTable;
    @FXML private TableColumn<Medication, String> nameColumn;
    @FXML private TableColumn<Medication, String> doseColumn;
    @FXML private TableColumn<Medication, String> scheduleColumn;
    @FXML private Label errorLabel;

    private ObservableList<Medication> medications = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        System.out.println("Initializing MedicationsController");
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        doseColumn.setCellValueFactory(cellData -> cellData.getValue().doseProperty());
        scheduleColumn.setCellValueFactory(cellData -> cellData.getValue().scheduleProperty());
        medicationsTable.setItems(medications);
        loadMedications();
    }

    private void loadMedications() {
        String sql = "SELECT name, dose, schedule FROM medications";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            medications.clear();
            while (rs.next()) {
                medications.add(new Medication(
                    rs.getString("name"),
                    rs.getString("dose"),
                    rs.getString("schedule")
                ));
            }
            System.out.println("Loaded " + medications.size() + " medications");
        } catch (SQLException e) {
            errorLabel.setText("Error al cargar medicamentos: " + e.getMessage());
            System.out.println("Error loading medications: " + e.getMessage());
        }
    }

    @FXML
    private void addMedication() {
        String name = nameField.getText();
        String dose = doseField.getText();
        String schedule = scheduleField.getText();
        if (name.isEmpty() || dose.isEmpty() || schedule.isEmpty()) {
            errorLabel.setText("Por favor, completa todos los campos");
            return;
        }

        String sql = "INSERT INTO medications (name, dose, schedule, taken) VALUES (?, ?, ?, ?)";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, dose);
            pstmt.setString(3, schedule);
            pstmt.setBoolean(4, false);
            pstmt.executeUpdate();
            medications.add(new Medication(name, dose, schedule));
            nameField.clear();
            doseField.clear();
            scheduleField.clear();
            errorLabel.setText("Medicamento añadido");
        } catch (SQLException e) {
            errorLabel.setText("Error: " + e.getMessage());
        }
    }
}