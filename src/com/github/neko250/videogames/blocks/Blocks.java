package com.github.neko250.videogames.blocks;

import javax.media.j3d.BranchGroup;
import java.util.Iterator;
import java.util.Vector;

public class Blocks {
    private Vector blocks = new Vector();
    BranchGroup bg = new BranchGroup();

    public BranchGroup blocksScene() {
        Iterator iterator = blocks.iterator();

        while (iterator.hasNext()) {
            bg.addChild(((Block) iterator.next()).boxGridScene());
        }

        return bg;
    }

    public Block getBlock(int x, int y) {
        Iterator iterator = blocks.iterator();

        while (iterator.hasNext()) {
            Block b = (Block) iterator.next();

            if ((b.getX() == x) && (b.getY() == y)) {
                return b;
            }
        }

        return null;
    }

    public Iterator getIterator() {
        return blocks.iterator();
    }

    public void addBlock(Block block) {
        blocks.addElement(block);
    }
}
