/*
CODE FOR FULL EARTH SIMULATION, STEP 1 OF THE SOLAR SYSTEM
 */

package project.sss;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.*;
import javafx.scene.image.Image;
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

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;

    private double anchorX, anchorY;
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);

    @Override
    public void start(Stage stage) throws IOException {


        Box box = prepareBox();

        SmartGroup g = new SmartGroup();
        g.getChildren().add(box);
        g.getChildren().add(prepareBoxTwo());
        g.getChildren().addAll(prepareLightSource());
        g.getChildren().add(new AmbientLight());



        g.translateXProperty().set(WIDTH/2);
        g.translateYProperty().set(HEIGHT/2);
        g.translateZProperty().set(-600);



        // ******************************************


        Camera camera = new PerspectiveCamera();
        camera.translateZProperty().set(-200);


        // ******************************************
        Scene scene = new Scene(g, WIDTH, HEIGHT, true);

        scene.setFill(Color.SILVER);
        initMouseControl(g, scene, stage);
        // Keyboard listener for moving sphere back and forward
        stage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case W: g.translateZProperty().set(g.getTranslateZ()  + 100);
                    break;
                case S: g.translateZProperty().set(g.getTranslateZ()  - 100);
                    break;
            }
        });

        scene.setCamera(camera);

        stage.setTitle("Solar System Simulation - By Faris Dababneh");
        stage.setScene(scene);
        stage.show();

        // ANIMATION FOR ROTATION
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                pointLight.setRotate(pointLight.getRotate() + 1);
            }
        };
        timer.start();
    }

    private Node prepareBoxTwo() {
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseMap(new Image(String.valueOf(new File("E:\\VSCODE PROJECTS\\Solar-System-Simulation\\src\\main\\java\\resources\\Stylized_Stone_Floor_005_basecolor.jpg"))));
        //material.setBumpMap(new Image(String.valueOf(new File("E:\\VSCODE PROJECTS\\Solar-System-Simulation\\src\\main\\java\\resources\\grunge.jpg"))));

        Box box = new Box(100, 200, 100);
        box.setMaterial(material);
        return box;
    }

    private final PointLight pointLight = new PointLight();
    private Node[] prepareLightSource() {

        // Ambient light gives everything the same light amount
//        AmbientLight ambientLight = new AmbientLight();
//        ambientLight.setColor(Color.AQUA);
//        return ambientLight;

        pointLight.setColor(Color.RED);
        pointLight.getTransforms().add(new Translate(0, -200, 150));
        // For the rotation animation
        pointLight.setRotationAxis(Rotate.X_AXIS);

        // TO REPRESENT THE POINT LIGHT
        Sphere sphere = new Sphere(15);
        // Position of point light
        sphere.getTransforms().setAll(pointLight.getTransforms());
        sphere.rotateProperty().bind(pointLight.rotateProperty());
        sphere.rotationAxisProperty().bind(pointLight.rotationAxisProperty());


        return new Node[]{pointLight, sphere};
    }

    private Box prepareBox() {
        // Texture material object
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseMap(new Image(String.valueOf(new File("E:\\VSCODE PROJECTS\\Solar-System-Simulation\\src\\main\\java\\resources\\Stylized_Stone_Floor_005_basecolor.jpg"))));
        //material.setBumpMap(new Image(String.valueOf(new File("E:\\VSCODE PROJECTS\\Solar-System-Simulation\\src\\main\\java\\resources\\grunge.jpg"))));

        Box box = new Box(400, 80, 200);
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