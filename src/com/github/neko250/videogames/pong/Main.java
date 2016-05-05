package com.github.neko250.videogames.pong;

import com.sun.j3d.utils.applet.MainFrame;

public class Main {
    public static void main(String[] args) {
        MainFrame frame = new MainFrame(new View(), 512, 512);
        // frame.setExtendedState(MainFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }
}
