package com.quaap.computationaldemonology;

import android.graphics.Canvas;
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

    protected float mMoveX;
    protected float mMoveY;
    protected float mMoveZ;

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

    public void deviceMoved(float x, float y, float z) {
        mMoveX = x;
        mMoveY = y;
        mMoveZ = z;
        //System.out.println(String.format("%f %f %f", x, y, z));
    }
}

class TouchLightning extends Drawgorythm {

    Paint fcircle;
    Paint fsparks;
    float radius = 50;
    double r = 0;
    double rstart = Math.PI/2;
    public void canvasChanged(final Canvas canvas) {
        super.canvasChanged(canvas);
        fcircle = new Paint();
        fcircle.setStyle(Paint.Style.FILL);
        //fcircle.setStrokeWidth(15);
        fcircle.setARGB(127,100,100,164);

        fsparks = new Paint();
        fsparks.setStyle(Paint.Style.STROKE);
        fsparks.setStrokeWidth(2);
        fsparks.setARGB(127,100,100,164);

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

                int strikes = (int) (Math.random() * 6 + 3);
                for (int i = 0; i < strikes; i++) {
                    float x2 = (float) ((Math.random() + .1) * x + x1);
                    float y2 = (float) ((Math.random() + .1) * y + y1);
                    canvas.drawLine(x1, y1, x2, y2, fsparks);
                }
                r += Math.PI * 2.0 / 5.0;
            }
        }
    }
}

abstract class Ring extends Drawgorythm {

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



class FuzzyRing extends Ring {
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


            canvas.drawLine(x, y, x + 1, y + 1, mForeground);

            rad += .001;

        } while (rad<Math.PI*2);


    }
}


class BarbedRing extends Ring {

    double size = 2;
    double speed = 40;
    double modsize = size;
    double dsize = .1;

    public void doDraw(final Canvas canvas, final long ticks) {
        super.doDraw(canvas,ticks);

        if (mTouchDY!=0) {
            size+= Math.signum(mTouchDY)/5;
            if (size>15) size=15;
            if (size<1) size=1;
            modsize = size;
            dsize = .1;
        }

        modsize += dsize + (mMoveX + mMoveY)/40;
        dsize += (size - modsize)/size/10;


        if (mTouchDX!=0) {
            speed+= Math.signum(mTouchDX)/5;
            if (speed>60) speed=60;
            if (speed<4) speed=4;
        }

        speed += mMoveZ/10;

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
                    canvas.drawPoint((float) xp + sizex, (float) yp + sizey, mForeground);
                    //canvas.drawRect((float) xp + sizex, (float) yp + sizey, (float) xp + sizex+p, (float) yp + sizey+p, mForeground);
                }

                x = xp;
                y = yp;


            } while (Math.round(x) != Math.round(nextX));

        }
    }

}


class PentaStar extends Ring {
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



    public void doDraw(final Canvas canvas, final long ticks) {
        super.doDraw(canvas,ticks);

        // rad = Math.atan((mTouchY - mCenterY) / (mTouchX - mCenterX));
        // rad2 = rad;
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

                for (int p = 1; p < trails+1; p++) {
                    int sizex = (int) (r / 5 / p * Math.cos((Math.PI * p - rad2)*modspeed * 14 * p)) + 1;
                    int sizey = (int) (r / 5 / p * Math.sin((Math.PI * p - rad2)*modspeed * 14 * p)) + 1;
                    canvas.drawPoint((float) xp + sizex, (float) yp + sizey, mForeground);
                    //canvas.drawRect((float) xp + sizex, (float) yp + sizey, (float) xp + sizex+p, (float) yp + sizey+p, mForeground);
                }

                x = xp;
                y = yp;


            } while (Math.round(x+1) != Math.round(nextX+1));

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
