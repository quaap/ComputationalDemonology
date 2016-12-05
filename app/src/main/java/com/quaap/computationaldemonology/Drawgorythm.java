package com.quaap.computationaldemonology;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by tom on 12/4/16.
 */

public abstract class Drawgorythm {
    protected Paint mForeground;

    protected Paint mBackground;

    protected int mCenterX;
    protected int mCenterY;

    protected int mWidth;
    protected int mHeight;

    protected float mTouchX;
    protected float mTouchY;
    protected float mTouchDX;
    protected float mTouchDY;

    protected boolean done=false;

    public void setPaints(Paint foreground, Paint background) {
        mForeground = foreground;
        mBackground = background;
    }

    public void canvasChanged(final Canvas canvas) {
        mWidth = canvas.getWidth();
        mHeight = canvas.getHeight();
        mCenterX = mWidth / 2;
        mCenterY = mHeight / 2;
        done = false;
    }

    public abstract void doDraw(final Canvas canvas, final long ticks);


    public void touched(int action, float touchX, float touchY, float touchDX, float touchDY) {
        mTouchX = touchX;
        mTouchY = touchY;
        mTouchDX = touchDX;
        mTouchDY = touchDY;
    }
}


abstract class Ring extends Drawgorythm {

    double r = 0;
    double rad = 0;

    @Override
    public void canvasChanged(final Canvas canvas) {
        super.canvasChanged(canvas);
        r = Math.min(mHeight, mWidth) / 3;
        rad = 0;
    }

}



class FuzzyRing extends Ring {

    @Override
    public void doDraw(final Canvas canvas, final long ticks) {

        for (long j=0; j<ticks; j++) {

            for (int i = 0; i < 2; i++) {
                double rad1 = rad;
                if (i == 1) rad1 = i * 2 * Math.PI - rad;

                double rnd = Math.random();
                int sizex = (int) (rnd * r / 10 * Math.cos(rad1 * 150)) + 1;
                int sizey = (int) (rnd * r / 10 * Math.sin(rad1 * 150)) + 1;
                float x = (float) (r * Math.sin(rad1)) + mCenterX + sizex;
                float y = (float) (r * Math.cos(rad1)) + mCenterY + sizey;


                canvas.drawLine(x, y, x + 8, y + 8, mForeground);
            }

            rad += .001;

        }


    }
}


class BarbedRing extends Ring {

    double size = 2;
    double speed = 40;
    double modsize = size;
    double dsize = .1;

    public void doDraw(final Canvas canvas, final long ticks) {

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


            float x = (float) (r * Math.sin(rad));
            float y = (float) (r * Math.cos(rad));
            double rnd = Math.random();
            int sizex = (int) (modsize*rnd * r / 10 * Math.cos(rad * speed)) + 1;
            int sizey = (int) (modsize*rnd * r / 10 * Math.sin(rad * speed)) + 1;
            canvas.drawLine(mCenterX + x, mCenterY + y, mCenterX + x + sizex, mCenterY + y + sizey, mForeground);


            rad += .005;
        } while (rad<Math.PI*2);

    }

}


class PentaRing extends Ring {
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


    public void doDraw(final Canvas canvas, final long ticks) {

       // rad = Math.atan((mTouchY - mCenterY) / (mTouchX - mCenterX));
       // rad2 = rad;
        if (mTouchDY!=0) {
            trails+= Math.signum(mTouchDY);
            if (trails>15) trails=15;
            if (trails<2) trails=2;

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
                    int sizex = (int) (r / 5 / p * Math.cos((Math.PI * p - rad2) * 10 * p)) + 1;
                    int sizey = (int) (r / 5 / p * Math.sin((Math.PI * p - rad2) * 10 * p)) + 1;
                    canvas.drawPoint((float) xp + sizex, (float) yp + sizey, mForeground);
                    //canvas.drawRect((float) xp + sizex, (float) yp + sizey, (float) xp + sizex+p, (float) yp + sizey+p, mForeground);
                }

                x = xp;
                y = yp;


            } while (Math.round(x) != Math.round(nextX));

        }
    }

}

/////////////////////////
//penta
//    double r = 0;
//    double rad = 0;
//    float lastX = 0;
//    float lastY = 0;
//    private void doDraw(final Canvas canvas) {
//        // Log.d("GraphicDmn", "doDraw");
//
//        float mx = canvas.getWidth() / 2;
//        float my = canvas.getHeight() / 2;
//
//
//        if (rad/Math.PI < Math.PI*10) {
//
//            r = Math.min(canvas.getWidth(), canvas.getHeight()) / 3;
//
//            float x = (float) (r * Math.sin(rad)) + mx;
//            float y = (float) (r * Math.cos(rad)) + my;
//            if (lastX != 0 && lastY != 0) {
//                canvas.drawLine(x,y, lastX, lastY, mLinePaint);
//               // canvas.drawLine(x,y, mx, my, mLinePaint);
//            }
//
//            lastX = x;
//            lastY = y;
//
//            rad += Math.PI * 2.0 / 5.0;
//
//        }
//
//
//    }

/////////////////////////
//fuzzy circle
//    double r = 0;
//    double rad = 0;
//    private void doDraw(final Canvas canvas) {
//       // Log.d("GraphicDmn", "doDraw");
//
//
//
//        if (rad/Math.PI < Math.PI*10) {
//            float mx = canvas.getWidth() / 2;
//            float my = canvas.getHeight() / 2;
//
//            r = Math.min(canvas.getWidth(), canvas.getHeight()) / 3;
//
//            for (int i = 0; i < 2; i++) {
//                double rad1 = rad;
//                if (i == 1) rad1 = i * Math.PI - rad;
//                float x = (float) (r * Math.sin(rad1));
//                float y = (float) (r * Math.cos(rad1));
//                double rnd = Math.random();
//                int sizex = (int) (rnd * 4 * r / 10 * Math.sin(rad1)) + 1;
//                int sizey = (int) (rnd * 4 * r / 10 * Math.cos(rad1)) + 1;
//                canvas.drawLine(mx + x, my + y, mx + x + sizex, my + y + sizey, mLinePaint);
//            }
//
//            rad += .05;
//        }
//
//    }
