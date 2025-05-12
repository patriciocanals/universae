package com.universae;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        Label label = new Label("Bienvenido a MediTrack");
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