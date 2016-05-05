package com.github.neko250.videogames;

import static com.sun.javafx.util.Utils.clamp;

public class Model {
    public static final int RIGHT = 0;
    public static final int LEFT = 1;
    public static final int UP = 2;
    public static final int DOWN = 3;

    private double ballX, ballY, ballZ;
    private double dx, dy, dz;
    private int state; // 0 -> playing | 1 -> win | 2 -> lose

    private boolean keys[] = new boolean[4];
    private double x[] = new double[2];
    private double y[] = new double[2];

    // public Audio audio = new Audio();

    public void setKey(int key, boolean value) {
        if (key >= 0 && key <= 3) {
            keys[key] = value;
        }
    }

    public void move() {
        if (state != 0) return;

        // ================
        // Ball
        double limit = 0.9;
        ballX = clamp(-limit, ballX + dx, limit);
        ballY = clamp(-limit, ballY + dy, limit);
        if (Math.abs(ballX) == limit) dx *= -1;
        if (Math.abs(ballY) == limit) dy *= -1;

        ballZ += dz;
        if (ballZ < -limit && collisionWithPad(0))
            hitWithPad(0, -limit);
        if (ballZ > limit && collisionWithPad(1))
            hitWithPad(1, limit);

        if (ballZ < -limit - 0.2) {
            state = 1;
            // audio.stopMusic();
        }

        if (ballZ > limit + 0.2) {
            state = 2;
            // audio.stopMusic();
        }

        // ================
        // Pads
        double speed = 0.032;
        double dx = 0;
        double dy = 0;

        // Adversary
        if (Math.abs(ballX - x[0]) > 0.1)
            dx = speed * Math.signum(ballX - x[0]) * 0.8f;
        if (Math.abs(ballY - y[0]) > 0.1)
            dy = speed * Math.signum(ballY - y[0]) * 0.8f;

        movePad(0, dx, dy);

        // Player
        dx = dy = 0;
        if (keys[RIGHT]) dx += speed;
        if (keys[LEFT])  dx -= speed;
        if (keys[UP])    dy += speed;
        if (keys[DOWN])  dy -= speed;

        movePad(1, dx, dy);
    }

    private boolean collisionWithPad(int pad) {
        return (ballX > x[pad] - 0.3) && (ballX < x[pad] + 0.3)
            && (ballY > y[pad] - 0.3) && (ballY < y[pad] + 0.3);
    }

    private void hitWithPad(int pad, double limit) {
        ballZ = limit;
        dz *= -1;
        dx = (ballX - x[pad]) / 6.25;
        dy = (ballY - y[pad]) / 6.25;
        // audio.playHit();
    }

    public void startGame() {
        dz = -0.016;
        dx = Math.random() * 0.032 - 0.016;
        dy = Math.random() * 0.032 - 0.016;
        x[0] = y[0] = x[1] = y[1] = 0;
        ballX = ballY = ballZ = 0;
        state = 0;
        // audio.playMusic();
    }

    private void movePad(int pos, double dx, double dy) {
        double limit = 0.7;
        x[pos] = clamp(-limit, x[pos] + dx, limit);
        y[pos] = clamp(-limit, y[pos] + dy, limit);
    }

    public double getPlayerX() {
        return x[1];
    }

    public double getPlayerY() {
        return y[1];
    }

    public double getAdversaryX() {
        return x[0];
    }

    public double getAdversaryY() {
        return y[0];
    }

    public double getBallX() {
        return ballX;
    }

    public double getBallY() {
        return ballY;
    }

    public double getBallZ() {
        return ballZ;
    }

    public int getState() {
        return state;
    }
}
