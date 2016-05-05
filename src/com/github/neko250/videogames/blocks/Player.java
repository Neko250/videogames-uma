package com.github.neko250.videogames.blocks;

import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

public class Player {
    private int x = 0;
    private int y = 0;
    private Transform3D t3d;
    TransformGroup tg;

    public Player(int inix, int iniy) {
        x = inix;
        y = iniy;
    }

    public int getX() {
        return x;
    }

    void setX(int newX) {
        x = newX;
    }

    public int getY() {
        return y;
    }

    public void setY(int newY) {
        y = newY;
    }

    public TransformGroup playerScene() {
        int flags = ObjectFile.RESIZE;
        ObjectFile f = new ObjectFile(flags);
        Scene s = null;

        try {
            s = f.load("res/diamond.obj");
        } catch (Exception e) {
            System.out.println("error :" + e.toString());
        }

        BranchGroup root = s.getSceneGroup();
        t3d = new Transform3D();
        t3d.setTranslation(new Vector3f(1.2f * (x + 1) - 0.4f, 1.2f * (y + 1) + 0.3f, 1.5f));
        t3d.rotY(-Math.PI / 2);
        tg = new TransformGroup(t3d);
        tg.setTransform(t3d);
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tg.addChild(root);

        return tg;
    }

    public void update(int x, int y) {
        t3d.setTranslation(new Vector3f(1.2f * (x + 1) - 0.4f, 1.2f * (y + 1) + 0.3f, 1.5f));
        tg.setTransform(t3d);
    }
}
