package project.sss;

import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;

import java.util.concurrent.atomic.AtomicBoolean;



public class UI {
    private static Rectangle2D screenBounds = Screen.getPrimary().getBounds();
    private static final double WIDTH = screenBounds.getWidth();
    private static final double HEIGHT = screenBounds.getHeight();

    // FOR 4K RESOLUTION SCREENS
    private static final int TOPLEFTX = -460;
    private static final int TOPY = -260;

    private static final SolarSystemSimulation sim = new SolarSystemSimulation();
    private static Slider zoomSlider;
    private static Slider shiftSlider;
    private static CheckBox lightToggle = new CheckBox();
    private static final Font font = new Font("Roboto", 10);
    private static final int numObjects = 12;
    private static final Planet[] planets = new Planet[5];
    private static AnimationTimer timer;
    private static long time;
    private static double time2;
    private static long start = System.currentTimeMillis();
    private static long anchor = System.currentTimeMillis();
    public static Node[] createControls(Group g, Earth earth, Sun sun, Mercury mercury, Venus venus, Planet mars, Planet jupiter, Planet saturn, Planet uranus, Planet neptune) {
        Planet[] planets = {mars, jupiter, saturn, uranus, neptune};
        zoomSlider = new Slider();
        zoomSlider.setOrientation(Orientation.VERTICAL);
        zoomSlider.setMax(3500);
        zoomSlider.setMin(-200);
        zoomSlider.setValue(1500);

        zoomSlider.setPrefHeight(300d);
        zoomSlider.setLayoutX(420);
        zoomSlider.setLayoutY(-150);
        zoomSlider.setShowTickLabels(true);
        zoomSlider.setTranslateZ(5);
        zoomSlider.setStyle("-fx-base: black");
        g.translateZProperty().bind(zoomSlider.valueProperty());

        shiftSlider = new Slider();
        shiftSlider.setOrientation(Orientation.HORIZONTAL);
        shiftSlider.setMax(2000);
        shiftSlider.setMin(-2000);
        shiftSlider.setValue(0);
        shiftSlider.setPrefWidth(300d);
        shiftSlider.setShowTickLabels(true);
        shiftSlider.setTranslateZ(5);
        shiftSlider.setLayoutX(-150);
        shiftSlider.setLayoutY(200);
        shiftSlider.setStyle("-fx-base: black");
        g.translateXProperty().bind(shiftSlider.valueProperty());

        AtomicBoolean visible = new AtomicBoolean(false);

        Label zoomLabel = new Label("Zoom: " + (int) zoomSlider.getValue());

        //zoomLabel.setLayoutX(-450);
        //zoomLabel.setLayoutY(-200);
        zoomLabel.setLayoutX(-448);
        zoomLabel.setLayoutY(180);
        zoomLabel.setTranslateZ(-50);
        zoomLabel.setTextFill(Color.WHITE);
        zoomLabel.setVisible(visible.get());
        zoomLabel.setFont(font);

        Label shiftLabel = new Label("Shift: " + (int) shiftSlider.getValue());
        shiftLabel.setLayoutX(-448);
        shiftLabel.setLayoutY(192);
        shiftLabel.setTranslateZ(-50);
        shiftLabel.setTextFill(Color.BLUE);
        shiftLabel.setVisible(visible.get());
        shiftLabel.setFont(font);

        Label timeLabel = new Label("Duration: 0s");
        timeLabel.setLayoutX(-448);
        timeLabel.setLayoutY(204);
        timeLabel.setTranslateZ(-50);
        timeLabel.setTextFill(Color.RED);
        timeLabel.setVisible(visible.get());
        timeLabel.setFont(font);

        timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                long end = System.currentTimeMillis();
                time = (end - start) / 1000;
                timeLabel.setText("Duration: " + time + "s");
            }
        };
        timer.start();

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10, 12, 10, 12));
        //vbox.setPadding(new Insets(0, 12, 10, 12));
        vbox.setSpacing(5);
        vbox.setLayoutX(TOPLEFTX);
        vbox.setLayoutY(TOPY);
        //vbox.setAlignment(Pos.TOP_LEFT);
        vbox.setTranslateZ(-50);
        //vbox.setPrefWidth(WIDTH);
        //vbox.setBackground(new Background());
        vbox.setStyle("-fx-background-image: url(\"C:\\Users\\882355\\IdeaProjects\\Solar System Simulation\\src\\main\\java\\resources\\galaxy.jpg\")");
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

        Label speed = new Label("Speed");
        speed.setPrefSize(45, 10);
        speed.setPadding(new Insets(2, 2, 2, 2));
        speed.setStyle("-fx-text-fill: #FFFFFF; -fx-border-color: #ffaf38; -fx-border-width: 0.8px");
        speed.setFont(font);

        String[] speeds = {".25", ".5", "1", "1.5", "2", "5"};
        ChoiceBox selectSpeed = new ChoiceBox(FXCollections.observableArrayList(speeds));
        selectSpeed.setValue(speeds[2]);
        //selectSpeed.setLayoutY(TOPY);
        selectSpeed.setPrefSize(45, 5);
        selectSpeed.setStyle("-fx-background-radius: 0; -fx-font-family: Roboto; -fx-font-size: 10px");

        HBox speedBox = new HBox(speed, selectSpeed);
        speedBox.setPadding(new Insets(2, 2, 2, 0));

        Label light = new Label("Light (q)");
        light.setPrefSize(45, 10);
        light.setPadding(new Insets(2, 2, 2, 2));
        light.setStyle("-fx-text-fill: #FFFFFF; -fx-border-color: #0d21fc; -fx-border-width: 0.8px");
        light.setFont(font);


        lightToggle.setSelected(false);

        HBox focusBox = new HBox(light, lightToggle);
        speedBox.setPadding(new Insets(2, 2, 2, 0));


        vbox.getChildren().addAll(statBox, pauseBox, speedBox, focusBox);

        statShow.setOnMouseClicked(event -> {
            if (visible.get()) {
                visible.set(false);
                zoomLabel.setVisible(visible.get());
                timeLabel.setVisible(visible.get());
                shiftLabel.setVisible(visible.get());
                stats.setText("Stats: off");
                stats.setStyle("-fx-background-color: #f73131");
            } else {
                visible.set(true);
                zoomLabel.setVisible(visible.get());
                timeLabel.setVisible(visible.get());
                shiftLabel.setVisible(visible.get());
                stats.setText("Stats: on");
                stats.setStyle("-fx-background-color: #35f731");
            }
        });

        selectSpeed.setOnAction(event -> {
            Object value = selectSpeed.getValue();
            sun.speed(Double.parseDouble(value.toString()));
            mercury.speed(Double.parseDouble(value.toString()));
            venus.speed(Double.parseDouble(value.toString()));
            earth.speed(Double.parseDouble(value.toString()));
            for (int i = 0; i < planets.length; i++) {
                planets[i].speed(Double.parseDouble(value.toString()));
            }
            timer.stop();
            double start1 = System.currentTimeMillis();

            time = (long) time2;
            timer = new AnimationTimer() {
                @Override
                public void handle(long l) {
                    double end = System.currentTimeMillis();

                    time2 = time + (((end - start1) / 1000) * Double.parseDouble(value.toString()));

                    int display = (int) time2;
                    timeLabel.setText("Duration: " + display + "s");
                }
            };
            timer.start();
        });

        lightToggle.setOnMouseClicked(event -> {
            if(lightToggle.isSelected()) {
                g.getChildren().addAll(sim.getAxes());
                AmbientLight ambientLight = new AmbientLight(Color.SILVER);
                g.getChildren().addAll(ambientLight);
            } else {
                for (int i = numObjects; i < g.getChildren().size(); i++) {
                    g.getChildren().remove(i);
                    i--;
                }
                g.getChildren().add(sun.getPointLight());
                g.getChildren().add(earth.getPointLight());
            }
        });

        checkPause.setOnMouseClicked(event -> {
            if (pause.getText().equals("Running")) {
                pause.setText("Paused");
                pause.setPrefSize(48, 10);
                pause.setStyle("-fx-background-color: #f73131");
                earth.pause(true);
                sun.pause(true);
                mercury.pause(true);
                venus.pause(true);
                for (int i = 0; i < planets.length; i++) {
                    planets[i].pause(true);
                }
                timer.stop();
                anchor = time;
            } else {
                pause.setText("Running");
                pause.setPrefSize(50, 10);
                pause.setStyle("-fx-background-color: #35f731");
                earth.pause(false);
                sun.pause(false);
                mercury.pause(false);
                venus.pause(false);
                for (int i = 0; i < planets.length; i++) {
                    planets[i].pause(false);
                }
                timer.start();
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
        // ***********************************************************
        shiftSlider.setOnMouseClicked(event -> {
            shiftLabel.setText("Shift: " + (int) shiftSlider.getValue());
        });
        shiftSlider.setOnMouseDragged(event -> {
            shiftLabel.setText("Shift: " + (int) shiftSlider.getValue());
        });
        shiftSlider.setOnKeyPressed(event -> {
            shiftLabel.setText("Shift: " + (int) shiftSlider.getValue());
        });

        Button reset = new Button("Reset");
        reset.setStyle("-fx-background-color: #fae01b; -fx-border-color: #000000");
        reset.setPrefSize(45, 8);
        reset.setLayoutX(420);
        reset.setLayoutY(-260);
        reset.translateZProperty().set(5);
        reset.setFont(font);

        reset.setOnMouseClicked(event -> {
            zoomSlider.setValue(1000);
            shiftSlider.setValue(0);

            selectSpeed.setValue(speeds[2]);
            earth.speed(1);
            sun.speed(1);
            mercury.speed(1);
            venus.speed(1);
            for (int i = 0; i < planets.length; i++) {
                planets[i].speed(1);
            }
        });



        return new Node[]{zoomSlider, shiftSlider, zoomLabel, timeLabel, shiftLabel, vbox, reset};
    }
}