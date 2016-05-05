package com.github.neko250.videogames.blocks;

import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseZoom;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;

public class WrapBlocksGame extends JPanel implements KeyListener {
    private static final int PWIDTH = 512;
    private static final int PHEIGHT = 512;
    private static final int BOUNDSIZE = 100;
    private static final Point3d USERPOSN = new Point3d(7.5, -8, 22);
    private TimeBehavior timeBehavior;
    BranchGroup bgTime;
    private SimpleUniverse su;
    private BranchGroup sceneBG;
    private BoundingSphere bounds; // for environment nodes
    private Player player;
    private Level level;
    private Board board;
    private Canvas3D canvas3D;
    private int currentStepCount = 0;
    private boolean isWinLevel = false;
    private boolean isWinGame = false;
    private int currentLevel = 1;
    private int remainingTime = 0;
    private int maxTime = 0;
    private TransformGroup mouseControl;

    public WrapBlocksGame() {
        setLayout(new BorderLayout());
        setOpaque(false);
        setPreferredSize(new Dimension(PWIDTH, PHEIGHT));
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        canvas3D = new Canvas3D(config);
        add("Center", canvas3D);
        canvas3D.setFocusable(true);
        canvas3D.requestFocus();
        canvas3D.addKeyListener(this);
        su = new SimpleUniverse(canvas3D);
        level = new Level();
        level.load(1);
        maxTime = level.getMaxTime();
        this.setRemainingTime(level.getMaxTime());
        createSceneGraph();
        initUserPosition(); // set user's viewpoint
        su.addBranchGraph(sceneBG);
    }

    private void addMouseControl() {
        mouseControl = new TransformGroup();
        mouseControl.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        mouseControl.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        mouseControl.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
        // mouseControl.setCapability(TransformGroup.ALLOW_DETACH);
        mouseControl.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
        mouseControl.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);

        MouseRotate mouseRotate = new MouseRotate();
        mouseRotate.setTransformGroup(mouseControl);
        mouseRotate.setSchedulingBounds(new BoundingSphere());
        sceneBG.addChild(mouseRotate);

        MouseTranslate mouseTranslate = new MouseTranslate();
        mouseTranslate.setTransformGroup(mouseControl);
        mouseTranslate.setSchedulingBounds(new BoundingSphere());
        sceneBG.addChild(mouseTranslate);

        MouseZoom mouseZoom = new MouseZoom();
        mouseZoom.setTransformGroup(mouseControl);
        mouseZoom.setSchedulingBounds(new BoundingSphere());
        sceneBG.addChild(mouseZoom);

        Transform3D tt = new Transform3D();
        tt.set(new Vector3d(0, -3, 3));
        TransformGroup ttg = new TransformGroup(tt);
        ttg.addChild(mouseControl);
        sceneBG.addChild(ttg);
    }

    private void createSceneGraph() {
        sceneBG = new BranchGroup();
        sceneBG.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
        sceneBG.setCapability(BranchGroup.ALLOW_DETACH);
        sceneBG.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
        sceneBG.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
        bounds = new BoundingSphere(new Point3d(0, 0, 0), BOUNDSIZE);
        addMouseControl();
        lightScene(); // add the lights
        // sceneBG.addChild(level.getBlocks().blocksScene());
        mouseControl.addChild(level.getBlocks().blocksScene());
        Blocks blks = level.getBlocks();
        Iterator it = blks.getIterator();
        int tempx = 0;
        int tempy = 0;

        while (it.hasNext()) {
            Block b = (Block) it.next();

            if (b != null) {
                if (b.isLandedOn()) {
                    tempx = b.getX();
                    tempy = b.getY();
                    break;
                }
            }
        }

        board = new Board();
        player = new Player(tempx, tempy);
        // sceneBG.addChild(board.boardScene());
        mouseControl.addChild(board.boardScene());
        board.updateText("moves:" + currentStepCount + "(max:" + level.getMaxSteps() + ")\n" + " Time Remaining:" + level.getMaxTime());
        // sceneBG.addChild(player.playerScene());
        mouseControl.addChild(player.playerScene());
        // sceneBG.addChild(backGroundScene());
        mouseControl.addChild(backGroundScene());
        PointLight light = new PointLight();
        // sceneBG.addChild(light);
        mouseControl.addChild(light);
        timeBehavior = new TimeBehavior(1000, this);
        BoundingSphere infniteBounds = new BoundingSphere(new Point3d(), Double.POSITIVE_INFINITY);
        timeBehavior.setSchedulingBounds(infniteBounds);
        timeBehavior.setEnable(true);
        bgTime = new BranchGroup();
        bgTime.addChild(timeBehavior);
        // sceneBG.addChild(bgTime);
        mouseControl.addChild(bgTime);
        bgTime.setCapability(BranchGroup.ALLOW_DETACH);
        player.update(tempx, tempy);

        sceneBG.compile(); // fix the scene
    }

    private BranchGroup backGroundScene() {
        BranchGroup root = new BranchGroup();
        Texture starTexture = null;

        try {
            TextureLoader loader = new TextureLoader("res/stars.gif", this);
            starTexture = loader.getTexture();
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        // Create a backgound node with maximum bounds
        Background background = new Background();
        background.setApplicationBounds(new BoundingSphere(new Point3d(), Double.MAX_VALUE));
        BranchGroup backgroundGroup = new BranchGroup();

        // Set the texture for the background appearance
        Appearance backgroundAppearance = new Appearance();
        backgroundAppearance.setTexture(starTexture);

        // Create a detailed sphere with normal pointing inwards
        Sphere backgroundSphere = new Sphere(0.5f, Sphere.GENERATE_TEXTURE_COORDS | Sphere.GENERATE_NORMALS_INWARD, 100, backgroundAppearance);
        backgroundGroup.addChild(backgroundSphere);
        background.setGeometry(backgroundGroup);
        root.addChild(background);

        return root;
    }

    private void lightScene() {
        Color3f white = new Color3f(1.0f, 1.0f, 1.0f);

        // Set up the ambient light
        AmbientLight ambientLightNode = new AmbientLight(white);
        ambientLightNode.setInfluencingBounds(bounds);
        // sceneBG.addChild(ambientLightNode);
        mouseControl.addChild(ambientLightNode);

        // Set up the directional lights
        Vector3f light1Direction = new Vector3f(-1.0f, -1.0f, -1.0f);
        // left, down, backwards
        Vector3f light2Direction = new Vector3f(1.0f, -1.0f, 1.0f);
        // right, down, forwards

        DirectionalLight light1 = new DirectionalLight(white, light1Direction);
        light1.setInfluencingBounds(bounds);
        // sceneBG.addChild(light1);
        mouseControl.addChild(light1);

        DirectionalLight light2 = new DirectionalLight(white, light2Direction);
        light2.setInfluencingBounds(bounds);
        // sceneBG.addChild(light2);
        mouseControl.addChild(light2);
    }

    private void initUserPosition() {
        ViewingPlatform vp = su.getViewingPlatform();
        TransformGroup steerTG = vp.getViewPlatformTransform();
        Transform3D t3d = new Transform3D();
        steerTG.getTransform(t3d);

        // args are: viewer posn, where looking, up direction
        t3d.lookAt(USERPOSN, new Point3d(7.5, 8, 0), new Vector3d(0, 1, -1));
        t3d.invert();

        steerTG.setTransform(t3d);
    }

    private void addBlocks() {
    }

    private boolean isBlocksFinished() {
        Iterator it = level.getBlocks().getIterator();

        while (it.hasNext()) {
            Block block = (Block) it.next();

            if (!block.isRolledOver()) {
                return false;
            }
        }

        return true;
    }

    public void setRemainingTime(int newTime) {
        remainingTime = newTime;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void updateText() {
        if ((this.getRemainingTime() <= 0 || currentStepCount >= level.getMaxSteps()) && (!this.isBlocksFinished())) {
            board.updateText("Lose");
            this.canvas3D.removeKeyListener(this);
            // this.sceneBG.removeChild(bgTime);
            mouseControl.removeChild(bgTime);
            this.canvas3D.removeKeyListener(this);
            Board b = new Board();
            BranchGroup branchGroup = new BranchGroup();
            branchGroup.addChild(b.boardScene("Game Over"));
            // this.sceneBG.addChild(branchGroup);
            mouseControl.addChild(branchGroup);
        }

        board.updateText("moves:" + currentStepCount + "(max:" + level.getMaxSteps() + ")\n" + " Time Remaining:" + this.getRemainingTime());
    }

    public void keyPressed(KeyEvent ke) {
        if ((this.getRemainingTime() <= 0 || currentStepCount >= level.getMaxSteps() - 1) && (!this.isBlocksFinished())) {
            this.canvas3D.removeKeyListener(this);
            // this.sceneBG.removeChild(bgTime);
            mouseControl.removeChild(bgTime);
            Board b = new Board();
            BranchGroup branchgroup = new BranchGroup();
            branchgroup.addChild(b.boardScene("Game Over"));
            // this.sceneBG.addChild(branchgroup);
            mouseControl.addChild(branchgroup);
        }

        int xOffset = player.getX();
        int yOffset = player.getY();

        if (isWinGame) {
            isWinGame = true;
            this.canvas3D.removeKeyListener(this);
            // this.sceneBG.removeChild(bgTime);
            mouseControl.removeChild(bgTime);
            Board b = new Board();
            BranchGroup branchgroup = new BranchGroup();
            branchgroup.addChild(b.boardScene("Win Game!"));
            // this.sceneBG.addChild(branchgroup);
            mouseControl.addChild(branchgroup);

            return;
        }

        if (isWinLevel) {
            isWinLevel = false;
            // this.sceneBG.addChild(bgTime);
            mouseControl.addChild(bgTime);

            /*
            if (currentLevel == 3) {
                isWinGame = true;
                this.canvas3D.removeKeyListener(this);
                // this.sceneBG.removeChild(bgTime);
                mouseControl.removeChild(bgTime);
                Board b = new Board();
                BranchGroup branchgroup = new BranchGroup();
                branchgroup.addChild(b.boardScene("Win Game!"));
                // this.sceneBG.addChild(branchgroup);
                mouseControl.addChild(branchgroup);

                return;
            }
            */

            currentLevel++;
            level.load(currentLevel);
            this.setRemainingTime(level.getMaxTime());
            System.err.println("this.remainingTime: " + this.remainingTime);
            currentStepCount = 0;
            sceneBG.detach();
            createSceneGraph();
            su.addBranchGraph(sceneBG);

            return;
        }

        int k = ke.getKeyCode();
        if (k == KeyEvent.VK_UP)
            yOffset += 1;
        else if (k == KeyEvent.VK_DOWN)
            yOffset -= 1;
        else if (k == KeyEvent.VK_RIGHT)
            xOffset += 1;
        else if (k == KeyEvent.VK_LEFT)
            xOffset -= 1;

        if ((k == KeyEvent.VK_UP) || (k == KeyEvent.VK_DOWN) || (k == KeyEvent.VK_RIGHT) || (k == KeyEvent.VK_LEFT)) {
            Block block = level.getBlocks().getBlock(xOffset, yOffset);

            if (block != null) {
                player.setX(xOffset);
                player.setY(yOffset);
                block.updateBoxGridColor();
                block.setRolledOver(true);
                player.update(xOffset, yOffset);
                currentStepCount++;
                board.updateText("moves:" + currentStepCount + "(max:" + level.getMaxSteps() + ")\n" + " Time Remaining:" + this.getRemainingTime());
            }

            if (isBlocksFinished()) {
                /*
                if (currentLevel == 3) {
                    isWinGame = true;
                    this.canvas3D.removeKeyListener(this);
                    // this.sceneBG.removeChild(bgTime);
                    mouseControl.removeChild(bgTime);
                    Board b = new Board();
                    BranchGroup branchgroup = new BranchGroup();
                    branchgroup.addChild(b.boardScene("Win Game!"));
                    // this.sceneBG.addChild(branchgroup);
                    mouseControl.addChild(branchgroup);
                    isWinGame = true;

                    return;
                }
                */

                // this.sceneBG.removeChild(bgTime);
                mouseControl.removeChild(bgTime);
                Board b = new Board();
                BranchGroup branchgroup = new BranchGroup();
                branchgroup.addChild(b.boardScene("Win Level! Press any key to continue...."));
                // this.sceneBG.addChild(branchgroup);
                mouseControl.addChild(branchgroup);
                isWinLevel = true;
            }
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }
}
