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
import javafx.geometry.Insets;
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
import javafx.scene.shape.Box;
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
    private final Font font = new Font("Roboto", 10);
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);

    private Camera camera;
    private final Earth earth = new Earth();
    //private final Moon moon = new Moon();
    private final Sun sun = new Sun();

    private Slider zoomSlider;

    @Override
    public void start(Stage stage) throws IOException {
        camera = new PerspectiveCamera(true);
        camera.setNearClip(1);
        camera.setFarClip(10000);
        camera.translateZProperty().set(-1000);




        // Seperate group for background objects (not mouse controllable)
        Group root = new Group();
        root.getChildren().addAll(prepareImages());

        Group system = new Group();
        system.getChildren().addAll(sun.prepareSun());
        system.getChildren().addAll(earth.prepareEarth());

        system.getChildren().add(sun.getPointLight());
        system.getChildren().add(earth.getPointLight());

        //system.getChildren().addAll(moon.prepareMoon());
        System.out.println(system.getChildren());

        AtomicInteger numPressed = new AtomicInteger();

        // TO TURN OFF AND ON AXES DISPLAY
        stage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if(event.getCode() == Q) {
                numPressed.getAndIncrement();
                if(numPressed.get() % 2 != 0) {
                    System.out.println("DEBUGGGG");
                    system.getChildren().addAll(getAxes());
                    AmbientLight ambientLight = new AmbientLight(Color.SILVER);
                    system.getChildren().addAll(ambientLight);
                } else {
                    System.out.println("ELSE STATEMENT");
                    System.out.println(system.getChildren());

                    for (int i = 5; i < system.getChildren().size(); i++) {
                        system.getChildren().remove(i);
                        i--;
                    }
                    system.getChildren().add(sun.getPointLight());
                    system.getChildren().add(earth.getPointLight());

                }
            }
        });

        root.getChildren().add(system);


        Scene scene = new Scene(root, WIDTH, HEIGHT, true);
        scene.setFill(Color.SILVER);

        root.getChildren().addAll(createControls(system, scene));
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


    public Node[] createControls(Group g, Scene scene) {
        zoomSlider = new Slider();
        zoomSlider.setMax(1200);
        zoomSlider.setMin(-200);
        zoomSlider.setValue(50);

        zoomSlider.setPrefWidth(300d);
        zoomSlider.setLayoutX(-150);
        zoomSlider.setLayoutY(210);
        zoomSlider.setShowTickLabels(true);
        zoomSlider.setTranslateZ(5);
        zoomSlider.setStyle("-fx-base: black");
        g.translateZProperty().bind(zoomSlider.valueProperty());

        AtomicBoolean visible = new AtomicBoolean(false);

        Label zoomLabel = new Label("Zoom: " + (int) zoomSlider.getValue());
        Label timeLabel = new Label("Time: 0");
        //zoomLabel.setLayoutX(-450);
        //zoomLabel.setLayoutY(-200);
        zoomLabel.setAlignment(Pos.TOP_LEFT);
        zoomLabel.setTranslateZ(-50);
        zoomLabel.setTextFill(Color.WHITE);
        zoomLabel.setVisible(visible.get());

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10, 12, 10, 12));
        vbox.setSpacing(10);
        vbox.setLayoutX(-WIDTH/4 + 175);
        vbox.setLayoutY(-228);
        //vbox.setAlignment(Pos.TOP_LEFT);
        vbox.setTranslateZ(-50);
        //vbox.setPrefWidth(WIDTH);
        //vbox.setBackground(new Background());
        vbox.setStyle(" -fx-background-image: url(\"E:\\VSCODE PROJECTS\\Solar-System-Simulation\\src\\main\\java\\resources\\galaxy.jpg\");");
        //vbox.setStyle("-fx-background-color: #FFFFFF");

        Label stats = new Label("Stats: off");
        stats.setPrefSize(60, 10);
        stats.setPadding(new Insets(2, 2, 2, 2));
        stats.setStyle("-fx-background-color: #f73131");
        stats.setFont(font);

        CheckBox statShow = new CheckBox();
        statShow.setSelected(false);

        HBox statBox = new HBox(stats, statShow);

        Label pause = new Label("Running");
        pause.setPrefSize(45, 10);
        pause.setPadding(new Insets(2, 2, 2, 2));
        pause.setStyle("-fx-background-color: #35f731");
        pause.setFont(font);

        CheckBox checkPause = new CheckBox();
        checkPause.setSelected(false);

        HBox pauseBox = new HBox(pause, checkPause);


        Label speed = new Label("Speed (1s)");
        speed.setPrefSize(60, 10);
        speed.setPadding(new Insets(2, 2, 2, 2));
        speed.setStyle("-fx-text-fill: #FFFFFF; -fx-border-color: #ffaf38; -fx-border-width: 0.8px");
        speed.setFont(font);

        String[] speeds = {"12hrs", "1d", "1m", "6ms", "1yr", "5 yrs"};
        ChoiceBox selectSpeed = new ChoiceBox(FXCollections.observableArrayList(speeds));
        selectSpeed.setValue(speeds[1]);
        selectSpeed.setLayoutY(-210);
        selectSpeed.setPrefSize(45, 5);
        selectSpeed.setStyle("-fx-background-radius: 0; -fx-font-family: Roboto; -fx-font-size: 10px");


        HBox speedBox = new HBox(speed, selectSpeed);
        speedBox.setPadding(new Insets(2, 2, 2, 0));

        vbox.getChildren().addAll(statBox, pauseBox, speedBox);

        statShow.setOnMouseClicked(event -> {
            if (visible.get()) {
                visible.set(false);
                zoomLabel.setVisible(visible.get());
                stats.setText("Stats: off");
                stats.setStyle("-fx-background-color: #f73131");
            } else {
                visible.set(true);
                zoomLabel.setVisible(visible.get());
                stats.setText("Stats: on");
                stats.setStyle("-fx-background-color: #35f731");
            }
        });

        checkPause.setOnMouseClicked(event -> {
            if (pause.getText().equals("Running")) {
                pause.setText("Paused");
                pause.setPrefSize(48, 10);
                pause.setStyle("-fx-background-color: #f73131");
                earth.pause(true);
                sun.pause(true);
            } else {
                pause.setText("Running");
                pause.setPrefSize(50, 10);
                pause.setStyle("-fx-background-color: #35f731");
                earth.pause(false);
                sun.pause(false);
            }
        });

        zoomSlider.setOnMouseClicked(event -> {
            zoomLabel.setText("Zoom: " + (int) zoomSlider.getValue());
        });
        zoomSlider.setOnMouseDragged(event -> {
            zoomLabel.setText("Zoom: " + (int) zoomSlider.getValue());
        });
        zoomSlider.setOnKeyPressed(event -> {
            zoomLabel.setText("Zoom: " + (int) zoomSlider.getValue());
        });



        timeLabel.setLayoutX(-WIDTH/2);
        timeLabel.setLayoutY(20);

        return new Node[]{zoomSlider, zoomLabel, vbox};
    }


    private ImageView[] prepareImages() {
        Image galaxy = new Image(String.valueOf(new File("E:\\VSCODE PROJECTS\\Solar-System-Simulation\\src\\main\\java\\resources\\galaxy.jpg")));
        ImageView galaxyView = new ImageView(galaxy);
        galaxyView.setPreserveRatio(false);

        galaxyView.getTransforms().add(new Translate(-galaxy.getWidth()/2, -galaxy.getHeight()/2, 6000));

        Image logo = new Image(String.valueOf(new File("E:\\VSCODE PROJECTS\\Solar-System-Simulation\\src\\main\\java\\resources\\logo.png")));
        ImageView logoView = new ImageView(logo);
        logoView.setPreserveRatio(false);
        System.out.println(logoView.getImage().getWidth() + " " + logoView.getImage().getHeight());

        logoView.scaleXProperty().set(0.3);
        logoView.scaleYProperty().set(0.3);
        logoView.setLayoutX(-WIDTH/4 + 5);
        logoView.setLayoutY(-HEIGHT/4 + 43);
        //logoView.getTransforms().add(new Translate(-500, -255, -10));



        return new ImageView[]{galaxyView, logoView};
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
    }
}
