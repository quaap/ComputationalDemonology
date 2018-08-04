package com.quaap.computationaldemonology.functions;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;


import com.quaap.computationaldemonology.R;
import com.quaap.computationaldemonology.util.Rand;

import java.util.ArrayList;
import java.util.List;

public class Awaken extends Drawgorythm {

    private float alpha = 3;
    private float scale = .0001f;
    private Rect dstrect;
    private Rect srcrect;
    private Rect gradrect;
    private Bitmap img;
    private Bitmap img2;
    private Bitmap gradient;
    private Paint alphatweak;
    private Paint white = new Paint();

    private double draws = 0;

    private Matrix m = new Matrix();

    private UpdaterThread mUpdaterThread;

    private final Object imgsync = new Object();
    private List<RectF> poss = new ArrayList<>();
    private List<RectF> dVs = new ArrayList<>();
    private int num;
    private Canvas c;

    public Awaken(Context context) {
        super(context);

        //img = context.getResources().getDrawable(R.drawable.rift);
        dstrect = new Rect();

        gradient = BitmapFactory.decodeResource(context.getResources(),R.drawable.gradient);
        gradrect = new Rect(0,0,gradient.getWidth(),gradient.getHeight());

        alphatweak = new Paint();
        alphatweak.setColor(Color.WHITE);
        white.setColor(Color.WHITE);
        num = Rand.getInt(7,15);

    }


    @Override
    public void canvasChanged(Canvas canvas) {
        super.canvasChanged(canvas);

        for (int i=0; i<num; i++) {
            // drawbjects.add(new Drawbject(gradient.copy(Bitmap.Config.ARGB_8888,true),1,1));

            int x = Rand.getInt(mCenterX-mCenterX/10, mCenterX+mCenterX/10);
            int y = Rand.getInt(mCenterY-mCenterY/10, mCenterY);
            int w = Rand.getInt(mWidth/15, mWidth/11);
            int h = w + 2; // + Rand.getInt(mHeight/7);
            RectF dst = new RectF(x,y,x+w, y+h);

            poss.add(dst);
            dVs.add(new RectF(Rand.getFloatNeg1To1(), Rand.getFloatNeg1To1(), 0,0));

        }
        img = Bitmap.createBitmap(mWidth, mHeight, gradient.getConfig());
        img2 = Bitmap.createBitmap(mWidth, mHeight, gradient.getConfig());
        c = new Canvas();
        c.setBitmap(img2);
        //img = gradient;
        srcrect = new Rect(0, 0, img.getWidth(), img.getHeight());

        if (mUpdaterThread!=null) {
            mUpdaterThread.done=true;
        }

        mUpdaterThread = new UpdaterThread();

        mUpdaterThread.start();

    }

    @Override
    public void stopping() {
        if (mUpdaterThread!=null) {
            mUpdaterThread.done=true;
        }
    }

    private class UpdaterThread extends Thread {
        boolean done=false;
        int sleep = 50;

        @Override
        public void run() {
            int topcenter = mHeight/3;
            while(!done) {

                c.drawColor(Color.TRANSPARENT, PorterDuff.Mode.SRC);
                for (int i = 0; i < num; i++) {
                    RectF d =  poss.get(i);
                    RectF dV = dVs.get(i);

                    if (d.left<=10)  dV.left = -dV.left;
                    if (d.top<=10)  dV.top = -dV.top;
                    if (d.right>=mWidth-10)  dV.left = -dV.left;
                    if (d.bottom>=mHeight*3/4)  dV.top = -dV.top;

                    d.set(d.left+dV.left - d.left*scale, d.top+dV.top - d.top*scale,
                            d.right+dV.left+ d.left*scale, d.bottom+dV.top  + d.top*scale);

                    m.setRectToRect(new RectF(gradrect), d, Matrix.ScaleToFit.CENTER);

                    c.drawBitmap(gradient, m, white);

                    scale += .0000001; // + Math.sin(draws/5)/1000;
                }
                synchronized (imgsync) {
                    img = img2.copy(Bitmap.Config.ARGB_8888, false);
                }
                try {
                    sleep(sleep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }


    @Override
    public void doDraw(Canvas canvas, long ticks) {

        if (alpha < 254) alpha += .01 + Math.sin(draws/2);

        draws+=.1;

        alphatweak.setAlpha((int)alpha);

        synchronized (imgsync) {
            canvas.drawBitmap(img, 0,0, alphatweak);
        }

    }
}