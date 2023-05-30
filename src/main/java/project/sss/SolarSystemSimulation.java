/*
--------------------------------------------------
SOLAR SYSTEM SIMULATION
--------------------------------------------------

IMPORTANT VALUES:

CONVERSIONS:



EARTH:
    ROTATION SPEED: ~1,037 MPH
    TILT: 23.5 DEGREES

SUN:
    ROTATION SPEED: ~448,000 MPH
    TILT: 7.25 DEGREES

 */

package project.sss;

import Testing.EarthSimulation;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class SolarSystemSimulation extends Application {

    private static final float WIDTH = 1400;
    private static final float HEIGHT = 1000;

    private double anchorX, anchorY;
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);

    private final Earth earth = new Earth();
    private final Sun sun = new Sun();


    private Slider zoomSlider;

    public float getStageWidth() {
        return WIDTH;
    }
    public float getStageHeight() {
        return HEIGHT;
    }
    @Override
    public void start(Stage stage) throws IOException {
        Camera camera = new PerspectiveCamera(true);
        camera.setNearClip(1);
        camera.setFarClip(10000);
        camera.translateZProperty().set(-1000);


        // Seperate group for background objects (not mouse controllable)
        Group root = new Group();
        root.getChildren().add(prepareImages());

        //zoomSlider = getZoomSlider();

        //root.getChildren().addAll(getLabels());

        Group system = new Group();
        system.getChildren().addAll(sun.prepareSun());
        system.getChildren().addAll(earth.prepareEarth());
        root.getChildren().add(system);


        root.getChildren().addAll(createControls(system));

        Scene scene = new Scene(root, WIDTH, HEIGHT, true);
        scene.setFill(Color.SILVER);


        // CAN MOVE THE SIMULATION AROUND
        initMouseControl(system, scene);


        scene.setCamera(camera);
        stage.setTitle("Solar System Simulation - By Faris Dababneh");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();

        prepareSpin();
    }

    public Node[] createControls(Group g) {
        zoomSlider = new Slider();
        zoomSlider.setMax(1000);
        zoomSlider.setMin(-200);
        zoomSlider.setValue(50);

        zoomSlider.setPrefWidth(300d);
        zoomSlider.setLayoutX(-150);
        zoomSlider.setLayoutY(210);
        zoomSlider.setShowTickLabels(true);
        zoomSlider.setTranslateZ(5);
        zoomSlider.setStyle("-fx-base: black");
        g.translateZProperty().bind(zoomSlider.valueProperty());


        Label zoomLabel = new Label("zoomSlider: " + zoomSlider.getValue());
        Label timeLabel = new Label("Time: 0");
        zoomLabel.setLayoutX(-WIDTH/2);
        zoomLabel.setLayoutY(0);
        zoomLabel.setTranslateZ(-50);


        timeLabel.setLayoutX(-WIDTH/2);
        timeLabel.setLayoutY(20);

        return new Node[]{zoomSlider, zoomLabel, timeLabel};
    }

    public SubScene getScene3D(Group group) {
        SubScene scene3d = new SubScene(group, WIDTH, HEIGHT, true, SceneAntialiasing.BALANCED);

        scene3d.setCamera(new PerspectiveCamera());
        return scene3d;
    }

    private void prepareSpin() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                earth.getEarth().rotateProperty().set(earth.getEarth().getRotate() - 0.25);
                sun.getSun().rotateProperty().set(sun.getSun().getRotate() - 1.5);
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


    public static void main(String[] args) {
        launch();
    }



    private void initMouseControl(Group group, Scene scene) {
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
       CODE FOR zoomSlider CONTROL USING SCROLL WHEEL
       REMOVED BECAUSE SLIDER IS USED FOR THIS PURPOSE

        stage.addEventHandler(ScrollEvent.SCROLL, event -> {
            double delta = event.getDeltaY();
            group.translateZProperty().set(group.getTranslateZ() + delta);
        });*/

    }
}