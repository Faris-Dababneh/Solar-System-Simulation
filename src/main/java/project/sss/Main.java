package project.sss;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class Main extends Application {

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 500;

    private double anchorX, anchorY;
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);

    @Override
    public void start(Stage stage) throws IOException {


        Box box = textureBox();



        SmartGroup g = new SmartGroup();
        g.getChildren().add(box);

        g.translateXProperty().set(WIDTH/2);
        g.translateYProperty().set(HEIGHT/2);
        g.translateZProperty().set(-600);



        // ******************************************


        Camera camera = new PerspectiveCamera();


        // ******************************************
        Scene scene = new Scene(g, WIDTH, HEIGHT);
        scene.setFill(Color.SILVER);
        initMouseControl(g, scene, stage);
        // Keyboard listener for moving sphere back and forward
        stage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {

            switch (event.getCode()) {
                case W: g.translateZProperty().set(g.getTranslateZ()  + 100);
                    break;
                case S: g.translateZProperty().set(g.getTranslateZ()  - 100);
                    break;
                /*case Q: g.rotateY(-10);
                    break;
                case E: g.rotateY(10);
                    break;
                case UP: g.rotateX(-10);
                    break;
                case DOWN: g.rotateX(10);
                    break;*/
            }
        });

        scene.setCamera(camera);



        stage.setTitle("Solar System Simulation - By Faris Dababneh");
        stage.setScene(scene);
        stage.show();
    }

    private Box textureBox() {
        // Texture material object
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.ROYALBLUE);

        //Image image = new Image(getClass().getResource());

        material.setDiffuseMap(new Image(String.valueOf(new File("C:\\Users\\882355\\IdeaProjects\\Solar System Simulation\\src\\main\\java\\project\\sss\\vikr.jpg"))));


        Box box = new Box(100, 20, 50);

        box.setMaterial(material);


        return box;
    }

    private void initMouseControl(SmartGroup group, Scene scene, Stage stage) {
        Rotate xRotate;
        Rotate yRotate;
        group.getTransforms().addAll (
                xRotate = new Rotate(0, Rotate.X_AXIS),
                yRotate = new Rotate(0, Rotate.Y_AXIS)
        );
        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);

        scene.setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            anchorAngleX = angleX.get();
            anchorAngleY = angleY.get();
        });

        scene.setOnMouseDragged(event -> {
            angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
            angleY.set(anchorAngleY + anchorX - event.getSceneX());
        });

        stage.addEventHandler(ScrollEvent.SCROLL, event -> {
            double delta = event.getDeltaY();
            group.translateZProperty().set(group.getTranslateZ() + delta);
        });

    }

    public static void main(String[] args) {
        launch();
    }

    class SmartGroup extends Group {
        Rotate r;
        Transform t = new Rotate();

        public void rotateX(int ang) {
            r = new Rotate(ang, Rotate.X_AXIS);
            t = t.createConcatenation(r);
            this.getTransforms().clear();
            this.getTransforms().addAll(t);
        }

        public void rotateY(int ang) {
            r = new Rotate(ang, Rotate.Y_AXIS);
            t = t.createConcatenation(r);
            this.getTransforms().clear();
            this.getTransforms().addAll(t);
        }
    }
}