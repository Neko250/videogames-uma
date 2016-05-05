package blocksGame;

import com.sun.j3d.utils.geometry.Box;

import javax.media.j3d.Appearance;
import javax.media.j3d.Material;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

public class Block {
    private Appearance ap = new Appearance();
    private Box box;
    private boolean isLandedOn;
    Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
    Color3f blue = new Color3f(0.3f, 0.3f, 0.8f);
    Color3f specular = new Color3f(0.9f, 0.9f, 0.9f);
    Material blueMat = new Material(blue, blue, blue, specular, 25.0f);
    private boolean isRolledOver;
    private int x;
    private int y;
    private int color;

    public Block(int iniX, int iniY) {
        blueMat.setLightingEnable(true);
        blueMat.setDiffuseColor(black);
        x = iniX;
        y = iniY;
        isLandedOn = false;
        isRolledOver = false;
        boxGridScene();
    }

    public void setX(int newX) {
        x = newX;
    }

    public int getX() {
        return x;
    }

    public void setY(int newY) {
        y = newY;
    }

    public int getY() {
        return y;
    }

    public TransformGroup boxGridScene() {
        ap.setCapability(Appearance.ALLOW_MATERIAL_READ);
        ap.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
        ap.setMaterial(blueMat);
        Transform3D t3d = new Transform3D();
        t3d.set(new Vector3f(1.2f * (x + 1), 1.2f * (y + 1), 0));
        TransformGroup tg = new TransformGroup(t3d);
        Box box = new Box(0.5f, 0.5f, 0.5f, Box.GENERATE_NORMALS, ap);
        tg.addChild(box);
        return tg;
    }

    public void updateBoxGridColor() {
        blueMat = new Material(black, black, blue, specular, 25.0f);
        ap.setMaterial(blueMat);
    }

    public boolean isLandedOn() {
        return isLandedOn;
    }

    public void setLandedOn(boolean bool) {
        isLandedOn = bool;
    }

    public boolean isRolledOver() {
        return isRolledOver;
    }

    public void setRolledOver(boolean bool) {
        isRolledOver = bool;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int newColor) {
        color = newColor;
    }
}
