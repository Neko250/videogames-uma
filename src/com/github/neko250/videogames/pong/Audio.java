package com.github.neko250.videogames.pong;

import javax.media.j3d.BackgroundSound;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.MediaContainer;
import javax.vecmath.Point3d;

public class Audio {
    private BackgroundSound music, hit;

    public Audio() {
        String path = System.getProperty("user.dir");
        MediaContainer hitContainer = new MediaContainer("res/hit.wav");
        hit = new BackgroundSound(hitContainer, 1.0f);
        hit.setSchedulingBounds(new BoundingSphere(new Point3d(), 10));

        MediaContainer musicContainer = new MediaContainer("res/song.wav");
        music = new BackgroundSound(musicContainer, 0.5f);
        music.setSchedulingBounds(new BoundingSphere(new Point3d(), 10));
        music.setLoop(BackgroundSound.INFINITE_LOOPS);
    }

    public void playMusic() {
        music.setEnable(true);
    }

    public void playHit() {
        hit.setEnable(true);
    }

    public void stopMusic() {
        music.setEnable(false);
    }

    public void stopHit() {
        hit.setEnable(false);
    }
}
