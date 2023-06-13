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
public class Venus {
    private final Sphere venus = new Sphere(58);
    private double[] original = {.02, -0.03};
    private double[] rates = {.02, -0.03};

    private final int distance = 560;
    private AnimationTimer timer;

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
    public Node[] prepareVenus(){
        PhongMaterial venusMaterial = new PhongMaterial();
        venusMaterial.setDiffuseMap(new Image(String.valueOf(new File("E:\\VSCODE PROJECTS\\Solar-System-Simulation\\src\\main\\java\\resources\\venus-map.jpg"))));
        venusMaterial.setSelfIlluminationMap(new Image(String.valueOf(new File("E:\\VSCODE PROJECTS\\Solar-System-Simulation\\src\\main\\java\\resources\\venus-illumination.jpg"))));

        venus.setMaterial(venusMaterial);

        venus.getTransforms().add(new Translate(0, 0, -distance));

        prepareOrbit();
        return new Node[]{venus};
    }

    public void prepareOrbit() {
        Rotate rotate = new Rotate(0, 0, 0, distance, new Point3D(0, 1, 0));
        Rotate r = new Rotate(0, 0, 0, 0, new Point3D(0, 1, 0));
        venus.getTransforms().addAll(rotate, r);

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