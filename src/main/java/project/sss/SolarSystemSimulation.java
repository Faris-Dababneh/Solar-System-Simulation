/*
--------------------------------------------------
SOLAR SYSTEM SIMULATION
--------------------------------------------------

IMPORTANT VALUES:





EARTH:
    ROTATION SPEED: ~1,037 MPH
    TILT: 23.5 DEGREES
    RADIUS: 3,958.8 mi

    DISTANCE FROM SUN: 94.227 million mi
    DISTANCE FROM MOON: 238,900 mi

MOON:
    RADIUS: 1,079.6 mi

SUN:
    ROTATION SPEED: ~448,000 MPH
    TILT: 7.25 DEGREES
    RADIUS: 432,690 mi

 */

package project.sss;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static javafx.scene.input.KeyCode.*;

public class SolarSystemSimulation extends Application {

    private static Rectangle2D screenBounds = Screen.getPrimary().getBounds();
    private static final double WIDTH = screenBounds.getWidth();
    private static final double HEIGHT = screenBounds.getHeight();

    private double anchorX, anchorY;
    private double anchorPosX, anchorPosY;

    private final DoubleProperty posX = new SimpleDoubleProperty(0);
    private final DoubleProperty posY = new SimpleDoubleProperty(0);

    private final Font font = new Font("Roboto", 10);
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);

    private static Camera camera;
    private static final Earth earth = new Earth();

    private static final Sun sun = new Sun();

    private static final Mercury mercury = new Mercury();


    private CheckBox lightToggle = new CheckBox();

    private Group system = new Group();

    public static Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera c) {
        camera = c;
    }

    public static Earth getEarth() {
        return earth;
    }

    public static Sun getSun() {
        return sun;
    }

    private static final int numObjects = 6;

    @Override
    public void start(Stage stage) throws IOException {
        camera = new PerspectiveCamera(true);
        camera.setNearClip(1);
        camera.setFarClip(10000);
        camera.translateZProperty().set(-1000);


        // Seperate group for background objects (not mouse controllable)
        Group root = new Group();
        root.getChildren().addAll(prepareImages());


        system.getChildren().addAll(sun.prepareSun());
        system.getChildren().addAll(earth.prepareEarth());
        system.getChildren().addAll(mercury.prepareMercury());


        system.getChildren().add(sun.getPointLight());
        system.getChildren().add(earth.getPointLight());

        //system.getChildren().addAll(moon.prepareMoon());
        System.out.println(system.getChildren());

        AtomicInteger numPressed = new AtomicInteger();

        // TO TURN OFF AND ON AXES DISPLAY
        stage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == Q) {
                numPressed.getAndIncrement();
                if (numPressed.get() % 2 != 0 && !lightToggle.isSelected()) {
                    system.getChildren().addAll(getAxes());
                    AmbientLight ambientLight = new AmbientLight(Color.SILVER);
                    system.getChildren().addAll(ambientLight);
                    lightToggle.setSelected(true);
                } else {
                    System.out.println(system.getChildren());
                    for (int i = numObjects; i < system.getChildren().size(); i++) {
                        system.getChildren().remove(i);
                        i--;
                    }
                    system.getChildren().add(sun.getPointLight());
                    system.getChildren().add(earth.getPointLight());
                    lightToggle.setSelected(false);

                }
            }
        });

        root.getChildren().add(system);


        Scene scene = new Scene(root, WIDTH, HEIGHT, true);


        root.getChildren().addAll(UI.createControls(system, earth, sun, mercury));
        //root.getChildren().addAll(controls(system));

        // CAN MOVE THE SIMULATION AROUND
        initMouseControl(system, scene);

        scene.setCamera(camera);
        stage.setTitle("Solar System Simulation - By Faris Dababneh");
        stage.setScene(scene);
        //stage.setMaximized(true);

        System.out.println(WIDTH + " " + HEIGHT);
        stage.show();

    }

    public Node[] getAxes() {
        PhongMaterial material1 = new PhongMaterial();
        material1.setDiffuseColor(Color.RED);

        Box x = new Box(1000, 5, 5);
        x.setMaterial(material1);

        PhongMaterial material2 = new PhongMaterial();
        material2.setDiffuseColor(Color.GREEN);
        Box y = new Box(5, 5, 1000);
        y.setMaterial(material2);

        PhongMaterial material3 = new PhongMaterial();
        material3.setDiffuseColor(Color.BLUE);
        Box z = new Box(5, 1000, 5);
        z.setMaterial(material3);
        return new Node[]{x, y, z};
    }




    private ImageView[] prepareImages() {
        Image galaxy = new Image(String.valueOf(new File("C:\\Users\\882355\\IdeaProjects\\Solar System Simulation\\src\\main\\java\\resources\\galaxy.jpg")));
        ImageView galaxyView = new ImageView(galaxy);
        galaxyView.setPreserveRatio(false);

        galaxyView.getTransforms().add(new Translate(-galaxy.getWidth() / 2, -galaxy.getHeight() / 2, 6000));

        Image logo = new Image(String.valueOf(new File("C:\\Users\\882355\\IdeaProjects\\Solar System Simulation\\src\\main\\java\\resources\\logo.png")));
        ImageView logoView = new ImageView(logo);
        logoView.setPreserveRatio(false);
        System.out.println(logoView.getImage().getWidth() + " " + logoView.getImage().getHeight());

        logoView.scaleXProperty().set(0.5);
        logoView.scaleYProperty().set(0.5);
        logoView.setLayoutX(-240);
        logoView.setLayoutY(-HEIGHT / 2 + 80);
        //logoView.getTransforms().add(new Translate(-500, -255, -10));


        return new ImageView[]{galaxyView, logoView};
    }

    public static void main(String[] args) {
        launch();
    }

    private void initMouseControl(Group group, Scene scene) {
        Rotate xRotate;
        Rotate yRotate;
        group.getTransforms().addAll(
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
    }

}
