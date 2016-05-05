package com.github.neko250.videogames.pong;

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.geometry.Text2D;
import com.sun.j3d.utils.image.TextureLoader;
import javafx.scene.shape.*;

import javax.media.j3d.*;
import javax.media.j3d.Shape3D;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import java.awt.*;

public class Figures {
    public static TransformGroup getLight() {
        TransformGroup tg = new TransformGroup();

        AmbientLight ambientLight = new AmbientLight(new Color3f(1, 1, 0.5f));
        ambientLight.setInfluencingBounds(new BoundingSphere(new Point3d(), 10));
        tg.addChild(ambientLight);

        PointLight pointLight = new PointLight(new Color3f(1, 1, 1), new Point3f(0, 0, 2), new Point3f(0.1f, 0.1f, 0.1f));
        pointLight.setInfluencingBounds(new BoundingSphere(new Point3d(), 10));
        tg.addChild(pointLight);

        return tg;
    }

    public static TransformGroup getBall(Component component) {
        TransformGroup tg = new TransformGroup();
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tg.addChild(new Sphere(0.05f, getPlasticAppearance(1, 0, 1, component)));

        PointLight pointLight = new PointLight(new Color3f(1, 1, 1), new Point3f(0, 0, 0), new Point3f(0.2f, 0.2f, 0.2f));
        pointLight.setInfluencingBounds(new BoundingSphere(new Point3d(), 10));
        tg.addChild(pointLight);

        return tg;
    }

    public static TransformGroup getAdversary(Component component) {
        TransformGroup tg = new TransformGroup();
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tg.addChild(new Box(0.3f, 0.3f, 0.03f, getWoodAppearance(0, 1, 1, component)));
        return tg;
    }

    public static TransformGroup getText(String text) {
        TransformGroup tg = new TransformGroup();
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tg.addChild(new Text2D(text, new Color3f(1, 1, 1), "Times", 32, 0));
        return tg;
    }

    public static TransformGroup getField(Component component) {
        TransformGroup tg = new TransformGroup();

        Appearance[] appearance = {
            getMetalAppearance(0.01f, 0, 0, component),
            getMetalAppearance(0, 0.01f, 0, component),
            getMetalAppearance(0, 0, 0.01f, component),
            getMetalAppearance(0.01f, 0.01f, 0, component)
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
        // material.setShininess(64);
        appearance.setMaterial(material);
        return appearance;
    }

    private static Appearance getMetalAppearance(float r, float g, float b, Component component) {
        Appearance appearance = getColorAppearance(r, g, b);
        Texture texture = new TextureLoader("res/metal.jpg", component).getTexture();
        appearance.setTexture(texture);
        TextureAttributes textureAttributes = new TextureAttributes();
        textureAttributes.setTextureMode(TextureAttributes.MODULATE);
        appearance.setTextureAttributes(textureAttributes);
        return appearance;
    }

    private static Appearance getPlasticAppearance(float r, float g, float b, Component component) {
        Appearance appearance = getColorAppearance(r, g, b);
        Texture texture = new TextureLoader("res/metal.jpg", component).getTexture();
        appearance.setTexture(texture);
        TextureAttributes textureAttributes = new TextureAttributes();
        textureAttributes.setTextureMode(TextureAttributes.MODULATE);
        appearance.setTextureAttributes(textureAttributes);
        return appearance;
    }

    private static Appearance getWoodAppearance(float r, float g, float b, Component component) {
        Appearance appearance = getColorAppearance(r, g, b);
        Texture texture = new TextureLoader("res/metal.jpg", component).getTexture();
        appearance.setTexture(texture);
        TextureAttributes textureAttributes = new TextureAttributes();
        textureAttributes.setTextureMode(TextureAttributes.MODULATE);
        appearance.setTextureAttributes(textureAttributes);
        return appearance;
    }
}
