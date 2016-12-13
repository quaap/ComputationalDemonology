package com.quaap.computationaldemonology.functions;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.quaap.computationaldemonology.R;
import com.quaap.computationaldemonology.util.Rand;

/**
 * Created by tom on 12/7/16.
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

public class CloudChamber extends Drawgorythm {

    int num = -1;
    private double [] sparkX;
    private double [] sparkY;
    private double [] sparkOldX;
    private double [] sparkOldY;
    private double [] sparkDX;
    private double [] sparkDY;
    private Paint  [] fgs;
    private final String words;
    long totalticks = 0;
    int startchar;
    private Paint mTextPaint;


    //private Bitmap mScreen;
  //  private Canvas mCanvas;

    private long iterations = 0;
    public CloudChamber(Context context) {
        super(context);
        words = context.getString(R.string.lovecraft2);
    }

    @Override
    public void canvasChanged(Canvas canvas) {
        super.canvasChanged(canvas);
        setValues();
        mTextPaint = new Paint();
        mTextPaint.setARGB(255, 32, 32, 64);
        mTextPaint.setTextSize(80);
        mTextPaint.setTypeface(Typeface.SERIF);
        startchar=mWidth;

       // mScreen = Bitmap.createBitmap(canvas.getWidth(),canvas.getHeight(), Bitmap.Config.ARGB_8888);
       // mCanvas = new Canvas(mScreen);
    }

    private void setValues() {
        num = (int)(Math.random()*20 + 20);
        sparkX = new double[num];
        sparkY = new double[num];
        sparkOldX = new double[num];
        sparkOldY = new double[num];
        sparkDX = new double[num];
        sparkDY = new double[num];
        fgs = new Paint[num];
        for (int i=0; i<num;i++) {
            setValue(i);
        }

    }

    private void setValue(int i) {
        sparkX[i] = (Rand.getDoubleNeg1To1()/2) * mCenterX + mCenterX;
        sparkY[i] = (Rand.getDoubleNeg1To1()/2) * mCenterY + mCenterY;
        sparkOldX[i] = sparkX[i];
        sparkOldY[i] = sparkY[i];
        sparkDX[i] = Rand.getDoubleNeg1To1()/2;
        sparkDY[i] = Rand.getDoubleNeg1To1()/2;

        fgs[i] = getRandomForeground();

       // if (swirl == i) swirl=-1;
    }

   // int swirl = -1;


    @Override
    public void doDraw(Canvas canvas, long ticks) {

        totalticks += ticks;


        for (int m=0; m<20; m++) {

            for (int i = 0; i < num; i++) {
                sparkX[i] += sparkDX[i];
                sparkY[i] += sparkDY[i];
                canvas.drawLine((float) sparkX[i], (float) sparkY[i], (float) sparkOldX[i], (float) sparkOldY[i], fgs[i]);
                sparkOldX[i] = sparkX[i];
                sparkOldY[i] = sparkY[i];

                iterations++;
                sparkDX[i] += Math.cos(iterations / 6.0)/1.0;
                sparkDY[i] += Math.sin(iterations / 6.0)/1.0;

                if (sparkX[i]>mWidth || sparkX[i]<0 || sparkY[i]>mHeight ||sparkY[i]<0) {
                    setValue(i);
                }
            }
        }


        startchar-=20;

        canvas.drawText(words, startchar, mCenterY-mCenterY/2, mTextPaint);

       // canvas.drawBitmap(mScreen,0,0,null);

    }
}
