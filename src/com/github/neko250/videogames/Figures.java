package com.github.neko250.videogames;

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.geometry.Text2D;

import javax.media.j3d.*;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

public class Figures {
    public static TransformGroup getLight() {
        TransformGroup tg = new TransformGroup();

        AmbientLight ambientLight = new AmbientLight(new Color3f(0.5f, 0.5f, 0.5f));
        ambientLight.setInfluencingBounds(new BoundingSphere(new Point3d(), 10));
        tg.addChild(ambientLight);

        PointLight pointLight = new PointLight(new Color3f(1, 1, 1), new Point3f(0, 0, 2), new Point3f(0.1f, 0.1f, 0.1f));
        pointLight.setInfluencingBounds(new BoundingSphere(new Point3d(), 10));
        tg.addChild(pointLight);

        return tg;
    }

    public static TransformGroup getBall() {
        TransformGroup tg = new TransformGroup();
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tg.addChild(new Sphere(0.05f, getColorAppearance(0, 1, 0)));

        PointLight pointLight = new PointLight(new Color3f(0, 1, 0), new Point3f(0, 0, 0), new Point3f(0.2f, 0.2f, 0.2f));
        pointLight.setInfluencingBounds(new BoundingSphere(new Point3d(), 10));
        tg.addChild(pointLight);

        return tg;
    }

    public static TransformGroup getAdversary() {
        TransformGroup tg = new TransformGroup();
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tg.addChild(new Box(0.3f, 0.3f, 0.01f, getColorAppearance(1, 1, 0)));
        return tg;
    }

    public static TransformGroup getText(String text) {
        TransformGroup tg = new TransformGroup();
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tg.addChild(new Text2D(text, new Color3f(0, 1, 1), "Times", 32, 0));
        return tg;
    }

    public static TransformGroup getField() {
        TransformGroup tg = new TransformGroup();

        Appearance[] appearance = {
            getColorAppearance(0, 0.2f, 1),
            getColorAppearance(1, 0.2f, 0),
            getColorAppearance(0, 0, 1),
            getColorAppearance(1, 0, 0)
        };

        for (int i = 0; i < 4; i++) {
            Transform3D auxTransform = new Transform3D();
            auxTransform.rotZ(Math.PI / 2 * i);
            TransformGroup auxTG = new TransformGroup(auxTransform);
            tg.addChild(auxTG);

            Point3d[] coords = new Point3d[11 * 2];
            Vector3f[] normals = new Vector3f[11 * 2];
            for (int j = 0; j < 11; j++) {
                coords[j * 2] = new Point3d(-1, 1, 2 - j);
                coords[j * 2 + 1] = new Point3d(-1, -1, 2 - j);
                normals[j * 2] = new Vector3f(1, -1, 0);
                normals[j * 2 + 1] = new Vector3f(1, 1, 0);
            }

            int[] vertex = { coords.length - 2 };
            TriangleStripArray geometry = new TriangleStripArray(
                coords.length,
                GeometryArray.COORDINATES | GeometryArray.NORMALS,
                vertex
            );
            geometry.setCoordinates(0, coords);
            geometry.setNormals(0, normals);

            Shape3D figure = new Shape3D(geometry, appearance[i]);
            auxTG.addChild(figure);
        }

        return tg;
    }

    private static Appearance getColorAppearance(float r, float g, float b) {
        Appearance appearance = new Appearance();
        Material material = new Material();
        material.setAmbientColor(r, g, b);
        material.setDiffuseColor(r, g, b);
        material.setSpecularColor(r, g, b);
        appearance.setMaterial(material);
        return appearance;
    }
}
