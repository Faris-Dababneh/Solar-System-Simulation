package project.sss;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;

import java.io.File;

public class Sun {

    private final Sphere sun = new Sphere(150);
    public Sphere getSun() {
        return sun;
    }

    public Node[] prepareSun() {
        Rotate rotate = new Rotate();

        PhongMaterial sunMaterial = new PhongMaterial();
        sunMaterial.setDiffuseMap(new Image(String.valueOf(new File("E:\\VSCODE PROJECTS\\Solar-System-Simulation\\src\\main\\java\\resources\\sun-map.jpg"))));
        //sunMaterial.setSelfIlluminationMap(new Image(String.valueOf(new File("E:\\VSCODE PROJECTS\\Solar-System-Simulation\\src\\main\\java\\resources\\sun-illumination.jpg"))));
        //sunMaterial.setSpecularMap(new Image(String.valueOf(new File("E:\\VSCODE PROJECTS\\Solar-System-Simulation\\src\\main\\java\\resources\\sun-specular.tif"))));
        //sunMaterial.setBumpMap(new Image(String.valueOf(new File("E:\\VSCODE PROJECTS\\Solar-System-Simulation\\src\\main\\java\\resources\\sun-bump.tif"))));

        PhongMaterial axisMat = new PhongMaterial();
        axisMat.setDiffuseColor(Color.RED);
        // FOR sun'S TITLT
        Cylinder axis = new Cylinder(6, 360);
        axis.rotateProperty().set(7.25);
        axis.setMaterial(axisMat);

        rotate.setAngle(7.25);

        sun.setRotationAxis(Rotate.Y_AXIS);
        sun.setMaterial(sunMaterial);


        return new Node[]{sun, axis};
    }
    
}
