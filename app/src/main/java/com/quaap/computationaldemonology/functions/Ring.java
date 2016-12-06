package com.quaap.computationaldemonology.functions;

import android.graphics.Canvas;

/**
 * Created by tom on 12/6/16.
 */

public abstract class Ring extends Drawgorythm {

    double rMax = 1;
    double r;
    double rad = 0;

    long started;

    @Override
    public void canvasChanged(final Canvas canvas) {
        super.canvasChanged(canvas);
        rMax = Math.min(mHeight, mWidth) / 2.5;
        r = 1;
        rad = 0;
        started = System.currentTimeMillis();
    }
    @Override
    public void doDraw(final Canvas canvas, final long ticks) {
        if (r<rMax) {
            r+=(rMax - r)/rMax;
        }
    }
}