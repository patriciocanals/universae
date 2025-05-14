package com.universae;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.stage.Stage;
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
    @FXML private TableColumn<Medication, Boolean> takenColumn;
    @FXML private Label errorLabel;

    private ObservableList<Medication> medications = FXCollections.observableArrayList();
    private int userId;

    public void setUserId(int userId) {
        this.userId = userId;
        loadMedications();
    }

    @FXML
    private void initialize() {
        System.out.println("Initializing MedicationsController");
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        doseColumn.setCellValueFactory(cellData -> cellData.getValue().doseProperty());
        scheduleColumn.setCellValueFactory(cellData -> cellData.getValue().scheduleProperty());
        takenColumn.setCellValueFactory(cellData -> cellData.getValue().takenProperty());
        takenColumn.setCellFactory(CheckBoxTableCell.forTableColumn(takenColumn));
        takenColumn.setEditable(true);
        medicationsTable.setEditable(true);
        medicationsTable.setItems(medications);

        takenColumn.setOnEditCommit(event -> {
            Medication med = event.getRowValue();
            med.setTaken(event.getNewValue());
            updateTakenStatus(med);
        });
    }

    private void loadMedications() {
        String sql = "SELECT id, name, dose, schedule, taken FROM medications WHERE user_id = ?";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            medications.clear();
            while (rs.next()) {
                medications.add(new Medication(
                    rs.getString("name"),
                    rs.getString("dose"),
                    rs.getString("schedule"),
                    rs.getBoolean("taken")
                ));
            }
            System.out.println("Loaded " + medications.size() + " medications for userId: " + userId);
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

        String sql = "INSERT INTO medications (user_id, name, dose, schedule, taken) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, name);
            pstmt.setString(3, dose);
            pstmt.setString(4, schedule);
            pstmt.setBoolean(5, false);
            pstmt.executeUpdate();
            medications.add(new Medication(name, dose, schedule, false));
            nameField.clear();
            doseField.clear();
            scheduleField.clear();
            errorLabel.setText("Medicamento añadido");
        } catch (SQLException e) {
            errorLabel.setText("Error: " + e.getMessage());
        }
    }

    private void updateTakenStatus(Medication medication) {
        String sql = "UPDATE medications SET taken = ? WHERE name = ? AND user_id = ?";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, medication.isTaken());
            pstmt.setString(2, medication.getName());
            pstmt.setInt(3, userId);
            pstmt.executeUpdate();
            errorLabel.setText("Estado actualizado");
        } catch (SQLException e) {
            errorLabel.setText("Error al actualizar estado: " + e.getMessage());
        }
    }

    @FXML
    private void logout() {
        try {
            System.out.println("Logging out, loading Login.fxml");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/universae/Login.fxml"));
            Scene scene = new Scene(loader.load(), 300, 400);
            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.setTitle("MediTrack - Iniciar Sesión");
            stage.setScene(scene);
            System.out.println("Login screen loaded successfully");
        } catch (Exception e) {
            System.out.println("Error loading Login.fxml: " + e.getMessage());
            errorLabel.setText("Error al cerrar sesión: " + e.getMessage());
        }
    }
}