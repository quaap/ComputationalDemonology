package com.quaap.computationaldemonology.functions;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.quaap.computationaldemonology.util.Rand;

/**
 * Created by tom on 12/4/16.
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

public abstract class Drawgorythm {

    private final Context mContext;
    Paint [] mForeground;

    Paint mBackground;

    int mCenterX;
    int mCenterY;

    int mWidth;
    int mHeight;

    float mTouchX;
    float mTouchY;
    float mTouchDX;
    float mTouchDY;

    float mMoveX;
    float mMoveY;
    float mMoveZ;

    private boolean done=false;

    private boolean toptouched = false;
    private boolean bottomtouched = false;
    private boolean lefttouched = false;
    private boolean righttouched = false;

    Drawgorythm(Context context) {
        mContext = context;
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

    Paint getRandomForeground() {
        return Rand.rand(mForeground);
    }

    private static int getRVal(int base) {
        return getVal(base, (int)((Rand.getDoubleNeg1To1()/2)*64));
    }

    protected static int getRVal2(int base) {
        return getVal(base, (int)((Rand.getDoubleNeg1To1()/2)*16));
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
        if (mTouchY!=0) {
            toptouched = mTouchY<mCenterY;
            bottomtouched = !toptouched;
        } else {
            toptouched = false;
            bottomtouched = false;
        }
        if (mTouchX!=0) {
            lefttouched = mTouchX<mCenterX;
            righttouched = !lefttouched;
        } else {
            lefttouched = false;
            righttouched = false;
        }
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
