package com.quaap.computationaldemonology.functions;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by tom on 12/6/16.
 */

public class PentaStar extends Ring {
    private float lastX = 0;
    private float lastY = 0;
    private float nextX = 0;
    private float nextY = 0;

    private double x;
    private double y;
    private double dX;
    private double dY;

    private double rad2 = rad;

    double trails = 2;

    private double speed = 1;

    private double modspeed = speed;
    private double dspeed = .005;

    public PentaStar(Context context) {
        super(context);
    }

    public void doDraw(final Canvas canvas, final long ticks) {
        super.doDraw(canvas,ticks);

        // rad = Math.atan((mTouchY - mCenterY) / (mTouchX - mCenterX));
        // rad2 = rad;
        if (mTouchDY!=0) {
            trails+= Math.signum(mTouchDY)/2;
            if (trails>5) trails=5;
            if (trails<1) trails=1;

        }

        if (mTouchDX!=0) {
            speed+= Math.signum(mTouchDX)/5;
            if (speed>60) speed=60;
            if (speed<1) speed=1;
            modspeed = speed;
            dspeed = .005;
        }
        modspeed += (mMoveX + mMoveY + mMoveZ)/20;

        rad = 0;

        for (long j = 0; j < 5; j++) {
            lastX = (float) (r * Math.sin(rad)) + mCenterX;
            lastY = (float) (r * Math.cos(rad)) + mCenterY;

            rad += Math.PI * 4.0 / 5.0;
            nextX = (float) (r * Math.sin(rad)) + mCenterX;
            nextY = (float) (r * Math.cos(rad)) + mCenterY;

            x = lastX;
            y = lastY;

            dX = nextX - lastX;
            dY = nextY - lastY;

            do {

                double fac = (dX==0?0:Math.abs(dY / dX))+1;
                if (fac > 2) fac = 2;
                double xp = x + Math.signum(dX) / fac;
                double yp = lastY + dY * (x - lastX) / dX;

                //canvas.drawLine((float)x, (float)y, (float)xp, (float)yp, mForeground);

                rad2 = Math.atan((y - mCenterY) / (x - mCenterX)) + Math.PI/3;

                Paint paint = getRandomForeground();
                for (int p = 1; p < trails+1; p++) {
                    int sizex = (int) (r / 4 / p * Math.cos((Math.PI * p - rad2)*modspeed * 16 * p)) + 1;
                    int sizey = (int) (r / 4 / p * Math.sin((Math.PI * p - rad2)*modspeed * 16 * p)) + 1;
                    canvas.drawPoint((float) xp + sizex, (float) yp + sizey, paint);
                    //canvas.drawRect((float) xp + sizex, (float) yp + sizey, (float) xp + sizex+p, (float) yp + sizey+p, mForeground);
                }

                x = xp;
                y = yp;


            } while (Math.round(x+1) != Math.round(nextX+1));

        }
    }

}
