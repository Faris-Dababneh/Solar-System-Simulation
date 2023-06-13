// 80 units


package project.sss;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point3D;
import javafx.scene.AmbientLight;
import javafx.scene.Node;
import javafx.scene.PointLight;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;


import java.io.File;

public class Earth {
    private final Sphere earth = new Sphere(60);
    private final Sphere moon = new Sphere(15);
    private final Cylinder axis = new Cylinder(2, 130);

    private final Cylinder axisMoon = new Cylinder(2, 70);

    private final PointLight pointLight = new PointLight();

    // Rates for earth and moon spins
    //                       ESpin, E & A Orbit, MSpin, MOrbit
    private double[] original = {2.7, -0.010, -0.010, 0.1, -0.18};
    private double[] rates = {2.7, -0.010, -0.010, 0.1, -0.18};

    // CHANGE THESE VALUES WHEN ADJUSTING THE DISTANCE FROM THE SUN
    private final int earthDistance = 905;
    // KEEP THEM WITHIN 100 OF EACH OTHER
    private final int moonDistance = 805;
    // ************************************************************************

    private AnimationTimer timer;
    public Sphere getEarth() {
        return earth;
    }

    public Cylinder getAxis() {
        return axis;
    }
    public PointLight getPointLight() {
        return pointLight;
    }

    public void pause(boolean b) {
        if (b) {
            timer.stop();
        } else {
            timer.start();
        }
    }

    public void speed(double multiplier) {
        for (int i = 0; i < original.length; i++) {
            rates[i] = original[i] * multiplier;
        }
    }
    public Node[] prepareMoon(){
        PhongMaterial moonMaterial = new PhongMaterial();
        moonMaterial.setDiffuseMap(new Image(String.valueOf(new File("E:\\VSCODE PROJECTS\\Solar-System-Simulation\\src\\main\\java\\resources\\moon-mapp.jpg"))));

        PhongMaterial axisMat = new PhongMaterial();
        axisMat.setDiffuseColor(Color.RED);
        moon.setMaterial(moonMaterial);
        axisMoon.setMaterial(axisMat);

        moon.getTransforms().add(new Translate(0, 0, -moonDistance));
        axisMoon.getTransforms().add(new Translate(0, 0, -moonDistance));


        return new Node[]{moon, axisMoon};
    }

    public Node[] prepareEarth() {
        //prepareMoon();
        PhongMaterial earthMaterial = new PhongMaterial();
        earthMaterial.setDiffuseMap(new Image(String.valueOf(new File("E:\\VSCODE PROJECTS\\Solar-System-Simulation\\src\\main\\java\\resources\\earth-map.jpg"))));
        earthMaterial.setSelfIlluminationMap(new Image(String.valueOf(new File("E:\\VSCODE PROJECTS\\Solar-System-Simulation\\src\\main\\java\\resources\\earth-illumination.jpg"))));
        earthMaterial.setSpecularMap(new Image(String.valueOf(new File("E:\\VSCODE PROJECTS\\Solar-System-Simulation\\src\\main\\java\\resources\\earth-specular.tif"))));
        earthMaterial.setBumpMap(new Image(String.valueOf(new File("E:\\VSCODE PROJECTS\\Solar-System-Simulation\\src\\main\\java\\resources\\earth-bump.tif"))));

        PhongMaterial axisMat = new PhongMaterial();
        axisMat.setDiffuseColor(Color.RED);
        axis.setMaterial(axisMat);

        //r.setAngle(23.5);

        earth.setRotationAxis(Rotate.Y_AXIS);
        earth.setMaterial(earthMaterial);
        earth.getTransforms().add(new Translate(0, 0, -earthDistance));
        axis.getTransforms().add(new Translate(0, 0, -earthDistance));


        //earth.setLayoutX(-500);
        prepareMoon();
        prepareOrbit();

        return new Node[]{earth, axis, moon};
    }

    public void prepareOrbit() {
        Rotate rotate = new Rotate(0, 0, 0, earthDistance, new Point3D(0, 1, 0));
        Rotate r = new Rotate(0, 0, 0, 0, new Point3D(0, 1, 0));
        Rotate tilt = new Rotate();
        tilt.setAngle(23.5);

        Rotate anchor = new Rotate(0, 0, 0, moonDistance, new Point3D(0, 1, 0));

        Rotate moonRotate = new Rotate(0, 0, 0, -100, new Point3D(0, 1, 0));
        Rotate moonR = new Rotate(0, 0, 0, 0, new Point3D(0, 1, 0));
        //Rotate seasons = new Rotate(0, 0, 0, 0, new Point3D(1, 0, 0));

        earth.getTransforms().addAll(rotate, r, tilt);
        axis.getTransforms().addAll(rotate, tilt);
        moon.getTransforms().addAll(anchor, moonRotate, moonR);

        //earth.getEarth().setRotationAxis(Rotate.X_AXIS);

        timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                r.setAngle(r.getAngle() + rates[0]); // FOR SPIN
                rotate.setAngle(rotate.getAngle() - rates[1]); // FOR ORBIT
                anchor.setAngle(anchor.getAngle() - rates[2]);


                moonR.setAngle(moonR.getAngle() - rates[3]); // FOR SPIN
                moonRotate.setAngle(moonRotate.getAngle() - rates[4]); // FOR ORBIT

            }
        };
        timer.start();
    };



}