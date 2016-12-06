package com.quaap.computationaldemonology.functions;

import android.graphics.Canvas;

/**
 * Created by tom on 12/6/16.
 */

public class PentaRing extends Ring {
    private float lastX = 0;
    private float lastY = 0;
    private float nextX = 0;
    private float nextY = 0;

    private double x;
    private double y;
    private double dX;
    private double dY;

    private double rad2 = rad;

    int trails = 7;

    double dmove = 1;

    public void doDraw(final Canvas canvas, final long ticks) {
        super.doDraw(canvas,ticks);

        // rad = Math.atan((mTouchY - mCenterY) / (mTouchX - mCenterX));
        // rad2 = rad;
        if (mTouchDY!=0) {
            trails+= Math.signum(mTouchDY);
            if (trails>15) trails=15;
            if (trails<2) trails=2;

        }

        if (Math.abs(mMoveZ)>.1) {
            dmove += (mMoveZ+mMoveY)/5;
            if (dmove>5) dmove = 5;
            if (dmove<-5) dmove = -5;
        }

        for (long j = 0; j < 5; j++) {
            lastX = (float) (r * Math.sin(rad)) + mCenterX;
            lastY = (float) (r * Math.cos(rad)) + mCenterY;

            rad += Math.PI * 2.0 / 5.0;
            nextX = (float) (r * Math.sin(rad)) + mCenterX;
            nextY = (float) (r * Math.cos(rad)) + mCenterY;

            x = lastX;
            y = lastY;

            dX = nextX - lastX;
            dY = nextY - lastY;

            do {

                double fac = (dX==0?0:Math.abs(dY / dX))+1;
                double xp = x + Math.signum(dX) / fac;
                double yp = lastY + dY * (x - lastX) / dX;

                //canvas.drawLine((float)x, (float)y, (float)xp, (float)yp, mForeground);

                rad2 = Math.atan((y - mCenterY) / (x - mCenterX));

                for (int p = 1; p < trails; p++) {
                    int sizex = (int) (dmove * r / 5 / p * Math.cos((Math.PI * p - rad2) * 10 * p)) + 1;
                    int sizey = (int) (dmove * r / 5 / p * Math.sin((Math.PI * p - rad2) * 10 * p)) + 1;
                    canvas.drawPoint((float) xp + sizex, (float) yp + sizey, getRandomForeground());
                    //canvas.drawRect((float) xp + sizex, (float) yp + sizey, (float) xp + sizex+p, (float) yp + sizey+p, mForeground);
                }

                x = xp;
                y = yp;


            } while (Math.round(x) != Math.round(nextX));

        }
    }

}