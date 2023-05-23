package project.sss;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 500;

    @Override
    public void start(Stage stage) throws IOException {


        Sphere sphere = new Sphere(50);

        sphere.translateXProperty().set(WIDTH/2);
        sphere.translateYProperty().set(HEIGHT/2);

        // ******************************************
        Group g = new Group();
        g.getChildren().add(sphere);

        Camera camera = new PerspectiveCamera();


        // ******************************************
        Scene scene = new Scene(g, WIDTH, HEIGHT);
        scene.setFill(Color.SILVER);

        // Keyboard listener for moving sphere back and forward
        stage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case W: sphere.translateZProperty().set(sphere.getTranslateZ()  + 100);
                    break;
                case S: sphere.translateZProperty().set(sphere.getTranslateZ()  - 100);
                    break;
            }
        });

        scene.setCamera(camera);
        stage.setTitle("Sphere");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}