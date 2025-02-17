package com.example.trial;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 915, 700);
        stage.setTitle("Toy Language Interpreter");
        stage.setScene(scene);

        // Ensures the application exits completely when the window is closed
        stage.setOnCloseRequest(event -> {
            System.out.println("Closing application...");
            System.exit(0);
        });

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

