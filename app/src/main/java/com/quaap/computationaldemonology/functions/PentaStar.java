package com.quaap.computationaldemonology.functions;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

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

public class PentaStar extends Ring {

    private double rad2 = rad;

    private double trails = 2;

    private double speed = 1;

    private double modspeed = speed;

    private double dmove = 1;


    public PentaStar(Context context) {
        super(context);
    }


    protected void incRad() {
        rad += Math.PI * 4.0 / 5.0;
    }
    public void doDraw(final Canvas canvas, final long ticks) {
        super.doDraw(canvas,ticks);

        if (Math.abs(mMoveZ)>.1) {
            dmove += (mMoveZ+mMoveY)/5;
            if (dmove>3) dmove = 3;
            if (dmove<-3) dmove = -3;
        }

        if (mTouchDY!=0) {
            trails+= Math.signum(mTouchDY)/2;
            if (trails>7) trails=7;
            if (trails<1) trails=1;

        }

        if (mTouchDX!=0) {
            speed+= Math.signum(mTouchDX)/5;
            if (speed>60) speed=60;
            if (speed<1) speed=1;
            modspeed = speed;
        }
        modspeed += (mMoveX + mMoveY + mMoveZ)/20;
        if (modspeed>5) modspeed=5;
        if (modspeed<-5) modspeed=-5;

        rad = 0;

        for (long j = 0; j < 5; j++) {
            double lastX = (float) (r * Math.sin(rad)) + mCenterX;
            double lastY = (float) (r * Math.cos(rad)) + mCenterY;

            incRad();

            double nextX = (float) (r * Math.sin(rad)) + mCenterX;
            double nextY = (float) (r * Math.cos(rad)) + mCenterY;

            double x = lastX;
            double y = lastY;

            double dX = nextX - lastX;
            double dY = nextY - lastY;

            double [] lastPointsX = new double[(int)trails];
            double [] lastPointsY = new double[(int)trails];


            int g = 0;
            do {

                double fac = (dX==0?0:Math.abs(dY / dX))+1;
                if (fac > 2) fac = 2;
                if (fac < 1) fac = 1;
                double xp = x + Math.signum(dX) / fac;
                double yp = lastY + dY * (x - lastX) / dX;

                //canvas.drawLine((float)x, (float)y, (float)xp, (float)yp, mForeground);

                rad2 = Math.atan((y - mCenterY) / (x - mCenterX)) + Math.PI/3;
                Paint paint = getRandomForeground();
                for (int i = 0; i < (int)trails; i++) {
                    int p=i+1;
                    int sizex = (int) (dmove * r / 3 / p * Math.cos((Math.PI * p - rad2)*modspeed * 15 * p)) + 1;
                    int sizey = (int) (dmove * r / 3 / p * Math.sin((Math.PI * p - rad2)*modspeed * 15 * p)) + 1;

                    double px = xp + sizex;
                    double py = yp + sizey;

                    if (g>0) {
                        canvas.drawLine((float) px, (float) py, (float) lastPointsX[i], (float) lastPointsY[i], paint);
                    }

                    //canvas.drawPoint((float) px, (float) py, paint);

                    lastPointsX[i] =  px;
                    lastPointsY[i] =  py;

                    //canvas.drawRect((float) xp + sizex, (float) yp + sizey, (float) xp + sizex+p, (float) yp + sizey+p, mForeground);
                }

                x = xp;
                y = yp;
                g++;

            } while (Math.round(x+1) != Math.round(nextX+1));

        }
    }

}
