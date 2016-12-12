package com.quaap.computationaldemonology.functions;

import android.content.Context;
import android.graphics.Canvas;

/**
 * Created by tom on 12/6/16.
 */

public abstract class Ring extends Drawgorythm {

    double rMax = 1;
    double r;
    double rad = 0;

    long started;


    public Ring(Context context) {
        super(context);
    }

    @Override
    public void canvasChanged(final Canvas canvas) {
        super.canvasChanged(canvas);

        mCenterY = (int)(mHeight*3.0/8);
        rMax = Math.min(mCenterY, mCenterX) *.8;
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