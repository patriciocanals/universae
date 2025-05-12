package com.universae;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.Connection;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        Connection conn = Database.connect();
        String dbMessage = (conn != null) ? "Base de datos conectada" : "Error en la base de datos";

        Label label = new Label("Bienvenido a MediTrack\n" + dbMessage);
        Button button = new Button("Iniciar");
        button.setOnAction(e -> label.setText("¡Botón pulsado!"));

        VBox root = new VBox(10, label, button);
        Scene scene = new Scene(root, 300, 200);

        primaryStage.setTitle("MediTrack - Gestión de Medicamentos");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}