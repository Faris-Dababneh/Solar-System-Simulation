package project.sss;

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

    private static final SolarSystemSimulation sim = new SolarSystemSimulation();
    private static Slider zoomSlider;
    private static Slider shiftSlider;
    private static CheckBox lightToggle = new CheckBox();
    private static final Font font = new Font("Roboto", 10);
    private static final int numObjects = 6;
    public static Node[] createControls(Group g, Earth earth, Sun sun, Mercury mercury) {
        zoomSlider = new Slider();
        zoomSlider.setOrientation(Orientation.VERTICAL);
        zoomSlider.setMax(1500);
        zoomSlider.setMin(-200);
        zoomSlider.setValue(1000);

        zoomSlider.setPrefHeight(300d);
        zoomSlider.setLayoutX(420);
        zoomSlider.setLayoutY(-150);
        zoomSlider.setShowTickLabels(true);
        zoomSlider.setTranslateZ(5);
        zoomSlider.setStyle("-fx-base: black");
        g.translateZProperty().bind(zoomSlider.valueProperty());

        shiftSlider = new Slider();
        shiftSlider.setOrientation(Orientation.HORIZONTAL);
        shiftSlider.setMax(500);
        shiftSlider.setMin(-500);
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
        Label timeLabel = new Label("Time: 0");
        //zoomLabel.setLayoutX(-450);
        //zoomLabel.setLayoutY(-200);
        zoomLabel.setAlignment(Pos.TOP_LEFT);
        zoomLabel.setTranslateZ(-50);
        zoomLabel.setTextFill(Color.WHITE);
        zoomLabel.setVisible(visible.get());

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10, 12, 10, 12));
        //vbox.setPadding(new Insets(0, 12, 10, 12));
        vbox.setSpacing(5);
        vbox.setLayoutX(-WIDTH / 2 + 220);
        vbox.setLayoutY(-HEIGHT / 2 + 125);
        //vbox.setAlignment(Pos.TOP_LEFT);
        vbox.setTranslateZ(-50);
        //vbox.setPrefWidth(WIDTH);
        //vbox.setBackground(new Background());
        vbox.setStyle(" -fx-background-image: url(\"C:\\Users\\882355\\IdeaProjects\\Solar System Simulation\\src\\main\\java\\resources\\galaxy.jpg\");");
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

        String[] speeds = {".25x", ".5x", "1x", "1.5x", "2x", "5x"};
        ChoiceBox selectSpeed = new ChoiceBox(FXCollections.observableArrayList(speeds));
        selectSpeed.setValue(speeds[2]);
        selectSpeed.setLayoutY(-210);
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
                stats.setText("Stats: off");
                stats.setStyle("-fx-background-color: #f73131");
            } else {
                visible.set(true);
                zoomLabel.setVisible(visible.get());
                stats.setText("Stats: on");
                stats.setStyle("-fx-background-color: #35f731");
            }
        });

        selectSpeed.setOnAction(event -> {
            Object value = selectSpeed.getValue();
            if (value.equals(speeds[0])) {
                earth.speed(.25);
                sun.speed(.25);
                mercury.speed(.25);
            } else if (value.equals(speeds[1])) {
                earth.speed(0.5);
                sun.speed(.5);
            } else if (value.equals(speeds[2])) {
                earth.speed(1);
                sun.speed(1);
            } else if (value.equals(speeds[3])) {
                earth.speed(1.5);
                sun.speed(1.5);
            } else if (value.equals(speeds[4])) {
                earth.speed(2);
                sun.speed(2);
            } else if (value.equals(speeds[5])) {
                earth.speed(5);
                sun.speed(5);
            }
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
        });

        timeLabel.setLayoutX(-WIDTH / 2);
        timeLabel.setLayoutY(20);

        return new Node[]{zoomSlider, shiftSlider, zoomLabel, vbox, reset};
    }
}
