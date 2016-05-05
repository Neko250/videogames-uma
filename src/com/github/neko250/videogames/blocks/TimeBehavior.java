package com.github.neko250.videogames.blocks;

import javax.media.j3d.Behavior;
import javax.media.j3d.WakeupCondition;
import javax.media.j3d.WakeupOnElapsedTime;
import java.util.Enumeration;

public class TimeBehavior extends Behavior {
    private WakeupCondition timeOut;
    private int timeDelay;
    private WrapBlocksGame wbg;

    public TimeBehavior(int td, WrapBlocksGame w) {
        timeDelay = td;
        wbg = w;
        timeOut = new WakeupOnElapsedTime(timeDelay);
    }

    public void initialize() {
        wakeupOn(timeOut);
    }

    public void processStimulus(Enumeration criteria) {
        wbg.setRemainingTime(wbg.getRemainingTime() - 1);
        wbg.updateText();
        wakeupOn(timeOut);
    }
}
