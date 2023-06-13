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
public class Mercury {
    private final Sphere mercury = new Sphere(25);
    private double[] original = {.2, -0.12};
    private double[] rates = {.2, -0.12};

    private final int mercuryDistance = 280;
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
    public Node[] prepareMercury(){
        PhongMaterial mercuryMaterial = new PhongMaterial();
        mercuryMaterial.setDiffuseMap(new Image(String.valueOf(new File("E:\\VSCODE PROJECTS\\Solar-System-Simulation\\src\\main\\java\\resources\\mercury-illumination.jpg"))));
        mercuryMaterial.setSelfIlluminationMap(new Image(String.valueOf(new File("E:\\VSCODE PROJECTS\\Solar-System-Simulation\\src\\main\\java\\resources\\mercury-map.jpg"))));

        mercury.setMaterial(mercuryMaterial);

        mercury.getTransforms().add(new Translate(0, 0, -mercuryDistance));

        prepareOrbit();
        return new Node[]{mercury};
    }

    public void prepareOrbit() {
        Rotate rotate = new Rotate(0, 0, 0, mercuryDistance, new Point3D(0, 1, 0));
        Rotate r = new Rotate(0, 0, 0, 0, new Point3D(0, 1, 0));
        mercury.getTransforms().addAll(rotate, r);

        timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                r.setAngle(r.getAngle() + rates[0]); // FOR SPIN
                rotate.setAngle(rotate.getAngle() - rates[1]); // FOR ORBIT
            }
        };
        timer.start();
    }
}