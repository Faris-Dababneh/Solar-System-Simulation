package project.sss;

import javafx.animation.AnimationTimer;
import javafx.scene.AmbientLight;
import javafx.scene.Node;
import javafx.scene.PointLight;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;

import java.io.File;

public class Sun {

    private final Sphere sun = new Sphere(150);
    private final PointLight p = new PointLight();
    private final Cylinder axis = new Cylinder(6, 360);
    AnimationTimer timer;

    private double rate = 2.0;
    private double original = 2.0;

    public Sphere getSun() {
        return sun;
    }
    public Node getPointLight() {
        return p;
    }

    public Cylinder getAxis() {
        return axis;
    }

    public void pause(boolean b) {
        if (b) {
            timer.stop();
        } else {
            timer.start();
        }
    }

    public void speed(double multiplier) {
        rate = original * multiplier;
    }
    public Node[] prepareSun() {
        Rotate rotate = new Rotate();

        PhongMaterial sunMaterial = new PhongMaterial();
        sunMaterial.setDiffuseMap(new Image(String.valueOf(new File("C:\\Users\\882355\\IdeaProjects\\Solar System Simulation\\src\\main\\java\\resources\\sun-map.jpg"))));
        sunMaterial.setSelfIlluminationMap(new Image(String.valueOf(new File("C:\\Users\\882355\\IdeaProjects\\Solar System Simulation\\src\\main\\java\\resources\\sun-map.jpg"))));
        //sunMaterial.setSpecularMap(new Image(String.valueOf(new File("C:\Users\882355\IdeaProjects\Solar System Simulation\src\main\java\resources\sun-specular.tif"))));
        //sunMaterial.setBumpMap(new Image(String.valueOf(new File("C:\Users\882355\IdeaProjects\Solar System Simulation\src\main\java\resources\sun-bump.tif"))));

        PhongMaterial axisMat = new PhongMaterial();
        axisMat.setDiffuseColor(Color.RED);
        axisMat.setSelfIlluminationMap(new Image(String.valueOf(new File("C:\\Users\\882355\\IdeaProjects\\Solar System Simulation\\src\\main\\java\\resources\\red.PNG"))));

        // FOR sun'S TITLT

        axis.rotateProperty().set(7.25);
        axis.setMaterial(axisMat);

        rotate.setAngle(7.25);

        sun.setRotationAxis(Rotate.Y_AXIS);
        sun.setMaterial(sunMaterial);
        sun.getTransforms().addAll(rotate);


        prepareSpin();
        return new Node[]{sun, axis};
    }

    public void prepareSpin() {
        timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                sun.rotateProperty().set(sun.getRotate() - rate);
            }
        };
        timer.start();
    }

}
