package com.quaap.computationaldemonology.functions;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by tom on 12/4/16.
 */

public abstract class Drawgorythm {
    protected Paint [] mForeground;

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

    public Drawgorythm() {

    }

    public void setPaints(Paint foreground, Paint background) {
        mForeground = new Paint[16];
        int color = foreground.getColor();
        int alpha = foreground.getAlpha();
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);

        for (int i=0; i<mForeground.length; i++) {
            mForeground[i] = new Paint();
            mForeground[i].setARGB(getRVal(alpha), getRVal(red), getRVal(green), getRVal(blue));
        }
        mBackground = background;
    }

    protected Paint getRandomForeground() {
        return mForeground[(int)(Math.random()*mForeground.length)];
    }

    private static int getRVal(int base) {
        return getVal(base, (int)((Math.random()-.5)*96));
    }

    private static int getVal(int base, int diff) {
        int value=base+diff;
        if (value>255) value=255;
        if (value<0) value=0;
        return value;
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
