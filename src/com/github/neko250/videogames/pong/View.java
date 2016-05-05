package com.github.neko250.videogames.pong;

import com.sun.j3d.utils.universe.SimpleUniverse;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3d;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class View extends Applet implements Runnable {
    public static final boolean DEBUG = false;

    Model model = new Model();
    TransformGroup mainTG = new TransformGroup();
    TransformGroup adversaryTG;
    TransformGroup ballTG;
    TransformGroup winTG, loseTG;

    public View() {
        setLayout(new BorderLayout());
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas = new Canvas3D(config);
        add("Center", canvas);
        SimpleUniverse universe = new SimpleUniverse(canvas);
        universe.getViewingPlatform().setNominalViewingTransform();
        universe.addBranchGraph(createScene());

        canvas.addKeyListener(
            new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    keyAction(e.getKeyCode(), true);
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    keyAction(e.getKeyCode(), false);
                }

                private void keyAction(int key, boolean value) {
                    switch (key) {
                        case KeyEvent.VK_LEFT:
                            model.setKey(Model.LEFT, value);
                            break;
                        case KeyEvent.VK_RIGHT:
                            model.setKey(Model.RIGHT, value);
                            break;
                        case KeyEvent.VK_UP:
                            model.setKey(Model.UP, value);
                            break;
                        case KeyEvent.VK_DOWN:
                            model.setKey(Model.DOWN, value);
                            break;
                        case KeyEvent.VK_SPACE:
                            if (!value)
                                model.startGame();
                            break;
                        default:
                            break;
                    }
                }
            }
        );

        model.startGame();

        Thread thread = new Thread(this);
        thread.start();
    }

    public BranchGroup createScene() {
        BranchGroup bg = new BranchGroup();
        mainTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        bg.addChild(mainTG);

        mainTG.addChild(Figures.getField(this));
        mainTG.addChild(adversaryTG = Figures.getAdversary(this));
        mainTG.addChild(ballTG = Figures.getBall(this));
        mainTG.addChild(Figures.getLight());

        bg.addChild(winTG = Figures.getText("Fuck yeah !"));
        bg.addChild(loseTG = Figures.getText("Loooser..."));

        return bg;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(16);
                model.move();
                position();

                debug();
            } catch (Exception e) { }
        }
    }

    private void position() {
        // ================
        // Player position
        double[] generalPos = { -model.getPlayerX(), -model.getPlayerY(), 0.0 };
        Transform3D generalTransform = new Transform3D();
        generalTransform.set(new Vector3d(generalPos));
        mainTG.setTransform(generalTransform);

        // ================
        // Adversary position
        double[] adversaryPos = { model.getAdversaryX(), model.getAdversaryY(), -8 };
        Transform3D adversaryTransform = new Transform3D();
        adversaryTransform.set(new Vector3d(adversaryPos));
        adversaryTG.setTransform(adversaryTransform);

        // ================
        // Ball position
        double[] ballPos = { model.getBallX(), model.getBallY(), model.getBallZ() * 5 - 3 };
        Transform3D ballTransform = new Transform3D();
        ballTransform.set(new Vector3d(ballPos));
        ballTG.setTransform(ballTransform);

        // ================
        // Texts
        int state = model.getState();
        Transform3D textTransform = new Transform3D();
        Vector3d v;

        // Win
        if (state == 1)
            v = new Vector3d(-0.3f, 0, 1);
        else
            v = new Vector3d(0, 100, 0);
        textTransform.set(v);
        winTG.setTransform(textTransform);

        // Lose
        if (state == 2)
            v = new Vector3d(-0.3f, 0, 1);
        else
            v = new Vector3d(0, 100, 0);
        textTransform.set(v);
        loseTG.setTransform(textTransform);
    }

    private void debug() {
        if (DEBUG) {
            System.out.println("ball ------ (" + model.getBallX() + ", " + model.getBallY() + ", " + model.getBallZ() + ")");
            System.out.println("player ---- (" + model.getPlayerX() + ", " + model.getPlayerY() + ")");
            System.out.println("adversary - (" + model.getAdversaryX() + ", " + model.getAdversaryY() + ")");
        }
    }
}
