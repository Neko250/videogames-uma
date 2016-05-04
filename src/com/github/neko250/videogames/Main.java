package com.github.neko250.videogames;

import com.sun.j3d.utils.applet.MainFrame;

public class Main {
    public static void main(String[] args) {
        MainFrame frame = new MainFrame(new View(), 300, 300);
        frame.setVisible(true);
    }
}
