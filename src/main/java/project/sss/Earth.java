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
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;


import java.io.File;

public class Earth {
    private final Sphere earth = new Sphere(50);
    private final Sphere moon = new Sphere(15);
    private final Cylinder axis = new Cylinder(2, 120);

    private final Cylinder axisMoon = new Cylinder(2, 50);
    private final PointLight pointLight = new PointLight();

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
    public Node[] prepareMoon(){
        PhongMaterial moonMaterial = new PhongMaterial();
        moonMaterial.setDiffuseMap(new Image(String.valueOf(new File("C:\\Users\\882355\\IdeaProjects\\Solar System Simulation\\src\\main\\java\\resources\\moon-mapp.jpg"))));

        PhongMaterial axisMat = new PhongMaterial();
        axisMat.setDiffuseColor(Color.RED);
        moon.setMaterial(moonMaterial);
        axisMoon.setMaterial(axisMat);

        moon.getTransforms().add(new Translate(0, -200, -105));
        axisMoon.getTransforms().add(new Translate(0, -200, -105));


        return new Node[]{moon, axisMoon};
    }

    public Node[] prepareEarth() {

        PhongMaterial earthMaterial = new PhongMaterial();
        earthMaterial.setDiffuseMap(new Image(String.valueOf(new File("C:\\Users\\882355\\IdeaProjects\\Solar System Simulation\\src\\main\\java\\resources\\earth-map.jpg"))));
        earthMaterial.setSelfIlluminationMap(new Image(String.valueOf(new File("C:\\Users\\882355\\IdeaProjects\\Solar System Simulation\\src\\main\\java\\resources\\earth-illumination.jpg"))));
        earthMaterial.setSpecularMap(new Image(String.valueOf(new File("C:\\Users\\882355\\IdeaProjects\\Solar System Simulation\\src\\main\\java\\resources\\earth-specular.tif"))));
        earthMaterial.setBumpMap(new Image(String.valueOf(new File("C:\\Users\\882355\\IdeaProjects\\Solar System Simulation\\src\\main\\java\\resources\\earth-bump.tif"))));

        PhongMaterial axisMat = new PhongMaterial();
        axisMat.setDiffuseColor(Color.RED);
        // FOR EARTH'S TITLT
        //axis = new Cylinder(2, 120);
        //axis.setLayoutX(-500);
        //axis.rotateProperty().set(23.5);
        axis.setMaterial(axisMat);

        //r.setAngle(23.5);

        earth.setRotationAxis(Rotate.Y_AXIS);
        earth.setMaterial(earthMaterial);
        earth.getTransforms().add(new Translate(0, 0, -505));
        axis.getTransforms().add(new Translate(0, 0, -505));

        //prepareMoon();

        //earth.setLayoutX(-500);

        prepareOrbit();
        return new Node[]{earth, axis, moon};
    }

    public void prepareOrbit() {
        Rotate rotate = new Rotate(0, 0, 0, 800, new Point3D(0, 1, 0));
        Rotate r = new Rotate(0, 0, 0, 0, new Point3D(0, 1, 0));
        Rotate tilt = new Rotate();
        tilt.setAngle(23.5);
        //Rotate seasons = new Rotate(0, 0, 0, 0, new Point3D(1, 0, 0));


        earth.getTransforms().addAll(rotate, r, tilt);
        axis.getTransforms().addAll(rotate, tilt);
        //earth.getEarth().setRotationAxis(Rotate.X_AXIS);

        timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                r.setAngle(r.getAngle() - 1.5); // FOR SPIN
                rotate.setAngle(rotate.getAngle() + 0.03); // FOR ORBIT

            }
        };
        timer.start();
    };

}
