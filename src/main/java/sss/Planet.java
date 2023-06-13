package project.sss;

import javafx.scene.Node;
import javafx.animation.AnimationTimer;
import javafx.geometry.Point3D;
import javafx.scene.image.Image;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;


import java.io.File;
public class Planet {
    private final Sphere planet;
    private final double[] original = new double[2];
    private final double[] rates = new double[2];
    private final PhongMaterial planetMaterial = new PhongMaterial();
    private final String name;

    private final int distance;
    private AnimationTimer timer;
    private Image diffuseMap;
    private Image illuminationMap;

    public Planet(int radius, int distance, double spin, double orbit, String name) {
        planet = new Sphere(radius);
        this.distance = distance;
        original[0] = spin;
        rates[0] = spin;
        original[1] = orbit;
        rates[1] = orbit;
        this.name = name;
    }

    public void speed(double multiplier) {
        for (int i = 0; i < original.length; i++) {
            rates[i] = original[i] * multiplier;
        }
    }
    public void pause(boolean b) {
        if (b) {
            timer.stop();
        } else {
            timer.start();
        }
    }
    public Node[] prepareplanet(){
        switch (name) {
            case "mars" -> {
                diffuseMap = new Image(String.valueOf(new File("E:\\VSCODE PROJECTS\\Solar-System-Simulation\\src\\main\\java\\resources\\mars-map.JPG")));
                illuminationMap = new Image(String.valueOf(new File("E:\\VSCODE PROJECTS\\Solar-System-Simulation\\src\\main\\java\\resources\\mars-illumination.jpg")));
            }
            case "jupiter" -> {
                diffuseMap = new Image(String.valueOf(new File("E:\\VSCODE PROJECTS\\Solar-System-Simulation\\src\\main\\java\\resources\\jupiter-map.JPG")));
                illuminationMap = new Image(String.valueOf(new File("E:\\VSCODE PROJECTS\\Solar-System-Simulation\\src\\main\\java\\resources\\jupiter-illumination.jpg")));
            }
            case "saturn" -> {
                diffuseMap = new Image(String.valueOf(new File("E:\\VSCODE PROJECTS\\Solar-System-Simulation\\src\\main\\java\\resources\\saturn-map.JPG")));
                illuminationMap = new Image(String.valueOf(new File("E:\\VSCODE PROJECTS\\Solar-System-Simulation\\src\\main\\java\\resources\\saturn-illumination.jpg")));
            }
            case "uranus" -> {
                diffuseMap = new Image(String.valueOf(new File("E:\\VSCODE PROJECTS\\Solar-System-Simulation\\src\\main\\java\\resources\\uranus-map.JPG")));
                illuminationMap = new Image(String.valueOf(new File("E:\\VSCODE PROJECTS\\Solar-System-Simulation\\src\\main\\java\\resources\\uranus-illumination.jpg")));
            }
            case "neptune" -> {
                diffuseMap = new Image(String.valueOf(new File("E:\\VSCODE PROJECTS\\Solar-System-Simulation\\src\\main\\java\\resources\\neptune-map.JPG")));
                illuminationMap = new Image(String.valueOf(new File("E:\\VSCODE PROJECTS\\Solar-System-Simulation\\src\\main\\java\\resources\\neptune-illumination.jpg")));
            }
        }

        planetMaterial.setDiffuseMap(diffuseMap);
        planetMaterial.setSelfIlluminationMap(illuminationMap);
        planet.setMaterial(planetMaterial);

        planet.getTransforms().add(new Translate(0, 0, -distance));

        prepareOrbit();
        return new Node[]{planet};
    }

    public void prepareOrbit() {
        Rotate rotate = new Rotate(0, 0, 0, distance, new Point3D(0, 1, 0));
        Rotate r = new Rotate(0, 0, 0, 0, new Point3D(0, 1, 0));
        planet.getTransforms().addAll(rotate, r);

        timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                r.setAngle(r.getAngle() + rates[0]); // FOR SPIN
                rotate.setAngle(rotate.getAngle() + rates[1]); // FOR ORBIT
            }
        };
        timer.start();
    }
}