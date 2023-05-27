/*
--------------------------------------------------
SOLAR SYSTEM SIMULATION
--------------------------------------------------

IMPORTANT VALUES:

CONVERSIONS:
0.1 CHANGE PER FRAME = 1,037 MPH    (FOR ANIMATION TIMER)




EARTH'S ROTATION SPEED: ~1,037 MPH
 */

package project.sss;

import Testing.ComplexShapes;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.*;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class EarthSimulation extends Application {

    private static final float WIDTH = 1400;
    private static final float HEIGHT = 1000;

    private double anchorX, anchorY;
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);

    private final Sphere earth = new Sphere(150);
    @Override
    public void start(Stage stage) throws IOException {
        Camera camera = new PerspectiveCamera(true);
        camera.setNearClip(1);
        camera.setFarClip(10000);
        camera.translateZProperty().set(-1000);

        SmartGroup system = new SmartGroup();
        system.getChildren().add(prepareEarth());

        Slider zoomSlider = zoomSlider();
        system.translateZProperty().bind(zoomSlider.valueProperty());

        // Seperate group for background objects (not mouse controllable)
        Group root = new Group();
        root.getChildren().add(system);
        root.getChildren().add(prepareImages());
        root.getChildren().add(zoomSlider);


        Scene scene = new Scene(root, WIDTH, HEIGHT, true);
        scene.setFill(Color.SILVER);


        // CAN MOVE THE SIMULATION AROUND
        initMouseControl(system, scene, stage);


        scene.setCamera(camera);
        stage.setTitle("Solar System Simulation - By Faris Dababneh");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();

        prepareSpin();
    }

    private void prepareSpin() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                earth.rotateProperty().set(earth.getRotate() + 0.1);
            }
        };
        timer.start();
    }

    private ImageView prepareImages() {
        Image galaxy = new Image(String.valueOf(new File("E:\\VSCODE PROJECTS\\Solar-System-Simulation\\src\\main\\java\\resources\\galaxy.jpg")));
        ImageView galaxyView = new ImageView(galaxy);
        galaxyView.setPreserveRatio(false);

        galaxyView.getTransforms().add(new Translate(-galaxy.getWidth()/2, -galaxy.getHeight()/2, 6000));

        return galaxyView;
    }

    private Node prepareEarth() {
        PhongMaterial earthMaterial = new PhongMaterial();
        earthMaterial.setDiffuseMap(new Image(String.valueOf(new File("E:\\VSCODE PROJECTS\\Solar-System-Simulation\\src\\main\\java\\resources\\earth-map.jpg"))));
        earthMaterial.setSelfIlluminationMap(new Image(String.valueOf(new File("E:\\VSCODE PROJECTS\\Solar-System-Simulation\\src\\main\\java\\resources\\earth-illumination.jpg"))));
        earthMaterial.setSpecularMap(new Image(String.valueOf(new File("E:\\VSCODE PROJECTS\\Solar-System-Simulation\\src\\main\\java\\resources\\earth-specular.tif"))));
        earthMaterial.setBumpMap(new Image(String.valueOf(new File("E:\\VSCODE PROJECTS\\Solar-System-Simulation\\src\\main\\java\\resources\\earth-bump.tif"))));

        earth.setRotationAxis(Rotate.Y_AXIS);
        earth.setMaterial(earthMaterial);
        return earth;
    }

    private Slider zoomSlider() {
        Slider zoom = new Slider();
        zoom.setMax(800);
        zoom.setMin(-200);
        zoom.setPrefWidth(300d);
        zoom.setLayoutX(-150);
        zoom.setLayoutY(200);
        zoom.setShowTickLabels(true);
        zoom.setTranslateZ(5);
        zoom.setStyle("-fx-base: black");

        return zoom;
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

        /*
       CODE FOR ZOOM CONTROL USING SCROLL WHEEL
       REMOVED BECAUSE SLIDER IS USED FOR THIS PURPOSE

        stage.addEventHandler(ScrollEvent.SCROLL, event -> {
            double delta = event.getDeltaY();
            group.translateZProperty().set(group.getTranslateZ() + delta);
        });*/

    }
}