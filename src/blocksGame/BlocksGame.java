package blocksGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class BlocksGame extends JFrame implements KeyListener {
    public BlocksGame() {
        super("Blocks Game");
        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        WrapBlocksGame w3d = new WrapBlocksGame();
        c.add(w3d, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setResizable(true);
        setVisible(true);
    }

    public static void main(String[] args) {
        new BlocksGame();
    }

    public void keyPressed(KeyEvent ke) {
        int xOffset = 0;
        int yOffset = 0;

        if (ke.getKeyCode() == KeyEvent.VK_UP)
            yOffset += 1;
        else if (ke.getKeyCode() == KeyEvent.VK_DOWN)
            yOffset -= 1;
        else if (ke.getKeyCode() == KeyEvent.VK_RIGHT)
            xOffset += 1;
        else if (ke.getKeyCode() == KeyEvent.VK_LEFT)
            xOffset -= 1;
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }
}
