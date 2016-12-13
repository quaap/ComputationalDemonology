package com.quaap.computationaldemonology.functions;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

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

public class TouchLightning extends Drawgorythm {

    Paint fcircle;
    //Paint fsparks;
    final float radius = 50;
    double r = 0;
    double rstart = Math.PI/2;

    public TouchLightning(Context context) {
        super(context);
    }

    public void canvasChanged(final Canvas canvas) {
        super.canvasChanged(canvas);
        fcircle = new Paint();
        fcircle.setStyle(Paint.Style.FILL);
        //fcircle.setStrokeWidth(15);
        fcircle.setARGB(127,100,100,164);

//        fsparks = new Paint();
//        fsparks.setStyle(Paint.Style.STROKE);
//        fsparks.setStrokeWidth(2);
//        fsparks.setARGB(127,100,100,164);

    }

    @Override
    public void doDraw(Canvas canvas, long ticks) {

        if (mTouchX!=0 && mTouchY!=0) {
            rstart += .07;
            r = rstart;
            float tx = mTouchX;
            float ty = mTouchY - radius/2;
            for (int j=0; j<5; j++) {

                //canvas.drawCircle(mTouchX, mTouchY, radius, fcircle);
                double x = (radius * Math.cos(r));
                double y = (radius * Math.sin(r));

                float x1 = (float)x + tx;
                float y1 = (float)y + ty;
                canvas.drawCircle(x1, y1, 8, fcircle);
                //canvas.drawCircle(tx, ty, radius, fsparks);

                int strikes = Rand.getNumber(3,6);
                for (int i = 0; i < strikes; i++) {
                    float x2 = (float) ((Rand.getDoubleNeg1To1()/2)*radius + x/2 + x1);
                    float y2 = (float) ((Rand.getDoubleNeg1To1()/2)*radius + y/2 + y1);
                    canvas.drawLine(x1, y1, x2, y2, getRandomForeground());
                }
                r += Math.PI * 2.0 / 5.0;
            }
        }
    }
}
