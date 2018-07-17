package com.quaap.computationaldemonology.functions;

import android.content.Context;
import android.graphics.Canvas;

import com.quaap.computationaldemonology.util.Rand;

/**
 * Created by tom on 12/6/16.
 *
 *    Copyright (C) 2016   Tom Kliethermes
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

public class FuzzyRing extends Ring {
    private double size = 1;
    private double speed = 40;
    private double modsize = size;
    private double dsize = .1;

    public FuzzyRing(Context context) {
        super(context);
    }
    @Override
    public void doDraw(final Canvas canvas, final long ticks) {
        super.doDraw(canvas,ticks);

        if (mTouchDY!=0) {
            size+= Math.signum(mTouchDY)/5;
            if (size>15) size=15;
            if (size<1) size=1;
            modsize = size;
            dsize = .1;
        }

        modsize += dsize;
        dsize += (size - modsize)/size/10;


        if (mTouchDX!=0) {
            speed+= Math.signum(mTouchDX)/5;
            if (speed>60) speed=60;
            if (speed<4) speed=4;
        }
        rad = 0;
        float lastX=0;
        float lastY=0;
        do {

            double rnd = Rand.getDouble()/2 + .5;
            int sizex = (int) (size * rnd * r / 20 * Math.cos(rad * 20 * speed)) + 1;
            int sizey = (int) (size * rnd * r / 20 * Math.sin(rad * 20 * speed)) + 1;
            float x = (float) (r * Math.sin(rad)) + mCenterX + sizex;
            float y = (float) (r * Math.cos(rad)) + mCenterY + sizey;

            if (lastX!=0) {
                canvas.drawLine(x, y, lastX, lastY, getRandomForeground());
            }

            lastX = x;
            lastY = y;

            rad += .01;

        } while (rad<Math.PI*2);


    }
}