package com.quaap.computationaldemonology.functions;

import android.content.Context;
import android.graphics.Canvas;

/**
 * Created by tom on 12/6/16.
 *
 *    Copyright (C) 2016  tom
 *
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
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