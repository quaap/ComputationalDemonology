package com.quaap.computationaldemonology.functions;

import android.graphics.Canvas;

/**
 * Created by tom on 12/6/16.
 */

public class FuzzyRing extends Ring {
    double size = 1;
    double speed = 40;
    double modsize = size;
    double dsize = .1;

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
        do {

            double rnd = Math.random()/2 + .5;
            int sizex = (int) (size * rnd * r / 20 * Math.cos(rad * 20 * speed)) + 1;
            int sizey = (int) (size * rnd * r / 20 * Math.sin(rad * 20 * speed)) + 1;
            float x = (float) (r * Math.sin(rad)) + mCenterX + sizex;
            float y = (float) (r * Math.cos(rad)) + mCenterY + sizey;


            canvas.drawLine(x, y, x + 1, y + 1, getRandomForeground());

            rad += .001;

        } while (rad<Math.PI*2);


    }
}